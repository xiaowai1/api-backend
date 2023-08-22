package com.cyj.apibackend.utils;

import cn.hutool.core.util.StrUtil;

/**
 * @ClassName RegexUtil
 * @Description TODO
 * @Author chixiaowai
 * @Date 2023/4/4 9:55
 * @Version 1.0
 */

public class RegexUtil {
    /**
     * 是否是无效手机格式
     * @param phone 要校验的手机号
     * @return true:无效手机号，false：有效手机号
     */
    public static boolean isPhoneInvalid(String phone){
        return mismatch(phone, RegexPatterns.PHONE_REGEX);
    }
    /**
     * 是否是无效邮箱格式
     * @param email 要校验的邮箱
     * @return true:无效邮箱，false：有效邮箱
     */
    public static boolean isEmailInvalid(String email){
        return mismatch(email, RegexPatterns.EMAIL_REGEX);
    }

    /**
     * 是否是无效验证码格式
     * @param code 要校验的验证码
     * @return true:无效验证码，false：有效验证码
     */
    public static boolean isCodeInvalid(String code){
        return mismatch(code, RegexPatterns.VERIFY_CODE_REGEX);
    }

    // 校验是否不符合正则格式
    private static boolean mismatch(String str, String regex){
        if (StrUtil.isBlank(str)) {
            return true;
        }
        return !str.matches(regex);
    }
}
