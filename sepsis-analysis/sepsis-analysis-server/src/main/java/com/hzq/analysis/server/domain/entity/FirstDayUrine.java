package com.hzq.analysis.server.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.entity FirstDayUrine
 * @date 2024/12/13 21:17
 * @description 入ICU第一天尿量实体类
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "first_day_urine")
public class FirstDayUrine extends BaseEntity {

    @Column(name = "urine_output", columnDefinition = "INT comment '尿量输出值(ml)'")
    private Integer urineOutput;
}
