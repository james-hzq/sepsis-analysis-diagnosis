package com.hzq.analysis.server.domain.dto;

import com.hzq.analysis.server.domain.projection.UrineChartProjection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.dto UrineChart
 * @date 2024/12/19 18:44
 * @description 患者入ICU第一天尿量查询对象
 */
@Data
@ToString
@EqualsAndHashCode
public class UrineChart {
    private Integer urineOutput;
    private Integer total;

    public UrineChart() {

    }

    public UrineChart(UrineChartProjection projection) {
        this.urineOutput = projection.getUrineOutput();
        this.total = projection.getTotal();
    }
}
