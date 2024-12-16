package com.hzq.system.server.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @author hua
 * @className com.hzq.common.dao BaseEntity
 * @date 2024/8/28 21:59
 * @description 系统管理模块公共实体类字段
 */
@MappedSuperclass
@Data
@ToString
@EqualsAndHashCode
public class BaseEntity {

    /**
     * 创建者
     */
    @Column(name="create_by", length = 30, nullable = false, columnDefinition="varchar(30) comment '创建者'")
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name="create_time", nullable = false, columnDefinition="DATETIME comment '创建时间'")
    private LocalDateTime createTime;

    /**
     * 更新者
     */
    @Column(name="update_by", length = 30, nullable = false, columnDefinition="varchar(30) comment '更新者'")
    private String updateBy;

    /**
     * 更新时间
     */
    @Column(name="update_time", nullable = false, columnDefinition="DATETIME comment '更新时间'")
    private LocalDateTime  updateTime;

    public void create(String currUsername) {
        LocalDateTime now = LocalDateTime.now();
        // 使用 DateTimeFormatter 转换
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.createTime = this.updateTime = LocalDateTime.parse(now.format(formatter), formatter);
        this.createBy = this.updateBy = currUsername;
    }

    public void update(String currUsername) {
        this.updateTime = LocalDateTime.now();
        this.updateBy = currUsername;
    }
}
