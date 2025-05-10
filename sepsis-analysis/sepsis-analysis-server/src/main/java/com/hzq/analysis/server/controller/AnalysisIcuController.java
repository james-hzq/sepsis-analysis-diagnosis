package com.hzq.analysis.server.controller;

import com.google.common.base.Strings;
import com.hzq.analysis.server.domain.vo.AgeChartVO;
import com.hzq.analysis.server.domain.vo.DrawItemVO;
import com.hzq.analysis.server.domain.vo.HeartAndBreathChartVO;
import com.hzq.analysis.server.service.AnalysisIcuService;
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
 * @className com.hzq.analysis.server.controller AnalysisIcuController
 * @date 2024/12/17 11:16
 * @description 统计分析——ICU患者请求处理器
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/analysis/icu")
public class AnalysisIcuController {

    private final AnalysisIcuService analysisIcuService;

    @GetMapping("/age-chart")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<AgeChartVO> getAgeChart(
            @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime
    ) {
        LocalDateTime start = Strings.isNullOrEmpty(startTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(startTime).atStartOfDay();
        LocalDateTime end = Strings.isNullOrEmpty(endTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(endTime).atStartOfDay();
        return Result.success(analysisIcuService.getAgeChart(start, end));
    }

    @GetMapping("/height-weight-chart")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<List<DrawItemVO<Double>>> getHeightAndWeightChart(
            @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime
    ) {
        LocalDateTime start = Strings.isNullOrEmpty(startTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(startTime).atStartOfDay();
        LocalDateTime end = Strings.isNullOrEmpty(endTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(endTime).atStartOfDay();
        return Result.success(analysisIcuService.getHeightAndWeightChart(start, end));
    }

    @GetMapping("/heart-breath-chart")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<HeartAndBreathChartVO> getHeartAndBreathChart(
            @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime
    ) {
        LocalDateTime start = Strings.isNullOrEmpty(startTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(startTime).atStartOfDay();
        LocalDateTime end = Strings.isNullOrEmpty(endTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(endTime).atStartOfDay();
        return Result.success(analysisIcuService.getHeartAndBreathChart(start, end));
    }

    @GetMapping("/end-chart")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<List<DrawItemVO<Integer>>> getEndChart(
            @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime
    ) {
        LocalDateTime start = Strings.isNullOrEmpty(startTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(startTime).atStartOfDay();
        LocalDateTime end = Strings.isNullOrEmpty(endTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(endTime).atStartOfDay();
        return Result.success(analysisIcuService.getEndChart(start, end));
    }

    @GetMapping("/score-chart")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<List<List<DrawItemVO<Integer>>>> getScoreChart(
            @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime
    ) {
        LocalDateTime start = Strings.isNullOrEmpty(startTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(startTime).atStartOfDay();
        LocalDateTime end = Strings.isNullOrEmpty(endTime) ? null : DateTimeUtils.convertStringToChinaLocalDate(endTime).atStartOfDay();
        return Result.success(analysisIcuService.getScoreChart(start, end));
    }
}
