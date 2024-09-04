package com.hzq.api.entity.system;

import com.hzq.web.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hua
 * @className com.hzq.api.entity.system SysMenu
 * @date 2024/8/31 9:56
 * @description 系统菜单实体类
 */

@Data
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
