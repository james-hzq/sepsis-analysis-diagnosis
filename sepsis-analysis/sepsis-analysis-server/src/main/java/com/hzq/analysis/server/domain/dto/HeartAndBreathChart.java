package com.hzq.analysis.server.domain.dto;

import com.hzq.analysis.server.domain.projection.HeartAndBreathChartProjection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.dto HeartAndBreathChart
 * @date 2024/12/19 10:25
 * @description 心率和呼吸图表查询对象
 */
@Data
@ToString
@EqualsAndHashCode
public class HeartAndBreathChart {
    private Integer heartMin;
    private Integer heart;
    private Integer heartMax;
    private Integer breathMin;
    private Integer breath;
    private Integer breathMax;

    public HeartAndBreathChart() {

    }

    public HeartAndBreathChart(HeartAndBreathChartProjection projection) {
        this.heartMin = projection.getHeartMin();
        this.heart = projection.getHeart();
        this.heartMax = projection.getHeartMax();
        this.breathMin = projection.getBreathMin();
        this.breath = projection.getBreath();
        this.breathMax = projection.getBreathMax();
    }

    public Integer getValue(String name) {
        return switch (name) {
            case "最小心率均值" -> getHeartMin();
            case "心率均值" -> getHeart();
            case "最大心率均值" -> getHeartMax();
            case "最小呼吸均值" -> getBreathMin();
            case "呼吸均值" -> getBreath();
            case "最大呼吸均值" -> getBreathMax();
            default -> 0;
        };
    }
}
