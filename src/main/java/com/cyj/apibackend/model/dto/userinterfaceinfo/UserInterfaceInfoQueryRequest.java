package com.cyj.apibackend.model.dto.userinterfaceinfo;

import com.cyj.apibackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 * 用于接收前端用户输入的请求参数
 * @author yupi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInterfaceInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

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

    /**
     * 状态：0-正常，1-禁用
     */
    private Integer status;

    private static final long serialVersionUID = 1L;
}
