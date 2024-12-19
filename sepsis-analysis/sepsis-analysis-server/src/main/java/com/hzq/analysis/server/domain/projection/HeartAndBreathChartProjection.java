package com.hzq.analysis.server.domain.projection;

/**
 * @author hua
 * @interfaceName com.hzq.analysis.server.domain.projection HeartAndBreathChartProjection
 * @date 2024/12/19 10:13
 * @description 心率和呼吸图表查询对象
 */
public interface HeartAndBreathChartProjection {
    Integer getHeartMin();
    Integer getHeart();
    Integer getHeartMax();
    Integer getBreathMin();
    Integer getBreath();
    Integer getBreathMax();
}
