package com.hzq.analysis.server.domain.vo;

import lombok.*;

/**
 * @author hua
 * @className com.hzq.analysis.server.domain.vo DrawItemVO
 * @date 2024/12/17 11:34
 * @description 用于前端绘图的JavaBean
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DrawItemVO<T> {
    private String name;
    private T value;
}
