package com.hzq.analysis.server.dao;

import com.hzq.analysis.server.domain.entity.TbPatientInfo;
import com.hzq.analysis.server.domain.projection.PatientLocationProjection;
import com.hzq.analysis.server.domain.projection.PatientReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
