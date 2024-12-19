package com.hzq.analysis.server.domain.dto;

import com.hzq.analysis.server.domain.projection.AgeChartProjection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.vo AgeChartVO
 * @date 2024/12/17 10:37
 * @description 患者年龄查询对象
 */
@Data
@ToString
@EqualsAndHashCode
public class AgeChart {
    private Integer age;
    private Integer cnt;

    public AgeChart() {

    }

    public AgeChart(AgeChartProjection projection) {
        this.age = projection.getAge();
        this.cnt = projection.getCnt();
    }
}
