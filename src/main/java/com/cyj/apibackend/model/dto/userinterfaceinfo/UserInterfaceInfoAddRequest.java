package com.cyj.apibackend.model.dto.userinterfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 * 用于接收前端用户输入的请求参数
 * @TableName user_interface_info
 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 接口ID
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用此时
     */
    private Integer leftNum;


    private static final long serialVersionUID = 1L;
}
