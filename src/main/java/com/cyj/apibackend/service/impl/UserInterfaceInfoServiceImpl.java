package com.cyj.apibackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyj.apibackend.common.BaseResponse;
import com.cyj.apibackend.common.DeleteRequest;
import com.cyj.apibackend.common.ErrorCode;
import com.cyj.apibackend.common.ResultUtils;
import com.cyj.apibackend.constant.CommonConstant;
import com.cyj.apibackend.exception.BusinessException;
import com.cyj.apibackend.model.dto.userinterfaceinfo.UserInterfaceInfoAddRequest;
import com.cyj.apibackend.model.dto.userinterfaceinfo.UserInterfaceInfoQueryRequest;
import com.cyj.apibackend.model.dto.userinterfaceinfo.UserInterfaceInfoUpdateRequest;
import com.cyj.apibackend.service.UserInterfaceInfoService;
import com.cyj.apibackend.mapper.UserInterfaceInfoMapper;
import com.cyj.apibackend.service.UserService;
import com.cyj.apicommon.model.entity.UserInterfaceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 *
 */
@Slf4j
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Resource
    private UserService userService;

    /**
     * 添加用户接口信息
     * @param userInterfaceInfoAddRequest
     * @return
     */
    @Override
    public BaseResponse addUserInterfaceInfo(UserInterfaceInfoAddRequest userInterfaceInfoAddRequest) {
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtil.copyProperties(userInterfaceInfoAddRequest, userInterfaceInfo);
        validUserInterfaceInfo(userInterfaceInfo, true);
//        User loginUser = userService.getLoginUser(request);
        userInterfaceInfo.setUserId(userInterfaceInfoAddRequest.getUserId());
        boolean result = this.save(userInterfaceInfo);
        if (!result){
            log.error("用户接口信息插入失败");
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(userInterfaceInfo.getId());
    }

    /**
     * 删除用户接口信息
     * @param deleteRequest
     * @param request
     * @return
     */
    @Override
    public BaseResponse deleteUserInterfaceInfo(DeleteRequest deleteRequest, HttpServletRequest request) {
//        User loginUser = userService.getLoginUser(request);
        Long id = deleteRequest.getId();
        UserInterfaceInfo userInterfaceInfo = this.getById(id);
        if (Objects.isNull(userInterfaceInfo)){
            log.error("删除失败，该用户接口信息不存在");
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean result = this.removeById(id);
        if (!result){
            log.error("删除失败，数据库错误");
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(result);
    }

    /**
     * 更新用户接口信息
     * @param userInterfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @Override
    public BaseResponse updateUserInterfaceInfo(UserInterfaceInfoUpdateRequest userInterfaceInfoUpdateRequest, HttpServletRequest request) {
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtil.copyProperties(userInterfaceInfoUpdateRequest, userInterfaceInfo);
        validUserInterfaceInfo(userInterfaceInfo, false);
        long id = userInterfaceInfoUpdateRequest.getId();
        UserInterfaceInfo oldInterfaceInfo = this.getById(id);
        if (Objects.isNull(oldInterfaceInfo)){
            log.error("更新失败，该用户接口信息不存在");
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR);
        }
        boolean result = this.updateById(userInterfaceInfo);
        if (!result){
            log.error("更新失败，数据库错误");
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(result);
    }

    /**
     * 获取用户接口信息列表
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @Override
    public BaseResponse listUserInterfaceInfo(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtil.copyProperties(userInterfaceInfoQueryRequest, userInterfaceInfo);
        LambdaQueryWrapper<UserInterfaceInfo> queryWrapper = new LambdaQueryWrapper<>(userInterfaceInfo);
        List<UserInterfaceInfo> userInterfaceInfoList = this.list(queryWrapper);
        return ResultUtils.success(userInterfaceInfoList);
    }

    /**
     * 分页查询用户接口信息
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @Override
    public BaseResponse listUserInterfaceInfoByPage(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        long current = userInterfaceInfoQueryRequest.getCurrent();
        long pageSize = userInterfaceInfoQueryRequest.getPageSize();
        UserInterfaceInfo userInterfaceInfo = new UserInterfaceInfo();
        BeanUtil.copyProperties(userInterfaceInfoQueryRequest, userInterfaceInfo);
        String sortField = userInterfaceInfoQueryRequest.getSortField();
        String sortOrder = userInterfaceInfoQueryRequest.getSortOrder();
        //限制爬虫
        if (pageSize > 50){
            log.error("分页查询失败");
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper<>(userInterfaceInfo);
        queryWrapper.orderBy(StrUtil.isNotBlank(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<UserInterfaceInfo> userInterfaceInfoPage = this.page(new Page<>(current, pageSize), queryWrapper);
        return ResultUtils.success(userInterfaceInfoPage);
    }

    /**
     * 接口调用统计
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {
        if (interfaceInfoId <= 0 || userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LambdaUpdateWrapper<UserInterfaceInfo> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId);
        updateWrapper.eq(UserInterfaceInfo::getUserId, userId);
        updateWrapper.gt(UserInterfaceInfo::getLeftNum, 0);
        updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        return update(updateWrapper);
    }


    private void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add){
        if (Objects.isNull(userInterfaceInfo)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (add) {
            if (userInterfaceInfo.getInterfaceInfoId() <= 0 || userInterfaceInfo.getUserId() <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或用户不存在");
            }
        }
        if (userInterfaceInfo.getLeftNum() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能小于0");
        }
    }
}




