package com.hzq.analysis.server.domain.projection;

/**
 * @author hua
 * @interfaceName com.hzq.analysis.server.domain.projection AgeChartProjection
 * @date 2024/12/17 10:39
 * @description 患者年龄查询对象
 */
public interface AgeChartProjection {
    Integer getAge();
    Integer getCnt();
}
