package com.hzq.analysis.server.domain.projection;

/**
 * @author hua
 * @interfaceName com.hzq.analysis.server.domain.projection UrineChartProjection
 * @date 2024/12/19 18:43
 * @description 患者入ICU第一天尿量查询对象
 */
public interface UrineChartProjection {
    Integer getUrineOutput();
    Integer getTotal();
}
