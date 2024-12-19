package com.hzq.analysis.server.domain.dto;

import com.hzq.analysis.server.domain.projection.ScoreChartProjection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.dto ScoreChart
 * @date 2024/12/19 13:41
 * @description 患者入ICU第一天得分情况查询对象
 */
@Data
@ToString
@EqualsAndHashCode
public class ScoreChart {
    private Integer score;
    private Integer total;

    public ScoreChart() {

    }

    public ScoreChart(ScoreChartProjection projection) {
        this.score = projection.getScore();
        this.total = projection.getTotal();
    }
}
