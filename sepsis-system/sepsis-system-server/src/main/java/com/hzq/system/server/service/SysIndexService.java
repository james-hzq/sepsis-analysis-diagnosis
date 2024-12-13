package com.hzq.system.server.service;

import com.hzq.system.server.dao.TbHospInfoDao;
import com.hzq.system.server.domain.entity.TbHospInfo;
import com.hzq.system.server.domain.projecction.ProvinceCityMapProjection;
import com.hzq.system.server.domain.vo.ProvinceCityMapVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author hua
 * @className com.hzq.system.server.service SysIndexService
 * @date 2024/12/11 21:11
 * @description 系统首页业务类
 */
@RequiredArgsConstructor
@Service
public class SysIndexService {

    private final TbHospInfoDao hospInfoDao;

    public List<ProvinceCityMapVO> getProvinceMapData() {
        return hospInfoDao.findProvinceMap()
                .orElse(new ArrayList<>())
                .stream()
                .sorted(Comparator.comparing(ProvinceCityMapProjection::getName, Collator.getInstance(Locale.CHINESE)))
                .map(projection -> new ProvinceCityMapVO(projection.getName(), projection.getValue().intValue()))
                .collect(Collectors.toList());
    }

    public List<ProvinceCityMapVO> getCityMapData(String icuCity) {
        return hospInfoDao.findCityMap(icuCity)
                .orElse(new ArrayList<>())
                .stream()
                .map(projection -> new ProvinceCityMapVO(projection.getName(), projection.getValue().intValue()))
                .collect(Collectors.toList());
    }

    public List<TbHospInfo> getTbHospInfo(String icuCity) {
        return hospInfoDao.findTbHospInfoByIcuCity(icuCity).orElse(new ArrayList<>());
    }
}
