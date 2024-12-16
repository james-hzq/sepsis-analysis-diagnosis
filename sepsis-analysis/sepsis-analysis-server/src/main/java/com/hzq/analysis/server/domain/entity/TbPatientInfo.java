package com.hzq.analysis.server.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.entity TbPatientInfo
 * @date 2024/12/13 18:50
 * @description ICU病患信息实体类
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tb_patient_info")
public class TbPatientInfo extends BaseEntity {

    @Column(name ="gender", columnDefinition = "char(1) comment '性别（F：男性，M：女性）'")
    private Character gender;

    @Column(name = "age", columnDefinition = "tinyint comment '年龄'")
    private Byte age;

    @Column(name = "height", columnDefinition = "double comment '身高'")
    private Double height;

    @Column(name = "weight", columnDefinition = "double comment '体重'")
    private Double weight;

    @Column(name = "first_care_unit", length = 100, columnDefinition = "varchar(100) comment '首次护理单元'")
    private String firstCareUnit;

    @Column(name = "last_care_unit", length = 100, columnDefinition = "varchar(100) comment '末次护理单元'")
    private String lastCareUnit;

    @Column(name = "icu_location", length = 100, columnDefinition = "varchar(100) comment '入院地点'")
    private String icuLocation;

    @Column(name = "doctor_id", length = 30, columnDefinition = "varchar(30) comment '入院地点'")
    private String doctorId;

    @Column(name = "is_died", columnDefinition = "char(1) comment '是否死亡'")
    private Character isDied;
}
