package com.hzq.analysis.server.domain.projection;

/**
 * @author hua
 * @interfaceName com.hzq.analysis.server.domain.projection PatientLocationProjection
 * @date 2024/12/16 21:07
 * @description 患者入院地点展示
 */
public interface PatientLocationProjection {
    String getName();
    Integer getPatientNum();
    Integer getSepsisNum();
}
