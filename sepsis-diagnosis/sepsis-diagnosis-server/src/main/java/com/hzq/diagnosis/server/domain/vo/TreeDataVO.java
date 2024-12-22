package com.hzq.diagnosis.server.domain.vo;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hua
 * @className com.hzq.diagnosis.server.domain.vo TreeDataVO
 * @date 2024/12/22 10:10
 * @description 前端展示树形控件的对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TreeDataVO {
    private String key;
    private String label;
    private List<TreeDataVO> children;
}
