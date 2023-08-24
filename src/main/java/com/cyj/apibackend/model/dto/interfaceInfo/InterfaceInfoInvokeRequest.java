package com.cyj.apibackend.model.dto.interfaceInfo;

import lombok.Data;

/**
 * @ClassName InterfaceInfoInvokeRequest
 * @Description TODO
 * @Author chixiaowai
 * @Date 2023/8/15 13:55
 * @Version 1.0
 */
@Data
public class InterfaceInfoInvokeRequest {

    /**
     * ID
     */
    private Long id;

    /**
     * 用户请求参数
     */
    private String userRequestParams;

    private static final long serialVersionUID = 1L;
}
