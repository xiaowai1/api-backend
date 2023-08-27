package com.cyj.apibackend.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cyj.apibackend.common.ErrorCode;
import com.cyj.apibackend.exception.BusinessException;
import com.cyj.apibackend.mapper.InterfaceInfoMapper;
import com.cyj.apicommon.model.entity.InterfaceInfo;
import com.cyj.apicommon.service.InnerInterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @ClassName InnerInterfaceInfoServiceImpl
 * @Description TODO
 * @Author chixiaowai
 * @Date 2023/8/26 14:36
 * @Version 1.0
 */
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    /**
     * 获取用户调用的接口信息
     * @param url 接口url
     * @param method 接口的调用方式
     * @return
     */
    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaQueryWrapper<InterfaceInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(InterfaceInfo::getUrl, url);
        queryWrapper.eq(InterfaceInfo::getMethod, method);
        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
