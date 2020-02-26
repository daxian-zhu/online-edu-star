package com.clark.daxian.auth.api.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * token存储实体
 * @author 大仙
 */
@Data
public class TokenEntity implements Serializable {
    /**
     * 唯一标识
     */
    private String id;
    /**
     * token
     */
    private String token;
    /**
     * 失效事件
     */
    private LocalDateTime invalidDate;
    /**
     * 失效 1 有效  0 无效
     */
    private Integer status = 1;
}
