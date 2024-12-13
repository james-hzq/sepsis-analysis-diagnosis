package com.hzq.system.server.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hua
 * @className com.hzq.system.server.domain.entity TbHospInfo
 * @date 2024/12/11 21:29
 * @description 医院信息实体类
 */
@Data
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "tb_hosp_info")
public class TbHospInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", columnDefinition="INT UNSIGNED comment '自增主键,医院序号'")
    private Integer id;

    @Column(name="hosp_name", columnDefinition="VARCHAR(100) comment '医院名称'")
    private String hospName;

    @Column(name="icu", columnDefinition="VARCHAR(100) comment 'ICU名称'")
    private String icu;

    @Column(name="icu_city", columnDefinition="VARCHAR(30) comment 'ICU所属城市'")
    private String icuCity;

    @Column(name="icu_dist", columnDefinition="VARCHAR(30) comment 'ICU所属地区'")
    private String icuDist;
}
