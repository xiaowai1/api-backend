package com.cyj.apibackend.service.impl.inner;

import com.cyj.apibackend.mapper.UserInterfaceInfoMapper;
import com.cyj.apibackend.service.UserInterfaceInfoService;
import com.cyj.apicommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @ClassName InnerUserInterfaceInfoServiceImpl
 * @Description TODO
 * @Author chixiaowai
 * @Date 2023/8/26 14:36
 * @Version 1.0
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 统计接口调用次数
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }
}
