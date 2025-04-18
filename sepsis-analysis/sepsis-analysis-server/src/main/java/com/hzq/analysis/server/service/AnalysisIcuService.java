package com.hzq.analysis.server.service;

import com.google.common.collect.Lists;
import com.hzq.analysis.server.dao.FirstDayScoreDao;
import com.hzq.analysis.server.dao.FirstDayVitalSignDao;
import com.hzq.analysis.server.dao.TbPatientInfoDao;
import com.hzq.analysis.server.domain.dto.*;
import com.hzq.analysis.server.domain.entity.FirstDayScore;
import com.hzq.analysis.server.domain.projection.ScoreChartProjection;
import com.hzq.analysis.server.domain.vo.AgeChartVO;
import com.hzq.analysis.server.domain.vo.DrawItemVO;
import com.hzq.analysis.server.domain.vo.HeartAndBreathChartVO;
import com.hzq.analysis.server.domain.vo.HeightAndWeightChartVO;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author hua
 * @className com.hzq.analysis.server.service AnalysisIcuService
 * @date 2024/12/17 11:18
 * @description 统计分析——ICU患者业务处理类
 */
@RequiredArgsConstructor
@Service
public class AnalysisIcuService extends BaseStatisticsService {

    private final TbPatientInfoDao tbPatientInfoDao;
    private final FirstDayVitalSignDao firstDayVitalSignDao;
    private final FirstDayScoreDao firstDayScoreDao;

    // 自定义线程池，避免使用默认的 ForkJoinPool
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public AgeChartVO getAgeChart(LocalDateTime startTime, LocalDateTime endTime) {
        // 查询年龄分组数据
        List<AgeChart> ageChartList = tbPatientInfoDao.findAgeChartInIcu(startTime, endTime)
                .map(list -> list.stream().map(AgeChart::new).toList())
                .orElseGet(Collections::emptyList);
        // 根据年龄分组数据构建 AgeChartVO
        return createAgeChartVO(ageChartList);
    }

    public List<DrawItemVO<Double>> getHeightAndWeightChart(LocalDateTime startTime, LocalDateTime endTime) {
        // 查询身高体重分组数据
        HeightAndWeightChartVO heightAndWeightChartVO = tbPatientInfoDao.findHeightAndWeightChartInIcu(startTime, endTime)
                .map(HeightAndWeightChartVO::new)
                .orElse(new HeightAndWeightChartVO());
        // 根据身高体重分组数据构建前端展示对象
        return createHeightAndWeightChart(heightAndWeightChartVO);
    }

    public HeartAndBreathChartVO getHeartAndBreathChart(LocalDateTime startTime, LocalDateTime endTime) {
        // 查询心率和呼吸最小值、均值、最大值数据
        HeartAndBreathChart heartAndBreathChart = firstDayVitalSignDao.findHeartAndBreathChartInIcu(startTime, endTime)
                .map(HeartAndBreathChart::new)
                .orElse(new HeartAndBreathChart());
        // 查询平均心率分组数据
        List<HeartAvgChart> heartAvgChartList = firstDayVitalSignDao.findHeartAvgChartInIcu(startTime, endTime)
                .map(list -> list.stream()
                        .map(HeartAvgChart::new)
                        .sorted(Comparator.comparing(HeartAvgChart::getName))
                        .toList()
                )
                .orElseGet(Collections::emptyList);
        // 根据心率和呼吸最小值、均值、最大值数据 和 平均心率分组数据 构建前端展示对象
        return createHeartAndBreathChart(heartAndBreathChart, heartAvgChartList);
    }

    public List<DrawItemVO<Integer>> getEndChart(LocalDateTime startTime, LocalDateTime endTime) {
        // 查询患者结局
        EndChart endChart = tbPatientInfoDao.findEndChartInIcu(startTime, endTime)
                .map(EndChart::new)
                .orElse(new EndChart());
        // 根据患者结局查询对象 构建前端展示对象
        return createEndChart(endChart);
    }

    public List<List<DrawItemVO<Integer>>> getScoreChart(LocalDateTime startTime, LocalDateTime endTime) {
        List<CompletableFuture<List<ScoreChart>>> futures = List.of(
                fetchScoreChartAsync(startTime, endTime, firstDayScoreDao::findSofaChartInIcu, executorService),
                fetchScoreChartAsync(startTime, endTime, firstDayScoreDao::findGcsMotorChartInIcu, executorService),
                fetchScoreChartAsync(startTime, endTime, firstDayScoreDao::findGcsVerbalChartInIcu, executorService),
                fetchScoreChartAsync(startTime, endTime, firstDayScoreDao::findGcsEyesChartInIcu, executorService)
        );
        List<List<ScoreChart>> results = futures.stream()
                .map(CompletableFuture::join)
                .toList();
        return createScoreChart(results);
    }
}
