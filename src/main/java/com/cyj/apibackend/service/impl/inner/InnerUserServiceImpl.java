package com.cyj.apibackend.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cyj.apibackend.common.ErrorCode;
import com.cyj.apibackend.exception.BusinessException;
import com.cyj.apibackend.mapper.UserMapper;
import com.cyj.apicommon.model.entity.User;
import com.cyj.apicommon.service.InnerUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @ClassName InnerUserServiceImpl
 * @Description TODO
 * @Author chixiaowai
 * @Date 2023/8/26 14:36
 * @Version 1.0
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 获取调用接口的用户信息
     * @param accessKey 用户的accessKey
     * @return
     */
    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isBlank(accessKey)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getAccessKey, accessKey);
        return userMapper.selectOne(queryWrapper);
    }
}
