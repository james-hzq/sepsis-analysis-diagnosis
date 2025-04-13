package com.hzq.system.server.service;

import com.google.common.base.Strings;
import com.hzq.system.server.dao.SysRoleDao;
import com.hzq.system.server.domain.dto.SysRoleForm;
import com.hzq.system.server.domain.entity.SysRole;
import com.hzq.system.server.domain.vo.SysRoleVO;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hua
 * @className com.hzq.system.server.service SysRoleService
 * @date 2024/10/1 15:59
 * @description 系统角色业务处理类
 */
@RequiredArgsConstructor
@Service
public class SysRoleService {

    private final SysRoleDao sysRoleDao;

    public List<SysRoleVO> list(SysRoleForm sysRoleForm) {
        // 动态添加查询条件
        Specification<SysRole> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!Strings.isNullOrEmpty(sysRoleForm.getRoleId()))
                predicates.add(criteriaBuilder.equal(root.get("roleId").as(Long.class), Long.parseLong(sysRoleForm.getRoleId())));
            if (!Strings.isNullOrEmpty(sysRoleForm.getRoleKey()))
                predicates.add(criteriaBuilder.like(root.get("roleKey").as(String.class), sysRoleForm.getRoleKey() + "%"));
            if (!Strings.isNullOrEmpty(sysRoleForm.getRoleName()))
                predicates.add(criteriaBuilder.like(root.get("roleName").as(String.class), sysRoleForm.getRoleName() + "%"));
            if (sysRoleForm.getStatus() != null && !Character.isWhitespace(sysRoleForm.getStatus()))
                predicates.add(criteriaBuilder.equal(root.get("status").as(Character.class), sysRoleForm.getStatus()));
            if (sysRoleForm.getStartTime() != null)
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime"), sysRoleForm.getStartTime()));
            if (sysRoleForm.getEndTime() != null)
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createTime"), sysRoleForm.getEndTime()));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        // 添加排序
        Sort sort = Sort.by("roleId").ascending();
        // 查询并结果集转换
        return sysRoleDao.findAll(spec, sort).stream()
                .map(SysRoleVO::new)
                .collect(Collectors.toList());
    }
}
