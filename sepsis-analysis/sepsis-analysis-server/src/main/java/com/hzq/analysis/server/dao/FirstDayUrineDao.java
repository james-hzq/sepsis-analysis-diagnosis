package com.hzq.analysis.server.dao;

import com.hzq.analysis.server.domain.entity.FirstDayUrine;
import com.hzq.analysis.server.domain.projection.UrineChartProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author hua
 * @interfaceName com.hzq.analysis.server.dao FirstDayUrine
 * @date 2024/12/19 18:42
 * @description 入ICU第一天尿量 Dao 层
 */
public interface FirstDayUrineDao extends JpaRepository<FirstDayUrine, Integer> {

    @Query("select t.urineOutput as urineOutput, count(*) as total " +
            "from FirstDayUrine t " +
            "where t.urineOutput is not null and t.urineOutput >= 0 and t.inTime between :startTime and :endTime " +
            "group by t.urineOutput order by t.urineOutput")
    Optional<List<UrineChartProjection>> findUrineChart(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
    );
}
