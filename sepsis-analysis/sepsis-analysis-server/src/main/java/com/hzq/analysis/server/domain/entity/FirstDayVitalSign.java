package com.hzq.analysis.server.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.entity FirstDayVitalSign
 * @date 2024/12/13 21:19
 * @description 入ICU第一天的生命体征实体类
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "first_day_vital_sign")
public class FirstDayVitalSign extends BaseEntity {

    @Column(name = "heart_min", columnDefinition = "double comment '最低心率(次/分)'")
    private double heartMin;

    @Column(name = "heart_max", columnDefinition = "double comment '最高心率(次/分)'")
    private double heartMax;

    @Column(name = "heart", columnDefinition = "double comment '平均心率(次/分)'")
    private double heart;

    @Column(name = "sbp_min", columnDefinition = "double comment '最低收缩压(mmHg)'")
    private double sbpMin;

    @Column(name = "sbp_max", columnDefinition = "double comment '最高收缩压(mmHg)'")
    private double sbpMax;

    @Column(name = "sbp", columnDefinition = "double comment '平均收缩压(mmHg)'")
    private double sbp;

    @Column(name = "dbp_min", columnDefinition = "double comment '最低舒张压(mmHg)'")
    private double dbpMin;

    @Column(name = "dbp_max", columnDefinition = "double comment '最高舒张压(mmHg)'")
    private double dbpMax;

    @Column(name = "dbp", columnDefinition = "double comment '平均舒张压(mmHg)'")
    private double dbp;

    @Column(name = "mbp_min", columnDefinition = "double comment '最低动脉压(mmHg)'")
    private double mbpMin;

    @Column(name = "mbp_max", columnDefinition = "double comment '最高动脉压(mmHg)'")
    private double mbpMax;

    @Column(name = "mbp", columnDefinition = "double comment '平均动脉压(mmHg)'")
    private double mbp;

    @Column(name = "breath_min", columnDefinition = "double comment '最低呼吸次数(次/分)'")
    private double breathMin;

    @Column(name = "breath_max", columnDefinition = "double comment '最高呼吸次数(次/分)'")
    private double breathMax;

    @Column(name = "breath", columnDefinition = "double comment '平均呼吸次数(次/分)'")
    private double breath;

    @Column(name = "spo2_min", columnDefinition = "double comment '最低血氧饱和度(%)'")
    private double spo2Min;

    @Column(name = "spo2_max", columnDefinition = "double comment '最高血氧饱和度(%)'")
    private double spo2Max;

    @Column(name = "spo2", columnDefinition = "double comment '平均血氧饱和度(%)'")
    private double spo2;

    @Column(name = "tep_min", columnDefinition = "double comment '最低体温(℃)'")
    private double tepMin;

    @Column(name = "tep_max", columnDefinition = "double comment '最高体温(℃)'")
    private double tepMax;

    @Column(name = "tep", columnDefinition = "double comment '平均体温(℃)'")
    private double tep;
}
