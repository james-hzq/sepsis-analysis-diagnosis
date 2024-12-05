package com.hzq.web.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author hua
 * @className com.hzq.common.dao BaseEntity
 * @date 2024/8/28 21:59
 * @description 实体类的公共字段
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
    private Date createTime;

    /**
     * 更新者
     */
    @Column(name="update_by", length = 30, nullable = false, columnDefinition="varchar(30) comment '更新者'")
    private String updateBy;

    /**
     * 更新时间
     */
    @Column(name="update_time", nullable = false, columnDefinition="DATETIME comment '更新时间'")
    private Date updateTime;

    public void create(String currUsername) {
        Date date = new Date();
        this.createTime = date;
        this.updateTime = date;
        this.createBy = this.updateBy = currUsername;
    }

    public void update(String currUsername) {
        this.updateTime = new Date();
        this.updateBy = currUsername;
    }
}
