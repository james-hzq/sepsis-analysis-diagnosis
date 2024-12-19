package com.hzq.analysis.server.domain.projection;

/**
 * @author hua
 * @interfaceName com.hzq.analysis.server.domain.projection EndChartProjection
 * @date 2024/12/19 12:58
 * @description 患者结局查询对象
 */
public interface EndChartProjection {
    Integer getSurvivalNum();
    Integer getDiedNum();
}
