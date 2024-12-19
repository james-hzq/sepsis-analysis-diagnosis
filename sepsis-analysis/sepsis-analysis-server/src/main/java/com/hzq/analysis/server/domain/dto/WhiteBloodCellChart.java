package com.hzq.analysis.server.domain.dto;

import com.hzq.analysis.server.domain.projection.WhiteBloodCellChartProjection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.function.DoubleSupplier;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.dto WhiteBloodCellChart
 * @date 2024/12/19 19:31
 * @description 白细胞计数查询对象
 */
@Data
@ToString
@EqualsAndHashCode
public class WhiteBloodCellChart {
    private Double lympCountMin;
    private Double monocytesMin;
    private Double eosinophilsMin;
    private Double basophilsMin;
    private Double neutrophilsMin;
    private Double lympCountMax;
    private Double monocytesMax;
    private Double eosinophilsMax;
    private Double basophilsMax;
    private Double neutrophilsMax;

    public WhiteBloodCellChart() {

    }

    public WhiteBloodCellChart(WhiteBloodCellChartProjection projection) {
        this.lympCountMin = projection.getLympCountMin();
        this.monocytesMin = projection.getMonocytesMin();
        this.eosinophilsMin = projection.getEosinophilsMin();
        this.basophilsMin = projection.getBasophilsMin();
        this.neutrophilsMin = projection.getNeutrophilsMin();
        this.lympCountMax = projection.getLympCountMax();
        this.monocytesMax = projection.getMonocytesMax();
        this.eosinophilsMax = projection.getEosinophilsMax();
        this.basophilsMax = projection.getBasophilsMax();
        this.neutrophilsMax = projection.getNeutrophilsMax();
    }

    private final DoubleSupplier[] valueSuppliers = new DoubleSupplier[] {
            this::getLympCountMin,
            this::getMonocytesMin,
            this::getEosinophilsMin,
            this::getBasophilsMin,
            this::getNeutrophilsMin,
            this::getLympCountMax,
            this::getMonocytesMax,
            this::getEosinophilsMax,
            this::getBasophilsMax,
            this::getNeutrophilsMax
    };

    public double getValue(int num) {
        if (num >= 0 && num < valueSuppliers.length) return valueSuppliers[num].getAsDouble();
        else throw new IllegalArgumentException("Invalid num value");
    }
}
