package com.hzq.analysis.server.domain.vo;

import com.hzq.analysis.server.domain.projection.PatientLocationProjection;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.vo PatientLocationVO
 * @date 2024/12/16 21:09
 * @description 患者入院地点展示
 */
@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
public class PatientLocationVO {
    private String name;
    private Integer patientNum;
    private Integer sepsisNum;

    public PatientLocationVO() {

    }

    public PatientLocationVO(PatientLocationProjection projection) {
        this.name = projection.getName();
        this.patientNum = projection.getPatientNum();
        this.sepsisNum = projection.getSepsisNum();
    }
}
