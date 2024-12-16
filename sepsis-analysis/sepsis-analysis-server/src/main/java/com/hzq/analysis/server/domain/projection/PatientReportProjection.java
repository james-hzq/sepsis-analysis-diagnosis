package com.hzq.analysis.server.domain.projection;

/**
 * @author hua
 * @interfaceName com.hzq.analysis.server.domain.projection PatientReportProjection
 * @date 2024/12/16 12:12
 * @description 患者填报概括展示
 */
public interface PatientReportProjection {
    Integer getPatientNum();
    Integer getSepsisNum();
    Double getSepsisPro();
}
