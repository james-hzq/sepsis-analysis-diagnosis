package com.hzq.system.server.domain.vo;

import lombok.*;

/**
 * @author hua
 * @className com.hzq.system.server.domain.vo ProvinceCityMapVO
 * @date 2024/12/12 9:51
 * @description 安徽省地图展示对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ProvinceCityMapVO {
    private String name;
    private Integer value;
}
