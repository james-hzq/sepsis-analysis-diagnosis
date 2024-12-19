package com.hzq.analysis.server.domain.vo;

import com.hzq.analysis.server.domain.projection.HeightAndWeightChartProjection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.vo HeightAndWeightChartVO
 * @date 2024/12/18 20:23
 * @description 患者身高体重图表查询对象
 */
@Data
@ToString
@EqualsAndHashCode
public class HeightAndWeightChartVO {
    private Double w1;
    private Double w2;
    private Double w3;
    private Double w4;
    private Double h1;
    private Double h2;
    private Double h3;
    private Double h4;
    private Double mAvgWeight;
    private Double fAvgWeight;
    private Double mAvgHeight;
    private Double fAvgHeight;

    public HeightAndWeightChartVO() {

    }

    public HeightAndWeightChartVO(HeightAndWeightChartProjection projection) {
        this.w1 = projection.getW1();
        this.w2 = projection.getW2();
        this.w3 = projection.getW3();
        this.w4 = projection.getW4();
        this.h1 = projection.getH1();
        this.h2 = projection.getH2();
        this.h3 = projection.getH3();
        this.h4 = projection.getH4();
        this.mAvgWeight = projection.getMaleAvgWeight();
        this.fAvgWeight = projection.getFemaleAvgWeight();
        this.mAvgHeight = projection.getMaleAvgHeight();
        this.fAvgHeight = projection.getFemaleAvgHeight();
    }
}
