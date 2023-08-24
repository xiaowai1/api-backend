package com.cyj.apibackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyj.apibackend.common.BaseResponse;
import com.cyj.apibackend.common.DeleteRequest;
import com.cyj.apibackend.common.ErrorCode;
import com.cyj.apibackend.common.ResultUtils;
import com.cyj.apibackend.constant.CommonConstant;
import com.cyj.apibackend.exception.BusinessException;
import com.cyj.apibackend.model.dto.interfaceInfo.InterfaceInfoAddRequest;
import com.cyj.apibackend.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.cyj.apibackend.model.dto.interfaceInfo.InterfaceInfoUpdateRequest;
import com.cyj.apibackend.model.entity.InterfaceInfo;
import com.cyj.apibackend.model.entity.User;
import com.cyj.apibackend.service.InterfaceInfoService;
import com.cyj.apibackend.mapper.InterfaceInfoMapper;
import com.cyj.apibackend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 *
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService{

    @Resource
    private UserService userService;

    /**
     * 添加接口
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @Override
    public BaseResponse addInterfaceInfo(InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtil.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        //校验接口
        validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = this.save(interfaceInfo);
        if(!result){
            log.error("数据库插入接口信息失败");
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(interfaceInfo.getId());
    }

    /**
     * 更新接口 (仅本人和管理员可用)
     * @param interfaceInfoUpdateRequest
     * @param request
     * @return
     */
    @Override
    public BaseResponse updateInterfaceInfo(InterfaceInfoUpdateRequest interfaceInfoUpdateRequest, HttpServletRequest request) {
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtil.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        //校验接口信息
        validInterfaceInfo(interfaceInfo, false);
        User loginUser = userService.getLoginUser(request);
        InterfaceInfo oldInterfaceInfo = this.getById(interfaceInfoUpdateRequest.getId());
        //判断该接口是否存在
        if (Objects.isNull(oldInterfaceInfo)){
            log.error("该接口信息不存在");
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR);
        }
        //只有本人和管理员可以修改接口信息
        if (!oldInterfaceInfo.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)){
            log.error("无权限");
            return ResultUtils.error(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = this.updateById(interfaceInfo);
        if(!result){
            log.error("数据库更新接口信息失败");
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(interfaceInfo.getId());
    }

    /**
     * 删除接口 (仅本人和管理员可用)
     * @param deleteRequest
     * @param request
     * @return
     */
    @Override
    @Transactional
    public BaseResponse<Boolean> deleteInterfaceInfo(DeleteRequest deleteRequest, HttpServletRequest request) {
        Long id = deleteRequest.getId();
        InterfaceInfo interfaceInfo = this.getById(id);
        //判断接口是否存在
        if (Objects.isNull(interfaceInfo)){
            log.error("该接口信息不存在");
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        //只有本人和管理员可删除
        if (!interfaceInfo.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)){
            log.error("无权限");
            return ResultUtils.error(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = this.removeById(id);
        if(!result){
            log.error("数据库删除接口信息失败");
            return ResultUtils.error(ErrorCode.OPERATION_ERROR);
        }
        return ResultUtils.success(result);
    }

    /**
     * 分页获取接口信息列表
     * @param interfaceInfoQueryRequest
     * @return
     */
    @Override
    public BaseResponse<List<InterfaceInfo>> listInterfaceInfoByPage(Page<InterfaceInfo> interfaceInfoPage, InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtil.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        // description 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (interfaceInfoPage.getSize() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
//        Page<InterfaceInfo> interfaceInfoPage = this.page(new Page<>(current, size), queryWrapper);
        IPage<InterfaceInfo> pageModel = baseMapper.selectPage(interfaceInfoPage, queryWrapper);
        List<InterfaceInfo> interfaceInfoList = pageModel.getRecords();
        return ResultUtils.success(interfaceInfoList);
    }



    /**
     * 校验 添加/修改 接口信息
     * @param interfaceInfo
     * @param add   true-添加；false-修改接口
     */
    private void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        String name = interfaceInfo.getName();
        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称过长");
        }
    }
}




