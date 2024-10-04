package com.hzq.system.server.service;

import com.google.common.base.Strings;
import com.hzq.core.result.ResultEnum;
import com.hzq.system.dto.SysUserDTO;
import com.hzq.system.server.dao.SysUserDao;
import com.hzq.system.server.dao.SysUserRoleDao;
import com.hzq.system.server.domain.dto.SysUserForm;
import com.hzq.system.server.domain.entity.SysUser;
import com.hzq.system.server.domain.vo.SysUserVO;
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
 * @author hua
 * @className com.hzq.system.server.service SysUserService
 * @date 2024/9/26 14:43
 * @description TODO
 */
@AllArgsConstructor
@Service
public class SysUserService extends SysBaseService {
    private final SysUserDao sysUserDao;
    private final SysUserRoleDao sysUserRoleDao;

    public SysUserDTO selectSysUserByUsername(String username) {
        // 从数据库根据用户名查询出 SysUser 实体对象
        SysUser sysUser = Optional.ofNullable(sysUserDao.findSysUserByUsername(username))
                .orElseThrow(() -> new SystemException("用户不存在"));
        // 将实体对象转换为系统用户传输对象
        return new SysUserDTO()
                .setUserId(sysUser.getUserId())
                .setUsername(sysUser.getUsername())
                .setPassword(sysUser.getPassword())
                .setEmail(sysUser.getEmail())
                .setAvatar(sysUser.getAvatar())
                .setStatus(sysUser.getStatus())
                .setDelFlag(sysUser.getDelFlag());
    }

    public List<SysUserVO> list(SysUserForm sysUserForm) {
        Specification<SysUser> specification = (root, query, cb) -> {
            //用于添加所有查询条件
            List<Predicate> predicateList = new ArrayList<>();
            if (!Strings.isNullOrEmpty(sysUserForm.getUsername()))
                predicateList.add(cb.equal(root.get("username").as(String.class), sysUserForm.getUsername()));
            selectCommonCondition(root, cb, predicateList, sysUserForm.getStatus(), sysUserForm.getStartTime(), sysUserForm.getEndTime());
            // 设置查询条件
            Predicate and = cb.and(predicateList.toArray(new Predicate[0]));
            query.where(and);
            //设置排序
            List<Order> orders = new ArrayList<>();
            orders.add(cb.asc(root.get("userId")));
            // 进行查询构建
            return query.orderBy(orders).getRestriction();
        };
        List<SysUser> sysUserList = sysUserDao.findAll(specification);
        List<SysUserVO> sysUserVOList = new ArrayList<>();
        sysUserList.forEach(sysUser -> sysUserVOList.add(new SysUserVO(sysUser)));
        return sysUserVOList;
    }
}
