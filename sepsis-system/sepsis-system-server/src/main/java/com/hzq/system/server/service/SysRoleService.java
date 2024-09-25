package com.hzq.system.server.service;


import com.hzq.system.server.dao.SysRoleDao;
import com.hzq.system.server.domain.dto.SysRoleDTO;
import com.hzq.system.server.domain.entity.SysRole;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gc
 * @class com.hzq.system.service SysRoleService
 * @date 2024/8/30 16:50
 * @description TODO
 */
@Service
public class SysRoleService extends BaseService {
    private final SysRoleDao sysRoleDao;

    @Autowired
    public SysRoleService(SysRoleDao sysRoleDao) {
        this.sysRoleDao = sysRoleDao;
    }

    public List<SysRole> list(SysRoleDTO sysRoleDTO) {
            Specification<SysRole> specification = (root, query, cb) -> {
            // 添加动态条件
            List<Predicate> predicateList = new ArrayList<>();
            if (sysRoleDTO.getRoleId() != null)
                predicateList.add(cb.equal(root.get("roleId").as(Long.class), sysRoleDTO.getRoleId()));
            if (sysRoleDTO.getRoleKey() != null && !sysRoleDTO.getRoleKey().isEmpty())
                predicateList.add(cb.like(root.get("roleKey").as(String.class), "%" + sysRoleDTO.getRoleKey() + "%"));
            if (sysRoleDTO.getRoleName() != null && !sysRoleDTO.getRoleName().isEmpty())
                predicateList.add(cb.like(root.get("roleName").as(String.class), "%" + sysRoleDTO.getRoleName() + "%"));

            // 公共动态条件查询
            selectCommonCondition(root, cb, predicateList, sysRoleDTO.getStatus(), sysRoleDTO.getStartTime(), sysRoleDTO.getEndTime());

            //  查询条件使用 and 连接，转换为数组
            Predicate and = cb.and(predicateList.toArray(new Predicate[0]));

            List<Order> orders = new ArrayList<>() {{
                add(cb.asc(root.get("roleId")));
            }};
            return query.where(and).orderBy(orders).getRestriction();
        };
        return sysRoleDao.findAll(specification);
    }

    public List<String> findRoleKeyByRoleIds(List<Long> roleIds) {
        return sysRoleDao.findRoleKeyByRoleIds(roleIds);
    }
}
