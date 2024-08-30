package com.hzq.system.service;

import com.hzq.api.domain.system.SysUserDTO;
import com.hzq.api.entity.system.SysUser;
import com.hzq.common.exception.SystemException;
import com.hzq.system.dao.SysUserDao;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hua
 * @className com.hzq.system.service SysUserService
 * @date 2024/8/28 23:12
 * @description TODO
 */
@Service
public class SysUserService implements BaseService {
    private SysUserDao sysUserDao;

    @Autowired
    public void setSysUserDao(SysUserDao sysUserDao) {
        this.sysUserDao = sysUserDao;
    }

    public List<SysUser> list(SysUserDTO sysUserDTO) {
        Specification<SysUser> specification = (root, query, cb) -> {
            // 添加动态条件
            List<Predicate> predicateList = new ArrayList<>();
            if (sysUserDTO.getUserId() != null)
                predicateList.add(cb.equal(root.get("userId").as(Long.class), sysUserDTO.getUserId()));
            if (sysUserDTO.getUsername() != null && !"".equals(sysUserDTO.getUsername()))
                predicateList.add(cb.like(root.get("username").as(String.class), "%" + sysUserDTO.getUsername() + "%"));
            if (sysUserDTO.getEmail() != null && !"".equals(sysUserDTO.getEmail()))
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
        // 判断系统中是否已经存在该username，如何存在，提示用户，请更换昵称
         if (sysUserDao.findByUsername(sysUserDTO.getUsername()).isPresent())
             throw new SystemException("用户名称已经存在, 请重新创建用户");
        // 将 SysUserDTO 转换为 SysUser 实体对象
        // 使用私钥 A 进行解密
        // 使用公钥 B 进行加密
        // 设置创建时间等信息
        // 将 sysUser 入库
        //
        return 0;
    }
}
