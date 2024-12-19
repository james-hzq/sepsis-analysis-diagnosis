package com.hzq.analysis.server.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.vo HeartAndBreathChartVO
 * @date 2024/12/19 10:20
 * @description 心率和呼吸图表展示对象
 */
@Data
@ToString
@EqualsAndHashCode
public class HeartAndBreathChartVO {
    private List<DrawItemVO<Integer>> heartAndBreathList = new ArrayList<>();
    private List<DrawItemVO<Integer>> heartAvgList = new ArrayList<>();
}
