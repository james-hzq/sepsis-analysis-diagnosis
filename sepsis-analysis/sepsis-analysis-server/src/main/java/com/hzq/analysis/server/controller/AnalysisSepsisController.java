package com.hzq.analysis.server.controller;

import com.google.common.base.Strings;
import com.hzq.analysis.server.domain.vo.DrawItemVO;
import com.hzq.analysis.server.service.AnalysisSepsisService;
import com.hzq.core.result.Result;
import com.hzq.core.util.DateTimeUtils;
import com.hzq.security.annotation.RequiresPermissions;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author hua
 * @className com.hzq.analysis.server.controller AnalysisSepsisController
 * @date 2024/12/19 16:15
 * @description 统计分析——SEPSIS患者请求处理器
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/analysis/sepsis")
public class AnalysisSepsisController {

    private final AnalysisSepsisService analysisSepsisService;

    @GetMapping("/urine-chart")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<List<List<DrawItemVO<Integer>>>> getUrineChart(
            @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime
    ) {
        LocalDateTime start = Strings.isNullOrEmpty(startTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(startTime).atStartOfDay();
        LocalDateTime end = Strings.isNullOrEmpty(endTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(endTime).atStartOfDay();
        return Result.success(analysisSepsisService.getUrineChart(start, end));
    }

    @GetMapping("/white-blood-cell-chart")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<List<DrawItemVO<Double>>> getWhiteBloodCellChart(
            @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime
    ) {
        LocalDateTime start = Strings.isNullOrEmpty(startTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(startTime).atStartOfDay();
        LocalDateTime end = Strings.isNullOrEmpty(endTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(endTime).atStartOfDay();
        return Result.success(analysisSepsisService.getWhiteBloodCellChart(start, end));
    }

    @GetMapping("/end-chart")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<List<DrawItemVO<Integer>>> getEndChart(
            @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime
    ) {
        LocalDateTime start = Strings.isNullOrEmpty(startTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(startTime).atStartOfDay();
        LocalDateTime end = Strings.isNullOrEmpty(endTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(endTime).atStartOfDay();
        return Result.success(analysisSepsisService.getEndChart(start, end));
    }

    @GetMapping("/score-chart")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<List<List<DrawItemVO<Integer>>>> getScoreChart(
            @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime
    ) {
        LocalDateTime start = Strings.isNullOrEmpty(startTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(startTime).atStartOfDay();
        LocalDateTime end = Strings.isNullOrEmpty(endTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(endTime).atStartOfDay();
        return Result.success(analysisSepsisService.getScoreChart(start, end));
    }
}
