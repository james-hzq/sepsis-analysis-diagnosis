package com.hzq.analysis.server.dao;

import com.hzq.analysis.server.domain.entity.FirstDayBg;
import com.hzq.analysis.server.domain.projection.WhiteBloodCellChartProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author hua
 * @interfaceName com.hzq.analysis.server.dao FirstDayBgDao
 * @date 2024/12/19 19:26
 * @description 入ICU第一天血气分析 Dao 层
 */
public interface FirstDayBgDao extends JpaRepository<FirstDayBg, Integer> {

    @Query(
            "select " +
                    "round(avg(t.lympCountMin), 2) as lympCountMin, " +
                    "round(avg(t.monocytesMin), 2) as monocytesMin, " +
                    "round(avg(t.eosinophilsMin), 2) as eosinophilsMin, " +
                    "round(avg(t.basophilsMin), 2) as basophilsMin, " +
                    "round(avg(t.neutrophilsMin), 2) as neutrophilsMin, " +
                    "round(avg(t.lympCountMax), 2) as lympCountMax, " +
                    "round(avg(t.monocytesMax), 2) as monocytesMax, " +
                    "round(avg(t.eosinophilsMax), 2) as eosinophilsMax, " +
                    "round(avg(t.basophilsMax), 2) as basophilsMax, " +
                    "round(avg(t.neutrophilsMax), 2) as neutrophilsMax " +
            "from FirstDayBg t " +
            "where t.inTime between :startTime and :endTime"
    )
    Optional<WhiteBloodCellChartProjection> findWhiteBloodCellChart(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
    );
}
