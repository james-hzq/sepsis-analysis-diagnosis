package com.hzq.analysis.server.domain.vo;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.vo AgeChartVO
 * @date 2024/12/17 11:33
 * @description 患者年龄图表展示对象
 */
@Data
@ToString
@EqualsAndHashCode
public class AgeChartVO {
    private int[] ageData = new int[100];
    private List<DrawItemVO<Integer>> list = new ArrayList<>();
}
