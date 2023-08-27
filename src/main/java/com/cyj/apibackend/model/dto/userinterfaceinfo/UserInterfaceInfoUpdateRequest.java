package com.cyj.apibackend.model.dto.userinterfaceinfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 更新请求
 * 用于接收前端用户输入的请求参数
 * @TableName user_interface_info
 */
@Data
public class UserInterfaceInfoUpdateRequest implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 总调用次数
     */
    private Integer totalNum;

    /**
     * 剩余调用此时
     */
    private Integer leftNum;

    /**
     * 状态：0-正常，1-禁用
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
