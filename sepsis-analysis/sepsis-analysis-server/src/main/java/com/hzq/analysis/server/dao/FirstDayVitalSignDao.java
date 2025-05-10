package com.hzq.analysis.server.dao;

import com.hzq.analysis.server.domain.entity.FirstDayVitalSign;
import com.hzq.analysis.server.domain.projection.HeartAndBreathChartProjection;
import com.hzq.analysis.server.domain.projection.HeartAvgChartProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author hua
 * @interfaceName com.hzq.analysis.server.dao FirstDayVitalSignDao
 * @date 2024/12/19 10:02
 * @description 入ICU第一天的生命体征 Dao 层
 */
public interface FirstDayVitalSignDao extends JpaRepository<FirstDayVitalSign, Integer> {

    @Query("select " +
            "ceiling(avg(t.heartMin)) as heartMin, " +
            "ceiling(avg(t.heart)) as heart, " +
            "ceiling(avg(t.heartMax)) as heartMax, " +
            "ceiling(avg(t.breathMin)) as breathMin, " +
            "ceiling(avg(t.breath)) as breath, " +
            "ceiling(avg(t.breathMax)) as breathMax " +
            "from FirstDayVitalSign t " +
            "where (:startTime is null or t.inTime >= :startTime) and (:endTime is null or t.inTime <= :endTime)")
    Optional<HeartAndBreathChartProjection> findHeartAndBreathChartInIcu(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
    );

    @Query("select ceiling(t.heart) as name, count(*) as value from FirstDayVitalSign t " +
            "where t.heart is not null and (:startTime is null or t.inTime >= :startTime) and (:endTime is null or t.inTime <= :endTime) " +
            "group by ceiling(t.heart)")
    Optional<List<HeartAvgChartProjection>> findHeartAvgChartInIcu(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
    );
}
