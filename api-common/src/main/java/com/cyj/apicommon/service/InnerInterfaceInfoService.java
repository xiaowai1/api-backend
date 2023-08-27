package com.cyj.apicommon.service;

import com.cyj.apicommon.model.entity.InterfaceInfo;
import com.cyj.apicommon.model.entity.User;

/**
 * @ClassName InnerUserService
 * @Description 接口服务
 * @Author chixiaowai
 * @Date 2023/8/26 14:19
 * @Version 1.0
 */

public interface InnerInterfaceInfoService {
    /**
     * 查询接口是否存在
     * @param path 接口的url
     * @param method 接口的请求方法
     * @return
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}
