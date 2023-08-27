package com.cyj.apibackend.service;

import com.cyj.apibackend.common.BaseResponse;
import com.cyj.apibackend.model.dto.user.UserLoginRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cyj.apicommon.model.entity.User;
import com.cyj.apicommon.model.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface UserService extends IService<User> {

    /**
     * 账号密码登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    BaseResponse<LoginUserVO> userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     * @param user
     * @return
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取验证码
     * @param userPhone
     * @return
     */
    BaseResponse getCode(String userPhone);

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    BaseResponse<Long> register(String userAccount, String userPassword, String checkPassword);

    /**
     * 手机号登陆
     * @param userLoginRequest
     * @return
     */
    BaseResponse<LoginUserVO> userPhoneLogin(UserLoginRequest userLoginRequest);

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 是否为管理员
     * @param user
     * @return
     */
    boolean isAdmin(User user);

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);
}
