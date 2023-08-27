package com.cyj.apibackend.service;

import com.cyj.apibackend.common.BaseResponse;
import com.cyj.apibackend.common.DeleteRequest;
import com.cyj.apibackend.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import com.cyj.apibackend.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.cyj.apibackend.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cyj.apicommon.model.entity.UserInterfaceInfo;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 添加用户接口信息
     * @param addRequest
     * @return
     */
    BaseResponse addUserInterfaceInfo(UserInterfaceInfoAddRequest addRequest);

    /**
     * 删除用户接口信息
     * @param deleteRequest
     * @param request
     * @return
     */
    BaseResponse deleteUserInterfaceInfo(DeleteRequest deleteRequest, HttpServletRequest request);

    /**
     * 更新用户接口信息
     * @param userInterfaceInfoUpdateRequest
     * @param request
     * @return
     */
    BaseResponse updateUserInterfaceInfo(UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest, HttpServletRequest request);


    /**
     * 获取用户接口信息列表
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    BaseResponse listUserInterfaceInfo(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);

    /**
     * 分页查询用户接口信息
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    BaseResponse listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);


    /**
     * 接口调用统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    boolean invokeCount(long interfaceInfoId, long userId);
}
