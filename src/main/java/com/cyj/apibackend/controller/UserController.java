package com.cyj.apibackend.controller;

import com.cyj.apibackend.common.BaseResponse;
import com.cyj.apibackend.common.ErrorCode;
import com.cyj.apibackend.common.ResultUtils;
import com.cyj.apibackend.model.dto.user.UserLoginRequest;
import com.cyj.apibackend.model.dto.user.UserRegisterRequest;
import com.cyj.apibackend.service.UserService;
import com.cyj.apicommon.model.entity.User;
import com.cyj.apicommon.model.vo.LoginUserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @ClassName UserController
 * @Description 用户接口
 * @Author chixiaowai
 * @Date 2023/8/22 10:39
 * @Version 1.0
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 账号密码登录
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (Objects.isNull(userLoginRequest)){
            log.error("user login failed, params error");
            ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)){
            log.error("user login failed, params error");
            ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userService.userLogin(userAccount, userPassword, request);
    }

    /**
     * 手机号登陆
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/phoneLogin")
    public BaseResponse<LoginUserVO> userPhoneLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if (Objects.isNull(userLoginRequest)){
            log.error("user login failed, params error");
            ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userService.userPhoneLogin(userLoginRequest);
    }

    /**
     * 获取手机验证码
     * @param userPhone
     */
    @GetMapping("/getCode")
    public BaseResponse getCode(String userPhone){
        return userService.getCode(userPhone);
    }

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if (Objects.isNull(userRegisterRequest)){
            log.error("user register failed, params error");
            ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            log.error("user register failed, params error");
            ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userService.register(userAccount, userPassword, checkPassword);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }
}
