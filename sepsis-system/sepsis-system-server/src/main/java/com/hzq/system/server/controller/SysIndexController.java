package com.hzq.system.server.controller;

import com.hzq.core.result.Result;
import com.hzq.security.annotation.RequiresPermissions;
import com.hzq.system.server.domain.entity.TbHospInfo;
import com.hzq.system.server.domain.vo.ProvinceCityMapVO;
import com.hzq.system.server.service.SysIndexService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hua
 * @className com.hzq.system.server.controller SysIndexController
 * @date 2024/12/11 21:10
 * @description 系统首页请求处理器
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/index")
public class SysIndexController {

    private final SysIndexService sysIndexService;

    @GetMapping("/province")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<List<ProvinceCityMapVO>> getProvinceMapData() {
        return Result.success(sysIndexService.getProvinceMapData());
    }

    @GetMapping("/city")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<List<ProvinceCityMapVO>> getCityMapData(
            @NotBlank(message = "城市参数不得为空") @RequestParam("icuCity") String icuCity
    ) {
        return Result.success(sysIndexService.getCityMapData(icuCity));
    }

    @GetMapping("/hosp")
    @RequiresPermissions("@ps.hasRolesAnd('user')")
    public Result<List<TbHospInfo>> getHospInfoData(
            @NotBlank(message = "城市参数不得为空") @RequestParam("icuCity") String icuCity
    ) {
        return Result.success(sysIndexService.getTbHospInfo(icuCity));
    }
}
