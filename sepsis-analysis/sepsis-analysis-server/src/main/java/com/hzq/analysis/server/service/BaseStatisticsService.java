package com.hzq.analysis.server.service;

import com.google.common.collect.Lists;
import com.hzq.analysis.server.domain.dto.*;
import com.hzq.analysis.server.domain.projection.ScoreChartProjection;
import com.hzq.analysis.server.domain.vo.AgeChartVO;
import com.hzq.analysis.server.domain.vo.DrawItemVO;
import com.hzq.analysis.server.domain.vo.HeartAndBreathChartVO;
import com.hzq.analysis.server.domain.vo.HeightAndWeightChartVO;
import com.hzq.core.util.ReflectionUtils;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author hua
 * @className com.hzq.analysis.server.service BaseStatisticsService
 * @date 2024/12/17 11:40
 * @description 统计分析基本业务类，提取公共方法，常量等
 */
public class BaseStatisticsService {
    // 年龄分组
    public static final String[] AGE_LABELS = {
            "0 - 20岁", "21 - 40岁", "41 - 60岁", "61 - 80岁", "81 - 100岁"
    };
    public static int[] AGE_LABELS_STANDARD = {
            20, 40, 60, 80, 100
    };
    // 身高体重分组
    public static final String[] HEIGHT_AND_WEIGHT_LABELS = {
            "< 40", "40 - 79", "80 - 119", ">= 120", "< 140", "140 - 159", "160 - 179", ">= 180",
            "男性体重平均值(kg)", "女性体重平均值(kg)", "男性身高平均值(cm)", "女性身高平均值(cm)"
    };
    public static final String[] HEIGHT_AND_WEIGHT_FIELDS = {
            "w1", "w2", "w3", "w4", "h1", "h2", "h3", "h4",
            "mAvgWeight", "fAvgWeight", "mAvgHeight", "fAvgHeight"
    };
    // 心率和呼吸分组
    public static final String[] HEART_AND_BREATH_LABELS = {
            "最小心率均值", "心率均值", "最大心率均值", "最小呼吸均值", "呼吸均值", "最大呼吸均值"
    };
    public static final String[] HEART_AVG_LABELS = {
            "<60次/分", "60-69次/分", "70-79次/分", "80-89次/分", ">=90次/分"
    };
    public static final Integer[] HEART_AVG_STANDARD = {
            60, 70, 80, 90, 100000
    };
    // 患者结局分组
    public static final String[] END_LABELS = {
            "存活人数", "死亡人数"
    };
    // 尿量分组
    public static final String[] URINE_LABELS = {
            "< 0.5L", "0.5-1L", "1-1.5L", "1.5-2L", "2-2.5L", "2.5-3L", "> 3L"
    };
    public static final Integer[] URINE_STANDARD = {
            500, 1000, 1500, 2000, 2500, 3000, 100000
    };
    // 淋巴细胞计数分组
    public static final String[] WHITE_BLOOD_CELL_CHART_LABELS = {
            "淋巴细胞计数", "单核细胞计数", "嗜碱粒细胞计数", "嗜酸粒细胞计数", "中性粒细胞计数"
    };

    /***
     * @author hua
     * @date 2024/12/17 19:42
     * @param ageChartList 年龄分组信息集合
     * @return com.hzq.analysis.server.domain.vo.AgeChartVO
     * @apiNote 根据年龄分组数据构建前端展示对象
     **/
    protected AgeChartVO createAgeChartVO(List<AgeChart> ageChartList) {
        // 创建 AgeChartVO 对象
        AgeChartVO ageChartVO = new AgeChartVO();
        int[] ageData = ageChartVO.getAgeData();
        List<DrawItemVO<Integer>> ageGroupList = ageChartVO.getList();
        // 当前年龄分组索引
        int groupIndex = 0;
        // 当前年龄分组人数
        int groupTotal = 0;

        for (AgeChart data : ageChartList) {
            int age = data.getAge();
            int cnt = data.getCnt();
            // 更新当前年龄的对应人数
            ageData[age - 1] = cnt;
            // 更新当前年龄分组的对应人数
            while (groupIndex < AGE_LABELS_STANDARD.length && age > AGE_LABELS_STANDARD[groupIndex]) {
                ageGroupList.add(new DrawItemVO<>(AGE_LABELS[groupIndex], groupTotal));
                groupTotal = 0;
                groupIndex++;
            }
            groupTotal += cnt;
        }

        // 添加最后一个分组
        if (groupIndex < AGE_LABELS.length) {
            ageGroupList.add(new DrawItemVO<>(AGE_LABELS[groupIndex], groupTotal));
        }

        return ageChartVO;
    }

