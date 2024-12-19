package com.hzq.analysis.server.service;

import com.hzq.analysis.server.dao.FirstDayBgDao;
import com.hzq.analysis.server.dao.FirstDayScoreDao;
import com.hzq.analysis.server.dao.FirstDayUrineDao;
import com.hzq.analysis.server.dao.TbPatientInfoDao;
import com.hzq.analysis.server.domain.dto.EndChart;
import com.hzq.analysis.server.domain.dto.ScoreChart;
import com.hzq.analysis.server.domain.dto.UrineChart;
import com.hzq.analysis.server.domain.dto.WhiteBloodCellChart;
import com.hzq.analysis.server.domain.vo.DrawItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hua
 * @className com.hzq.analysis.server.service AnalysisSepsisService
 * @date 2024/12/19 16:15
 * @description 统计分析——SEPSIS患者业务处理类
 */
@RequiredArgsConstructor
@Service
public class AnalysisSepsisService extends BaseStatisticsService {

    private final TbPatientInfoDao tbPatientInfoDao;
    private final FirstDayScoreDao firstDayScoreDao;
    private final FirstDayUrineDao firstDayUrineDao;
    private final FirstDayBgDao firstDayBgDao;

    // 自定义线程池，避免使用默认的 ForkJoinPool
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public List<List<DrawItemVO<Integer>>> getUrineChart(LocalDateTime startTime, LocalDateTime endTime) {
        // 查询患者尿量分组
        List<UrineChart> urineChartList = firstDayUrineDao.findUrineChart(startTime, endTime)
                .map(list -> list.stream().map(UrineChart::new).toList())
                .orElse(Collections.emptyList());
        // 根据患者尿量分组 构建前端展示对象
        return createUrineChart(urineChartList);
    }

    public List<DrawItemVO<Double>> getWhiteBloodCellChart(LocalDateTime startTime, LocalDateTime endTime) {
        // 查询白细胞分类计数最小值和最大值
        WhiteBloodCellChart whiteBloodCellChart = firstDayBgDao.findWhiteBloodCellChart(startTime, endTime)
                .map(WhiteBloodCellChart::new)
                .orElse(new WhiteBloodCellChart());
        // 根据白细胞分类计数最小值和最大值 构建前端展示对象
        return createWhiteBloodCellChart(whiteBloodCellChart);
    }

    public List<DrawItemVO<Integer>> getEndChart(LocalDateTime startTime, LocalDateTime endTime) {
        // 查询患者结局
        EndChart endChart = tbPatientInfoDao.findEndChartInSepsis(startTime, endTime)
                .map(EndChart::new)
                .orElse(new EndChart());
        // 根据患者结局查询对象 构建前端展示对象
        return createEndChart(endChart);
    }

    public List<List<DrawItemVO<Integer>>> getScoreChart(LocalDateTime startTime, LocalDateTime endTime) {
        List<CompletableFuture<List<ScoreChart>>> futures = List.of(
                fetchScoreChartAsync(startTime, endTime, firstDayScoreDao::findSofaChartInSepsis, executorService),
                fetchScoreChartAsync(startTime, endTime, firstDayScoreDao::findGcsMotorChartInSepsis, executorService),
                fetchScoreChartAsync(startTime, endTime, firstDayScoreDao::findGcsVerbalChartInSepsis, executorService),
                fetchScoreChartAsync(startTime, endTime, firstDayScoreDao::findGcsEyesChartInSepsis, executorService)
        );
        List<List<ScoreChart>> results = futures.stream()
                .map(CompletableFuture::join)
                .toList();
        return createScoreChart(results);
    }
}
