package com.cyj.apibackend.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cyj.apibackend.annotation.AuthCheck;
import com.cyj.apibackend.common.*;
import com.cyj.apibackend.constant.UserConstant;
import com.cyj.apibackend.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.cyj.apibackend.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.cyj.apibackend.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.cyj.apibackend.service.InterfaceInfoService;
import com.cyj.apicommon.model.entity.InterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName InterfaceInfoController
 * @Description 接口信息接口
 * @Author chixiaowai
 * @Date 2023/8/22 20:25
 * @Version 1.0
 */
@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 添加接口
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse addInterfaceInfo(InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request){
        if (Objects.isNull(interfaceInfoAddRequest)){
            log.error("add interfaceInfo failed, params error");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return interfaceInfoService.addInterfaceInfo(interfaceInfoAddRequest, request);
    }

    /**
     * 删除接口 (仅本人和管理员可用)
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(DeleteRequest deleteRequest, HttpServletRequest request){
        if (Objects.isNull(deleteRequest) || deleteRequest.getId() <= 0){
            log.error("delete interfaceInfo failed, params error");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return interfaceInfoService.deleteInterfaceInfo(deleteRequest, request);
    }


    /**
     * 更新接口 (仅本人和管理员可用)
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse addInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest, HttpServletRequest request){
        if (Objects.isNull(interfaceInfoUpdateRequest) || interfaceInfoUpdateRequest.getId() <= 0){
            log.error("update interfaceInfo failed, params error");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return interfaceInfoService.updateInterfaceInfo(interfaceInfoUpdateRequest, request);
    }

    /**
     * 根据接口id获取接口信息
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse getInterfaceInfoById(long id){
        if (id <= 0) {
            log.error("get interfaceInfo failed, params error");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 获取接口列表 (仅管理员可用)
     * @param interfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfo(InterfaceInfoQueryRequest interfaceInfoQueryRequest){
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        if (interfaceInfoQueryRequest != null) {
            BeanUtil.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        }
        LambdaQueryWrapper<InterfaceInfo> queryWrapper = new LambdaQueryWrapper<>(interfaceInfoQuery);
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list(queryWrapper);
        return ResultUtils.success(interfaceInfoList);
    }


    /**
     * 分页获取接口列表
     * @param interfaceInfoQueryRequest
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest){
        if (interfaceInfoQueryRequest == null) {
            log.error("pageList interfaceInfo failed, params error");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long current = interfaceInfoQueryRequest.getCurrent();
        long pageSize = interfaceInfoQueryRequest.getPageSize();
        Page<InterfaceInfo> interfaceInfoPage = new Page<>(current, pageSize);
        return interfaceInfoService.listInterfaceInfoByPage(interfaceInfoPage, interfaceInfoQueryRequest);
    }

    /**
     * 发布接口 （仅管理员可用）
     * @param idRequest
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse onlineInterfaceInfo(@RequestBody IdRequest idRequest){
        if (idRequest == null || idRequest.getId() <= 0) {
            log.error("online interfaceInfo failed, params error");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return interfaceInfoService.onlineInterfaceInfo(idRequest);
    }

    /**
     * 下线接口（仅管理员可用）
     * @param idRequest
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse offlineInterfaceInfo(@RequestBody IdRequest idRequest){
        if (idRequest == null || idRequest.getId() <= 0) {
            log.error("offline interfaceInfo failed, params error");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        return interfaceInfoService.offlineInterfaceInfo(idRequest);
    }
}
