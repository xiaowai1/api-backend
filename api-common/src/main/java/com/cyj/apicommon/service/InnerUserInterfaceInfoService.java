package com.cyj.apicommon.service;

import com.cyj.apicommon.model.entity.User;

/**
 * @ClassName InnerUserService
 * @Description 用户接口服务
 * @Author chixiaowai
 * @Date 2023/8/26 14:19
 * @Version 1.0
 */

public interface InnerUserInterfaceInfoService {
    /**
     * 调用接口次数统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}
