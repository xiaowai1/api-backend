package com.cyj.apicommon.service;

import com.cyj.apicommon.model.entity.User;

/**
 * @ClassName InnerUserService
 * @Description 用户服务
 * @Author chixiaowai
 * @Date 2023/8/26 14:19
 * @Version 1.0
 */

public interface InnerUserService {
    /**
     * 查询调用接口的用户信息
     * @param accessKey 用户的accessKey
     * @return
     */
    User getInvokeUser(String accessKey);
}
