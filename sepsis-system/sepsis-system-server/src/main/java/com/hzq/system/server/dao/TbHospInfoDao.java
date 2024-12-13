package com.hzq.system.server.dao;

import com.hzq.system.server.domain.entity.TbHospInfo;
import com.hzq.system.server.domain.projecction.ProvinceCityMapProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * @author hua
 * @interfaceName com.hzq.system.server.dao TbHospInfoDao
 * @date 2024/12/12 8:48
 * @description 医院站点信息 DAO 层
 */
public interface TbHospInfoDao extends JpaRepository<TbHospInfo, Integer> {

    @Query("select t.icuCity as name, count(t.id) as value " +
            "from TbHospInfo t group by t.icuCity order by t.icuCity")
    Optional<List<ProvinceCityMapProjection>> findProvinceMap();

    @Query("select t.icuDist as name, count(t.id) as value " +
            "from TbHospInfo as t group by t.icuCity, t.icuDist having t.icuCity =:icuCity")
    Optional<List<ProvinceCityMapProjection>> findCityMap(@Param("icuCity") String icuCity);

    @Query("select t from TbHospInfo t where t.icuCity =:icuCity")
    Optional<List<TbHospInfo>> findTbHospInfoByIcuCity(@Param("icuCity") String icuCity);
}
