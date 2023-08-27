package com.cyj.apibackend.controller;

import com.cyj.apibackend.annotation.AuthCheck;
import com.cyj.apibackend.common.BaseResponse;
import com.cyj.apibackend.common.DeleteRequest;
import com.cyj.apibackend.common.ErrorCode;
import com.cyj.apibackend.common.ResultUtils;
import com.cyj.apibackend.constant.UserConstant;
import com.cyj.apibackend.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import com.cyj.apibackend.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.cyj.apibackend.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import com.cyj.apibackend.service.UserInterfaceInfoService;
import com.cyj.apicommon.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @ClassName UserInterfaceInfoController
 * @Description 用户接口信息接口
 * @Author chixiaowai
 * @Date 2023/8/25 11:05
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("userInterfaceInfo")
public class UserInterfaceInfoController {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 添加用户接口信息 (仅管理员可用)
     * @param addRequest
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse addUserInterfaceInfo(@RequestBody UserInterfaceInfoAddRequest addRequest){
        if (Objects.isNull(addRequest)){
            log.error("add UserInterfaceInfo failed, params error");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userInterfaceInfoService.addUserInterfaceInfo(addRequest);
    }

    /**
     * 删除用户接口信息
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse deleteUserInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request){
        if (Objects.isNull(deleteRequest) || deleteRequest.getId() <= 0){
            log.error("delete UserInterfaceInfo failed, params error");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userInterfaceInfoService.deleteUserInterfaceInfo(deleteRequest, request);
    }

    /**
     * 更新用户接口信息
     * @param userInterfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse updateUserInterfaceInfo(@RequestBody UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest, HttpServletRequest request){
        if (Objects.isNull(userInterfaceInfoUpdateRequest) || userInterfaceInfoUpdateRequest.getId() <= 0){
            log.error("upate UserInterfaceInfo failed, params error");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userInterfaceInfoService.updateUserInterfaceInfo(userInterfaceInfoUpdateRequest, request);
    }

    /**
     * 根据ID获取用户接口信息
     * @param id
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse getUserInterfaceInfoById(long id){
        if (id <= 0){
            log.error("get UserInterfaceInfo failed, params error");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById(id);
        return ResultUtils.success(userInterfaceInfo);
    }

    /**
     * 获取用户接口信息列表
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse listUserInterfaceInfo(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest){
        if (Objects.isNull(userInterfaceInfoQueryRequest)){
            log.error("list UserInterfaceInfo failed, params error");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userInterfaceInfoService.listUserInterfaceInfo(userInterfaceInfoQueryRequest);
    }

    /**
     * 分页查询用户接口信息
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest){
        if (Objects.isNull(userInterfaceInfoQueryRequest)){
            log.error("list by page UserInterfaceInfo failed, params error");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return userInterfaceInfoService.listUserInterfaceInfoByPage(userInterfaceInfoQueryRequest);
    }

}