    /**
     * @author hua
     * @date 2024/12/18 20:34
     * @param vo 患者身高体重图表查询对象
     * @return java.util.List<com.hzq.analysis.server.domain.vo.DrawItemVO<java.lang.Integer>>
     * @apiNote 根据身高体重分组数据构建前端展示对象
     **/
    protected List<DrawItemVO<Double>> createHeightAndWeightChart(HeightAndWeightChartVO vo) {
        List<DrawItemVO<Double>> resList = new ArrayList<>();
        IntStream.range(0, HEIGHT_AND_WEIGHT_LABELS.length)
                .forEach(i -> resList.add(new DrawItemVO<>(
                        HEIGHT_AND_WEIGHT_LABELS[i],
                        (Double) ReflectionUtils.getFieldValue(vo, HEIGHT_AND_WEIGHT_FIELDS[i])
                )));
        return resList;
    }

    /**
     * @author hua
     * @date 2024/12/19 13:14
     * @param heartAndBreathChart 心率和呼吸 最大值、均值、最小值的查询对象
     * @param heartAvgChartList 平均心率分组的查询对象
     * @return com.hzq.analysis.server.domain.vo.HeartAndBreathChartVO
     * @apiNote 根据心率和呼吸最小值、均值、最大值数据 和 平均心率分组数据 构建前端展示对象
     **/
    protected HeartAndBreathChartVO createHeartAndBreathChart(HeartAndBreathChart heartAndBreathChart, List<HeartAvgChart> heartAvgChartList) {
        HeartAndBreathChartVO resultVO = new HeartAndBreathChartVO();
        List<DrawItemVO<Integer>> heartAndBreathList = resultVO.getHeartAndBreathList();
        List<DrawItemVO<Integer>> heartAvgList = resultVO.getHeartAvgList();
        // 添加心率、呼吸 最大值、均值、最小值的图表展示对象
        IntStream.range(0, HEART_AND_BREATH_LABELS.length)
                .forEach(i -> heartAndBreathList.add(
                        new DrawItemVO<>(HEART_AND_BREATH_LABELS[i], heartAndBreathChart.getValue(HEART_AND_BREATH_LABELS[i]))
                ));
        // 添加平均心率分组的图表展示对象
        final int[] data = new int[HEART_AVG_LABELS.length];
        int idx = 0;
        for (HeartAvgChart chart : heartAvgChartList) {
            if (chart.getName() >= HEART_AVG_STANDARD[idx]) idx++;
            data[idx] += chart.getValue();
        }
        IntStream.range(0, HEART_AVG_LABELS.length)
                .forEach(i -> heartAvgList.add(
                        new DrawItemVO<>(HEART_AVG_LABELS[i], data[i])
                ));
        return resultVO;
    }

    /**
     * @author hua
     * @date 2024/12/19 18:54
     * @param urineChartList 患者尿量分组集合
     * @return java.util.List<java.util.List<com.hzq.analysis.server.domain.vo.DrawItemVO<java.lang.Integer>>>
     * @apiNote 根据患者尿量分组 构建前端展示对象
     **/
    protected List<List<DrawItemVO<Integer>>> createUrineChart(List<UrineChart> urineChartList) {
        // 获取当前尿量以及对应人数
        List<DrawItemVO<Integer>> urineList = urineChartList
                .stream()
                .map(urineChart -> new DrawItemVO<>(
                        String.valueOf(urineChart.getUrineOutput()), urineChart.getTotal()
                ))
                .toList();
        // 获取尿量分组人数
        int[] labelCnt = {0, 0, 0, 0, 0, 0, 0};
        int idx = 0;
        for (UrineChart item : urineChartList) {
            if (item.getUrineOutput().compareTo(URINE_STANDARD[idx]) >= 0) idx++;
            labelCnt[idx] += item.getTotal();
        }
        List<DrawItemVO<Integer>> urineGroupList = IntStream.range(0, URINE_LABELS.length)
                .mapToObj(i -> new DrawItemVO<>(URINE_LABELS[i], labelCnt[i]))
                .toList();
        // 返回结果集合
        return Lists.newArrayList(urineList, urineGroupList);
    }

