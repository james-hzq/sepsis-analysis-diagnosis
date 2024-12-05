package com.hzq.system.server.domain.vo;

import jakarta.persistence.Tuple;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author hua
 * @className com.hzq.system.server.domain.vo SysUserVO
 * @date 2024/10/4 17:27
 * @description 用于前端用户展示的对象
 */
@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
public class SysUserRoleVO {
    private Long userId;
    private String username;
    private String email;
    private String roleKey;
    private Set<String> roles;
    private Character status;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;

    public SysUserRoleVO(Tuple tuple) {
        this.userId =  tuple.get("user_id", Long.class);
        this.username = tuple.get("username", String.class);
        this.email = tuple.get("email", String.class);
        this.roles = Arrays.stream(tuple.get("roleKey", String.class).split(",")).collect(Collectors.toSet());
        this.status = tuple.get("status", Character.class);
        this.createTime = tuple.get("create_time", Date.class);
        this.createBy = tuple.get("create_by", String.class);
        this.updateTime = tuple.get("update_time", Date.class);
        this.updateBy = tuple.get("update_by", String.class);
    }
}
