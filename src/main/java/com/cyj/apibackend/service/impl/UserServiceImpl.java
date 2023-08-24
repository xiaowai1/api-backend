package com.cyj.apibackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyj.apibackend.common.BaseResponse;
import com.cyj.apibackend.common.ErrorCode;
import com.cyj.apibackend.common.ResultUtils;
import com.cyj.apibackend.constant.RedisConstant;
import com.cyj.apibackend.exception.BusinessException;
import com.cyj.apibackend.model.dto.user.UserLoginRequest;
import com.cyj.apibackend.model.entity.User;
import com.cyj.apibackend.model.enums.UserRoleEnum;
import com.cyj.apibackend.model.vo.LoginUserVO;
import com.cyj.apibackend.service.UserService;
import com.cyj.apibackend.mapper.UserMapper;
import com.cyj.apibackend.utils.RegexUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 *
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserMapper userMapper;

    /**
     * 密码加密盐值，混淆密码
     */
    private static final String SALT = "cyj";


    /**
     * 账号密码登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    @Override
    public BaseResponse userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            log.error("user login failed, params error");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4 || userAccount.length() > 20) {
            log.error("user login failed, userAccount Illegal ");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            log.error("user login failed, userPassword Illegal ");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 查询用户是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userAccount);
        queryWrapper.eq(User::getUserPassword, encryptPassword);
        User user = baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.error("user login failed, userAccount cannot find");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        //密码错误
        if (!encryptPassword.equals(user.getUserPassword())){
            log.info("user login failed, userPassword cannot match");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        createTokenAndSaveRedis(user);
        return ResultUtils.success(this.getLoginUserVO(user));
    }

    /**
     * 获取登录用户的前端视图
     * @param user
     * @return
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    /**
     * 获取手机验证码
     * @param userPhone
     * @return
     */
    @Override
    public BaseResponse getCode(String userPhone) {
        // 校验手机号
        if (RegexUtil.isPhoneInvalid(userPhone)){
            // 格式错误返回错误信息
            return ResultUtils.error("手机号格式错误!");
        }
        // 格式正确生成验证码
        String code = RandomUtil.randomNumbers(6);
        // 将验证码存到redis
        stringRedisTemplate.opsForValue().set(RedisConstant.LOGIN_CODE_KEY + userPhone, code, RedisConstant.LOGIN_CODE_TTL, TimeUnit.MINUTES);
//        session.setAttribute("code", code);
        // 模拟发送验证码
        log.debug("发送验证码成功,验证码为：{}", code);
        return ResultUtils.success(code);
    }

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    @Override
    public BaseResponse register(String userAccount, String userPassword, String checkPassword) {
        // 1、校验参数
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            log.error("user register failed, params error");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4 || userAccount.length() > 20) {
            log.error("user register failed, userAccount Illegal ");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "账号格式不正确");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            log.error("user register failed, userPassword Illegal ");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "密码格式不正确");
        }
        if (!userPassword.equals(checkPassword)) {
            log.error("user register failed, params error ");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 2、账户不能重复
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUserAccount, userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                log.error(userAccount + " 该账号已经被使用");
                return ResultUtils.error(ErrorCode.PARAMS_ERROR, "该账号已经被使用");
            }
            // 3、 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            // 4、 为用户分配accessKey和secretKey
            String accessKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(5));
            String secretKey = DigestUtil.md5Hex(SALT + userAccount + RandomUtil.randomNumbers(8));
            // 5、 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserName("api-" + RandomUtil.randomString(10));
            user.setUserPassword(encryptPassword);
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return ResultUtils.success(user.getId());
        }
    }

    @Override
    public BaseResponse<LoginUserVO> userPhoneLogin(UserLoginRequest userLoginRequest) {
        String userPhone = userLoginRequest.getUserPhone();
        //校验手机号格式
        if (RegexUtil.isPhoneInvalid(userPhone)){
            log.error("手机号格式错误");
            return ResultUtils.error("手机号格式错误");
        }
        String cacheCode = stringRedisTemplate.opsForValue().get(RedisConstant.LOGIN_CODE_KEY + userPhone);
        // 判断验证码
        if (cacheCode == null || !cacheCode.equals(userLoginRequest.getCode())){
            log.debug("验证码错误");
            return ResultUtils.error("验证码错误");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserPhone, userPhone);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在创建用户
        if (Objects.isNull(user)){
            user = createUserWithPhone(userPhone);
        }
        createTokenAndSaveRedis(user);
        return ResultUtils.success(this.getLoginUserVO(user));
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        // 先判断是否已登录
        String tokenKey = RedisConstant.LOGIN_USER_KEY + token;

        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(tokenKey);
        log.info("userMap:{}", userMap);
        User currentUser = BeanUtil.fillBeanWithMap(userMap, new User(), false);
        log.info("currentUser:{}", currentUser);
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 是否为管理员
     * @param user
     * @return
     */
    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        User loginUser = getLoginUser(request);
        return isAdmin(loginUser);
    }

    /**
     * 手机号登录时自动创建用户
     * @param phone
     * @return
     */
    private User createUserWithPhone(String phone) {
        //通过手机号注册的用户随机分配一个userAccount
        String defaultUserAccount = "user-" +  RandomUtil.randomString(8);
        // 手机号注册的用户默认密码位12345678
        String defaultPassword = "12345678";
        // 为用户分配accessKey和secretKey
        String accessKey = DigestUtil.md5Hex(SALT + defaultUserAccount + RandomUtil.randomNumbers(5));
        String secretKey = DigestUtil.md5Hex(SALT + defaultUserAccount + RandomUtil.randomNumbers(8));
        // 插入数据
        User user = new User();
        user.setUserAccount(defaultUserAccount);
        user.setUserPhone(phone);
        user.setUserName("api-" + RandomUtil.randomString(10));
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + defaultPassword).getBytes());
        user.setUserPassword(encryptPassword);
        user.setAccessKey(accessKey);
        user.setSecretKey(secretKey);

        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return user;
    }

    /**
     * 生成token令牌并将登陆用户信息存入redis
     * @param user
     */
    public void createTokenAndSaveRedis(User user){
        // 生成token令牌
        String token = UUID.randomUUID().toString(true);
        LoginUserVO loginUserVO = BeanUtil.copyProperties(user, LoginUserVO.class);
        //存入redis
        Map<String, Object> userMap = BeanUtil.beanToMap(loginUserVO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> {
                            if (fieldValue == null) {
                                return null;
                            }else if (fieldValue instanceof Date){
                                return DateUtil.formatDateTime((Date) fieldValue);
                            }else if (fieldValue instanceof Long) {
                                return String.valueOf(fieldValue);
                            }
                            return fieldValue.toString();
                        }));

        String tokenKey = RedisConstant.LOGIN_USER_KEY + token;
        stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
        stringRedisTemplate.expire(tokenKey, RedisConstant.LOGIN_USER_TTL, TimeUnit.MINUTES);
    }
}




