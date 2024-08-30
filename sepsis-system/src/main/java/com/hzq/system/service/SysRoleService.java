package com.hzq.system.service;

import com.hzq.api.domain.system.SysRoleDTO;
import com.hzq.api.entity.system.SysRole;
import com.hzq.system.dao.SysRoleDao;
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
public class SysRoleService implements BaseService {
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
            if (sysRoleDTO.getRoleKey() != null && !"".equals(sysRoleDTO.getRoleKey()))
                predicateList.add(cb.like(root.get("roleKey").as(String.class), "%" + sysRoleDTO.getRoleKey() + "%"));
            if (sysRoleDTO.getRoleName() != null && !"".equals(sysRoleDTO.getRoleName()))
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
}
