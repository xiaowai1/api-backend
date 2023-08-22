package com.cyj.apibackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求
 *
 * @author chixiaowai
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 用户账号（唯一）
     */
    private String userAccount;

    /**
     * 电话（唯一）
     */
    private String userPhone;

    /**
     * 验证码
     */
    private String code;

    /**
     * 用户密码
     */
    private String userPassword;
}
