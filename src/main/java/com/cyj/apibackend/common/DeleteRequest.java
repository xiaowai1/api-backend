package com.cyj.apibackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *
 * @author chixiaowai
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
