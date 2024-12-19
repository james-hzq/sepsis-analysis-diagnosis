package com.hzq.analysis.server.service;

import com.hzq.analysis.server.dao.TbPatientInfoDao;
import com.hzq.analysis.server.domain.entity.TbPatientInfo;
import com.hzq.analysis.server.domain.vo.PatientLocationVO;
import com.hzq.analysis.server.domain.vo.PatientReportVO;
import com.hzq.core.result.ResultEnum;
import com.hzq.web.exception.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hua
 * @className com.hzq.analysis.server.service AnalysisReportService
 * @date 2024/12/16 12:25
 * @description 统计分析——填报概括业务处理类
 */
@RequiredArgsConstructor
@Service
public class AnalysisReportService {

    private final TbPatientInfoDao tbPatientInfoDao;

    public PatientReportVO getPatientReport() {
        return tbPatientInfoDao.findPatientReport()
                .map(PatientReportVO::new)
                .orElseThrow(() -> new SystemException(ResultEnum.UNKNOWN_ERROR));
    }

    public Page<TbPatientInfo> patientInfoList(Pageable pageable) {
        return tbPatientInfoDao.findAll(pageable);
    }

    public List<PatientLocationVO> getPatientLocations() {
        return tbPatientInfoDao.findPatientLocation()
                .orElse(new ArrayList<>())
                .stream()
                .map(PatientLocationVO::new)
                .toList();
    }
}
