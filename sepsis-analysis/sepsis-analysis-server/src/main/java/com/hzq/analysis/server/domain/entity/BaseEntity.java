package com.hzq.analysis.server.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.entity BaseEntity
 * @date 2024/12/13 19:04
 * @description 数据分析模块公共实体类字段
 */
@MappedSuperclass
@Data
@ToString
@EqualsAndHashCode
public class BaseEntity {

    /**
     * 自增无符号主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id", columnDefinition = "INT UNSIGNED comment '无意义,自增主键'")
    private Integer id;

    /**
     * 患者入院ID
     */
    @Column(name ="patient_id", length = 30, columnDefinition = "varchar(30) comment '患者入院 ID'")
    private String patientId;

    /**
     * 患者出院ID
     */
    @Column(name ="icu_id", length = 30, columnDefinition = "varchar(30) comment '患者入ICU ID'")
    private String icuId;

    /**
     * 患者是否为脓毒症（0：否，1：是）
     */
    @Column(name ="is_sepsis", columnDefinition = "char(1) comment '是否为脓毒症(0: 否, 1: 是)'")
    private Character isSepsis;

    /**
     * 患者入ICU时间
     */
    @Column(name ="in_time", columnDefinition = "DATETIME comment '入ICU时间'")
    private LocalDateTime inTime;

    /**
     * 患者出ICU时间
     */
    @Column(name ="out_time", columnDefinition = "DATETIME comment '出ICU时间'")
    private LocalDateTime outTime;
}
