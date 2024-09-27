package com.hzq.system.server.domain.entity;

import com.hzq.web.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hua
 * @className com.hzq.system.server.domain.entity SysMenu
 * @date 2024/9/26 16:36
 * @description 系统菜单实体类
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "sys_menu")
public class SysMenu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long menuId;
    private String menuName;
    private Long parentId;
    @Transient
    private String parentName;
    private String path;
    private String component;
    private String query;
    private Character isFrame;
    private Character menuType;
    private Character status;
    private String perms;
    private String icon;
    @Transient
    private List<SysMenu> children = new ArrayList<>();
}
