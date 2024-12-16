package com.hzq.analysis.server.controller;

import com.hzq.analysis.server.domain.entity.TbPatientInfo;
import com.hzq.analysis.server.domain.vo.PatientLocationVO;
import com.hzq.analysis.server.domain.vo.PatientReportVO;
import com.hzq.analysis.server.service.AnalysisReportService;
import com.hzq.core.result.Result;
import com.hzq.security.annotation.RequiresPermissions;
import com.hzq.web.util.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hua
 * @className com.hzq.analysis.server.controller AnalysisReportController
 * @date 2024/12/16 12:24
 * @description 统计分析——填报概括请求处理器
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/analysis/report")
public class AnalysisReportController {

    private final AnalysisReportService analysisReportService;

    @GetMapping("/summarize")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<PatientReportVO> getPatientReport() {
        return Result.success(analysisReportService.getPatientReport());
    }

    @GetMapping("/list")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<Page<TbPatientInfo>> patientList() {
        Pageable pageable = PageUtils.buildPageRequest();
        return Result.success(analysisReportService.patientInfoList(pageable));
    }

    @GetMapping("/location")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<List<PatientLocationVO>> getPatientLocation() {
        return Result.success(analysisReportService.getPatientLocations());
    }
}
