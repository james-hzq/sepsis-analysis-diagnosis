package com.hzq.analysis.server.domain.projection;

/**
 * @author hua
 * @interfaceName com.hzq.analysis.server.domain.projection HeightAndWeightChartProjection
 * @date 2024/12/18 9:20
 * @description 患者身高体重图表查询对象
 */
public interface HeightAndWeightChartProjection {
    Double getW1();
    Double getW2();
    Double getW3();
    Double getW4();
    Double getH1();
    Double getH2();
    Double getH3();
    Double getH4();
    Double getMaleAvgWeight();
    Double getFemaleAvgWeight();
    Double getMaleAvgHeight();
    Double getFemaleAvgHeight();
}
