package com.hzq.analysis.server.domain.dto;

import com.hzq.analysis.server.domain.projection.HeartAvgChartProjection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.dto HeartAvgChart
 * @date 2024/12/19 10:30
 * @description 心率平均值查询对象
 */
@Data
@ToString
@EqualsAndHashCode
public class HeartAvgChart {
    private Integer name;
    private Integer value;

    public HeartAvgChart() {

    }

    public HeartAvgChart(HeartAvgChartProjection projection) {
        this.name = projection.getName();
        this.value = projection.getValue();
    }
}
