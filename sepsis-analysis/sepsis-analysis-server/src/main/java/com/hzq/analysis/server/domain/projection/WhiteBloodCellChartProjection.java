package com.hzq.analysis.server.domain.projection;

/**
 * @author hua
 * @interfaceName com.hzq.analysis.server.domain.projection WhiteBloodCellChartProjection
 * @date 2024/12/19 19:28
 * @description 白细胞计数查询对象
 */
public interface WhiteBloodCellChartProjection {
    Double getLympCountMin();
    Double getMonocytesMin();
    Double getEosinophilsMin();
    Double getBasophilsMin();
    Double getNeutrophilsMin();
    Double getLympCountMax();
    Double getMonocytesMax();
    Double getEosinophilsMax();
    Double getBasophilsMax();
    Double getNeutrophilsMax();
}
