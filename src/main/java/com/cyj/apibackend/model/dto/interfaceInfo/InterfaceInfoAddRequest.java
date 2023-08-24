package com.cyj.apibackend.model.dto.interfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 * 用于接收前端用户输入的请求参数
 * @TableName interface_info
 */
@Data
public class InterfaceInfoAddRequest implements Serializable {
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
     * 请求类型
     */
    private String method;

    /**
     * 请求参数
     */
    private String requestParams;

    /**
     * 请求头
     */
    private String requestHeader;

    /**
     * 响应头
     */
    private String responseHeader;

    private static final long serialVersionUID = 1L;
}
