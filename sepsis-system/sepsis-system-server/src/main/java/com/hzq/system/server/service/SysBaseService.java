package com.hzq.system.server.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Date;
import java.util.List;

/**
 * @author gc
 * @interface com.hzq.system.service BaseService
 * @date 2024/8/30 17:00
 * @description 系统业务类的公共 Service
 */
public class SysBaseService {

    // 查询参数常量
    private static final String STATUS_PARAMETER = "status";
    private static final String CREATE_TIME_PARAMETER = "createTime";

    /**
     * @param status    状态
     * @param startTime 创建初始时间
     * @param endTime   创建结束时间
     * @author gc
     * @date 2024/8/30 17:08
     * @apiNote 用于动态条件查询（状态，创建时间，结束时间的公共方法）
     **/
    public <T> void selectCommonCondition(Root<T> root, CriteriaBuilder cb, List<Predicate> predicateList, Character status, Date startTime, Date endTime) {
        if (status != null)
            predicateList.add(cb.equal(root.get(STATUS_PARAMETER).as(Character.class), status));
        if (startTime != null)
            predicateList.add(cb.greaterThanOrEqualTo(root.get(CREATE_TIME_PARAMETER).as(Date.class), startTime));
        if (endTime != null)
            predicateList.add(cb.lessThanOrEqualTo(root.get(CREATE_TIME_PARAMETER).as(Date.class), endTime));
    }
}
