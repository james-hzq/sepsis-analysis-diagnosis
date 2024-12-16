package com.hzq.analysis.server.domain.vo;

import com.hzq.analysis.server.domain.projection.PatientReportProjection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.projection PatientReportVO
 * @date 2024/12/16 12:09
 * @description 患者填报概括展示
 */
@Data
@ToString
@EqualsAndHashCode
public class PatientReportVO {
    private Integer patientNum;
    private Integer sepsisNum;
    private double sepsisPro;

    public PatientReportVO() {

    }

    public PatientReportVO(PatientReportProjection projection) {
        this.patientNum = projection.getPatientNum();
        this.sepsisNum = projection.getSepsisNum();
        this.sepsisPro = projection.getSepsisPro();
    }
}