    /**
     * @author hua
     * @date 2024/12/19 19:49
     * @param whiteBloodCellChart 白细胞分类计数最小值和最大值
     * @return java.util.List<com.hzq.analysis.server.domain.vo.DrawItemVO<java.lang.Double>>
     * @apiNote 根据白细胞分类计数最小值和最大值 构建前端展示对象
     **/
    protected List<DrawItemVO<Double>> createWhiteBloodCellChart(WhiteBloodCellChart whiteBloodCellChart) {
        int length = WHITE_BLOOD_CELL_CHART_LABELS.length;
        return IntStream.range(0, length << 1)
                .mapToObj(i -> new DrawItemVO<>(WHITE_BLOOD_CELL_CHART_LABELS[i % length], whiteBloodCellChart.getValue(i)))
                .toList();
    }

    /**
     * @author hua
     * @date 2024/12/19 13:16
     * @param endChart 患者结局查询对象
     * @return java.util.List<com.hzq.analysis.server.domain.vo.DrawItemVO<java.lang.Integer>>
     * @apiNote 根据患者结局查询对象 构建前端展示对象
     **/
    protected List<DrawItemVO<Integer>> createEndChart(EndChart endChart) {
        List<DrawItemVO<Integer>> resList = new ArrayList<>(2);
        IntStream.range(0, END_LABELS.length)
                .forEach(i -> resList.add(
                        new DrawItemVO<>(END_LABELS[i], endChart.getValue(END_LABELS[i]))
                ));
        return resList;
    }

    /**
     * @author hua
     * @date 2024/12/19 17:44
     * @param startTime 查询的开始时间
     * @param endTime 查询的结束时间
     * @param queryFunction DAO 查询方法的函数引用
     * @param threadPool 自定义的线程池
     * @return java.util.concurrent.CompletableFuture<java.util.List<com.hzq.analysis.server.domain.dto.ScoreChart>>
     * @apiNote 用于得分图表查询的并发方法
     **/
    protected CompletableFuture<List<ScoreChart>> fetchScoreChartAsync(
            LocalDateTime startTime,
            LocalDateTime endTime,
            BiFunction<LocalDateTime, LocalDateTime, Optional<List<ScoreChartProjection>>> queryFunction,
            ExecutorService threadPool
    ) {
        return CompletableFuture.supplyAsync(() ->
                        queryFunction.apply(startTime, endTime)
                                .map(list -> list.stream().map(ScoreChart::new).toList())
                                .orElse(new ArrayList<>()),
                threadPool
        );
    }

    /**
     * @author hua
     * @date 2024/12/19 13:55
     * @param lists 得分情况
     * @return java.util.List<java.util.List<com.hzq.analysis.server.domain.vo.DrawItemVO<java.lang.Integer>>>
     * @apiNote 根据得分情况 构建前端展示对象
     **/
    protected final List<List<DrawItemVO<Integer>>> createScoreChart(List<List<ScoreChart>> lists) {
        return lists.stream()
                .map(this::scoreChartListToDrawItemVOList)
                .collect(Collectors.toList());
    }

    /**
     * @author hua
     * @date 2024/12/19 14:05
     * @param list 得分情况
     * @return java.util.List<com.hzq.analysis.server.domain.vo.DrawItemVO<java.lang.Integer>>
     * @apiNote 将得分情况集合转换为前端展示对象集合
     **/
    private List<DrawItemVO<Integer>> scoreChartListToDrawItemVOList(List<ScoreChart> list) {
        return list.stream()
                .map(scoreChart -> new DrawItemVO<>(String.valueOf(scoreChart.getScore()), scoreChart.getTotal()))
                .collect(Collectors.toList());
    }
}
