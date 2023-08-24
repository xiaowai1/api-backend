package com.cyj.apibackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.apibackend.common.BaseResponse;
import com.cyj.apibackend.common.DeleteRequest;
import com.cyj.apibackend.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.cyj.apibackend.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.cyj.apibackend.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.cyj.apibackend.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    /**
     * 添加接口
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    BaseResponse addInterfaceInfo(InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request);

    /**
     * 更新接口
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    BaseResponse updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest, HttpServletRequest request);

    /**
     * 删除接口
     * @param deleteRequest
     * @param request
     * @return
     */
    BaseResponse<Boolean> deleteInterfaceInfo(DeleteRequest deleteRequest, HttpServletRequest request);

    /**
     * 分页获取接口信息列表
     * @param interfaceInfoQueryRequest
     * @param interfaceInfoPage
     * @return
     */
    BaseResponse<List<InterfaceInfo>> listInterfaceInfoByPage(Page<InterfaceInfo> interfaceInfoPage, InterfaceInfoQueryRequest interfaceInfoQueryRequest);
}
