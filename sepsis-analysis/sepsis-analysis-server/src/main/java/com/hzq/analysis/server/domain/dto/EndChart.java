package com.hzq.analysis.server.domain.dto;

import com.hzq.analysis.server.domain.projection.EndChartProjection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.dto EndChart
 * @date 2024/12/19 13:09
 * @description 患者结局查询对象
 */
@Data
@ToString
@EqualsAndHashCode
public class EndChart {
    private Integer survivalNum;
    private Integer diedNum;

    public EndChart() {

    }

    public EndChart(EndChartProjection projection) {
        this.survivalNum = projection.getSurvivalNum();
        this.diedNum = projection.getDiedNum();
    }

    public Integer getValue(String fieldName) {
        return switch (fieldName) {
            case "存活人数" -> survivalNum;
            case "死亡人数" -> diedNum;
            default -> 0;
        };
    }
}
