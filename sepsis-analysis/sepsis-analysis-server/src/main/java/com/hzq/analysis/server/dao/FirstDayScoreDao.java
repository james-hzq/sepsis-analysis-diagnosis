package com.hzq.analysis.server.dao;

import com.hzq.analysis.server.domain.entity.FirstDayScore;
import com.hzq.analysis.server.domain.projection.ScoreChartProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author hua
 * @interfaceName com.hzq.analysis.server.dao FirstDayScoreDao
 * @date 2024/12/19 13:31
 * @description 入ICU第一天得分 Dao 层
 */
public interface FirstDayScoreDao extends JpaRepository<FirstDayScore, Integer> {

    @Query("select t.sofa as score, count(*) as total from FirstDayScore t " +
            "where (:startTime is null or t.inTime >= :startTime) and (:endTime is null or t.inTime <= :endTime) " +
            "group by t.sofa order by t.sofa")
    Optional<List<ScoreChartProjection>> findSofaChartInIcu(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
    );

    @Query("select t.sofa as score, count(*) as total from FirstDayScore t " +
            "where t.isSepsis = '1' and (:startTime is null or t.inTime >= :startTime) and (:endTime is null or t.inTime <= :endTime) " +
            "group by t.sofa order by t.sofa")
    Optional<List<ScoreChartProjection>> findSofaChartInSepsis(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
    );

    @Query("select t.gcsMotor as score, count(*) as total from FirstDayScore t " +
            "where (:startTime is null or t.inTime >= :startTime) and (:endTime is null or t.inTime <= :endTime) " +
            "group by t.gcsMotor order by t.gcsMotor")
    Optional<List<ScoreChartProjection>> findGcsMotorChartInIcu(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
    );

    @Query("select t.gcsMotor as score, count(*) as total from FirstDayScore t " +
            "where t.isSepsis = '1' and (:startTime is null or t.inTime >= :startTime) and (:endTime is null or t.inTime <= :endTime) " +
            "group by t.gcsMotor order by t.gcsMotor")
    Optional<List<ScoreChartProjection>> findGcsMotorChartInSepsis(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
    );

    @Query("select t.gcsVerbal as score, count(*) as total from FirstDayScore t " +
            "where (:startTime is null or t.inTime >= :startTime) and (:endTime is null or t.inTime <= :endTime) " +
            "group by t.gcsVerbal order by t.gcsVerbal")
    Optional<List<ScoreChartProjection>> findGcsVerbalChartInIcu(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
    );

    @Query("select t.gcsVerbal as score, count(*) as total from FirstDayScore t " +
            "where t.isSepsis = '1' and (:startTime is null or t.inTime >= :startTime) and (:endTime is null or t.inTime <= :endTime) " +
            "group by t.gcsVerbal order by t.gcsVerbal")
    Optional<List<ScoreChartProjection>> findGcsVerbalChartInSepsis(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
    );

    @Query("select t.gcsEyes as score, count(*) as total from FirstDayScore t " +
            "where (:startTime is null or t.inTime >= :startTime) and (:endTime is null or t.inTime <= :endTime) " +
            "group by t.gcsEyes order by t.gcsEyes")
    Optional<List<ScoreChartProjection>> findGcsEyesChartInIcu(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
    );

    @Query("select t.gcsEyes as score, count(*) as total from FirstDayScore t " +
            "where t.isSepsis = '1' and (:startTime is null or t.inTime >= :startTime) and (:endTime is null or t.inTime <= :endTime) " +
            "group by t.gcsEyes order by t.gcsEyes")
    Optional<List<ScoreChartProjection>> findGcsEyesChartInSepsis(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
    );
}
