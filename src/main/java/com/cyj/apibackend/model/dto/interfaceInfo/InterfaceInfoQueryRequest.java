package com.cyj.apibackend.model.dto.interfaceInfo;

import com.cyj.apibackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 * 用于接收前端用户输入的请求参数
 * @author chixiaowai
 * @TableName interface_info
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * ID
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口信息描述
     */
    private String description;

    /**
     * 接口地址
     */
    private String url;

    /**
     * 创建人
     */
    private Long userId;

    /**
     * 请求类型
     */
    private String method;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    /**
     * 接口状态(0 - 关闭，1 - 开启)
     */
    private Integer status;

    /**
     * 是否删除(0 - 未删除， 1- 已删除)
     */
    private Byte isDeleted;

    private static final long serialVersionUID = 1L;
}
