package com.hzq.web.util;

import com.google.common.base.Converter;
import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

/**
 * @author hua
 * @className com.hzq.web.util.page PageUtils
 * @date 2024/12/7 17:04
 * @description 分页请求工具类
 */
public class PageUtils {

    // 起始页码
    private static final String PAGE_NUM = "pageNum";
    // 每页显示记录数
    private static final String PAGE_SIZE = "pageSize";
    // 排序列
    private static final String ORDER_BY = "orderBy";
    // 排序方向
    private static final String DIRECTION = "direction";
    // 默认起始页码
    private static final Integer DEFAULT_PAGE_NUM = 1;
    // 默认每页显示记录数
    private static final Integer DEFAULT_PAGE_SIZE = 10;
    // 顺序排序
    private static final String ASC = "ASC";
    // 逆序排序
    private static final String DESC = "DESC";
    // 分页参数转换器
    private static final Converter<String, Integer> pageParameterConverter = Ints.stringConverter();

    /**
     * @author hua
     * @date 2024/12/7 21:12
     * @return org.springframework.data.domain.Pageable
     * @apiNote 构建分页请求，准许多个字段的同序排序，多字段不同序排序需要自己构建
     **/
    public static Pageable buildPageRequest() {
        int pageNum = Optional.ofNullable(pageParameterConverter.convert(ServletUtils.getParameter(PAGE_NUM)))
                .orElse(DEFAULT_PAGE_NUM) - 1;
        int pageSize = Optional.ofNullable(pageParameterConverter.convert(ServletUtils.getParameter(PAGE_SIZE)))
                .orElse(DEFAULT_PAGE_SIZE);
        String orderBy = ServletUtils.getParameter(ORDER_BY);
        String direction = ServletUtils.getParameter(DIRECTION);
        if (Strings.isNullOrEmpty(orderBy) || (!ASC.equals(direction) && !DESC.equals(direction))) {
            return PageRequest.of(pageNum, pageSize, Sort.unsorted());
        }
        return PageRequest.of(pageNum, pageSize, ASC.equals(direction) ? Sort.Direction.ASC : Sort.Direction.DESC, orderBy);
    }
}
