package com.hzq.analysis.server.domain.projection;

/**
 * @author hua
 * @interfaceName com.hzq.analysis.server.domain.projection ScoreChartProjection
 * @date 2024/12/19 13:39
 * @description 患者入ICU第一天得分情况查询对象
 */
public interface ScoreChartProjection {
    Integer getScore();
    Integer getTotal();
}
