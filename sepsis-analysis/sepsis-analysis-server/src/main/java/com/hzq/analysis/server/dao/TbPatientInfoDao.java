package com.hzq.analysis.server.dao;

import com.hzq.analysis.server.domain.entity.TbPatientInfo;
import com.hzq.analysis.server.domain.projection.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author hua
 * @interfaceName com.hzq.analysis.server.dao TbPatientInfoDao
 * @date 2024/12/16 8:50
 * @description 患者信息 DAO 层
 */
public interface TbPatientInfoDao extends JpaRepository<TbPatientInfo, Integer> {

    @Query(
            "select " +
            "t1.patientNum as patientNum, " +
            "t1.sepsisNum as sepsisNum, " +
            "round((t1.sepsisNum * 1.0) / (t1.patientNum * 1.0) * 100, 2) as sepsisPro " +
            "from (" +
                "select " +
                "count(distinct t.id) as patientNum, " +
                "sum(case when t.isSepsis = '0' then 1 else 0 end) as sepsisNum " +
                "from TbPatientInfo t" +
            ") t1"
    )
    Optional<PatientReportProjection> findPatientReport();

    @Query(
            "select " +
                "t.icuLocation as icuLoacltion, " +
                "count(distinct t.id) as patientNum, " +
                "sum(case when t.isSepsis = '0' then 1 else 0 end) as sepsisNum " +
            "from TbPatientInfo as t " +
            "group by t.icuLocation"
    )
    Optional<List<PatientLocationProjection>> findPatientLocation();

    @Query(
            "select t.age as age, count(t.id) as cnt from TbPatientInfo t " +
            "where t.inTime between :startTime and :endTime " +
            "group by t.age " +
            "order by t.age asc"
    )
    Optional<List<AgeChartProjection>> findAgeChartInIcu(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
    );

    @Query(
            "select " +
                    "cast(sum(case when t.weight < 40 then 1 else 0 end) as double) as w1, " +
                    "cast(sum(case when t.weight >= 40 and t.weight < 80 then 1 else 0 end) as double) as w2, " +
                    "cast(sum(case when t.weight >= 80 and t.weight < 120 then 1 else 0 end) as double) as w3, " +
                    "cast(sum(case when t.weight >= 120 then 1 else 0 end) as double) as w4, " +
                    "cast(sum(case when t.height < 140 then 1 else 0 end) as double) as h1, " +
                    "cast(sum(case when t.height >= 140 and t.height < 160 then 1 else 0 end) as double) as h2, " +
                    "cast(sum(case when t.height >= 160 and t.height < 180 then 1 else 0 end) as double) as h3, " +
                    "cast(sum(case when t.height >= 180 then 1 else 0 end) as double) as h4, " +
                    "(select cast(round(avg(t1.weight), 2) as double) from TbPatientInfo t1 " +
                        "where " +
                        "t1.gender = 'M' and " +
                        "t1.weight is not null and " +
                        "t1.inTime between :startTime and :endTime" +
                    ") as maleAvgWeight, " +
                    "(select cast(round(avg(t1.weight), 2) as double) from TbPatientInfo t1 " +
                        "where " +
                        "t1.gender = 'F' and " +
                        "t1.weight is not null and " +
                        "t1.inTime between :startTime and :endTime" +
                    ") as femaleAvgWeight, " +
                    "(select cast(round(avg(t1.height), 2) as double) from TbPatientInfo t1 " +
                        "where " +
                        "t1.gender = 'M' and " +
                        "t1.height is not null and " +
                        "t1.inTime between :startTime and :endTime" +
                    ") as maleAvgHeight, " +
                    "(select cast(round(avg(t1.height), 2) as double) from TbPatientInfo t1 " +
                        "where " +
                        "t1.gender = 'F' and " +
                        "t1.height is not null and " +
                        "t1.inTime between :startTime and :endTime" +
                    ") as femaleAvgHeight " +
                    "from TbPatientInfo t " +
                    "where " +
                    "t.weight is not null and " +
                    "t.height is not null and " +
                    "t.inTime between :startTime and :endTime"
    )
    Optional<HeightAndWeightChartProjection> findHeightAndWeightChartInIcu(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
    );

    @Query("select " +
            "sum(case when t.isDied = '0' then 1 else 0 end) as survivalNum, " +
            "sum(case when t.isDied = '1' then 1 else 0 end) as diedNum " +
            "from TbPatientInfo t " +
            "where t.inTime between :startTime and :endTime")
    Optional<EndChartProjection> findEndChartInIcu(
            @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime
    );
}
