package com.hzq.analysis.server.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.entity FirstDayScore
 * @date 2024/12/13 21:12
 * @description 入ICU第一天得分实体类
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "first_day_score")
public class FirstDayScore extends BaseEntity {

    @Column(name = "sofa", columnDefinition = "INT comment '多器官功能障碍评分'")
    private Integer sofa;

    @Column(name = "gcs_motor", columnDefinition = "INT comment 'gcs_motor运动反应'")
    private Integer gcsMotor;

    @Column(name = "gcs_verbal", columnDefinition = "INT comment 'gcs_verbal语言反应'")
    private Integer gcsVerbal;

    @Column(name = "gcs_eyes", columnDefinition = "INT comment 'gcs_eyes眼睛反应'")
    private Integer gcsEyes;
}
