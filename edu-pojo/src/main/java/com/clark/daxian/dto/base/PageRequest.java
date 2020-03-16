package com.clark.daxian.dto.base;

import lombok.Data;

/**
 * 分页请求实体
 * @author clark
 */
@Data
public class PageRequest {
    /**
     * 那条开始
     */
    private Integer page;
    /**
     * 查询多少条
     */
    private Integer pageSize;

}

