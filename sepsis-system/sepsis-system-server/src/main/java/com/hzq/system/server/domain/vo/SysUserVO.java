package com.hzq.system.server.domain.vo;

import com.hzq.system.server.domain.entity.SysUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * @author hua
 * @className com.hzq.system.server.domain.vo SysUserVO
 * @date 2024/10/4 17:27
 * @description 用于前端用户展示的对象
 */
@Data
@ToString
@EqualsAndHashCode
public class SysUserVO {
    private Long userId;
    private String username;
    private Character status;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;

    public SysUserVO() {}

    public SysUserVO(SysUser sysUser) {
        this.userId = sysUser.getUserId();
        this.username = sysUser.getUsername();
        this.status = sysUser.getStatus();
        this.createTime = sysUser.getCreateTime();
        this.createBy = sysUser.getCreateBy();
        this.updateTime = sysUser.getUpdateTime();
        this.updateBy = sysUser.getUpdateBy();
    }
}
