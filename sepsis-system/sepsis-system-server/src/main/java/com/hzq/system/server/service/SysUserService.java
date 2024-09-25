package com.hzq.system.server.service;

import com.hzq.system.server.dao.SysUserDao;
import com.hzq.system.server.domain.dto.SysUserDTO;
import com.hzq.system.server.domain.entity.SysUser;
import com.hzq.web.exception.SystemException;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author gc
 * @class com.hzq.system.server.service SysUserService
 * @date 2024/9/25 15:40
 * @description TODO
 */
@AllArgsConstructor
@Service
public class SysUserService extends BaseService {
    private SysUserDao sysUserDao;

    public SysUser selectByUsername(String username) {
        return sysUserDao.findSysUsersByUsername(username);
    }

    public List<SysUser> list(SysUserDTO sysUserDTO) {
        Specification<SysUser> specification = (root, query, cb) -> {
            // 添加动态条件
            List<Predicate> predicateList = new ArrayList<>();
            if (sysUserDTO.getUserId() != null)
                predicateList.add(cb.equal(root.get("userId").as(Long.class), sysUserDTO.getUserId()));
            if (sysUserDTO.getUsername() != null && !sysUserDTO.getUsername().isEmpty())
                predicateList.add(cb.like(root.get("username").as(String.class), "%" + sysUserDTO.getUsername() + "%"));
            if (sysUserDTO.getEmail() != null && !sysUserDTO.getEmail().isEmpty())
                predicateList.add(cb.equal(root.get("email").as(String.class), sysUserDTO.getEmail()));

            // 公共动态条件查询
            selectCommonCondition(root, cb, predicateList, sysUserDTO.getStatus(), sysUserDTO.getStartTime(), sysUserDTO.getEndTime());

            //  查询条件使用 and 连接，转换为数组
            Predicate and = cb.and(predicateList.toArray(new Predicate[0]));

            List<Order> orders = new ArrayList<>() {{
                add(cb.asc(root.get("userId")));
            }};
            return query.where(and).orderBy(orders).getRestriction();
        };
        return sysUserDao.findAll(specification);
    }

    public int insert(SysUserDTO sysUserDTO) {
        SysUser sysUser = Optional.ofNullable(sysUserDao.findSysUsersByUsername(sysUserDTO.getUsername()))
                .orElseThrow(() -> new SystemException("用户名称已经存在, 请重新创建用户"));
        // 判断系统中是否已经存在该username，如何存在，提示用户，请更换昵称
        // 将 SysUserDTO 转换为 SysUser 实体对象
        // 使用私钥 A 进行解密
        // 使用公钥 B 进行加密
        // 设置创建时间等信息
        // 将 sysUser 入库
        //
        return 0;
    }
}
