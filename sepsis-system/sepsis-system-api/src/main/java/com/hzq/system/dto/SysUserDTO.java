package com.hzq.system.dto;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * @author gc
 * @class com.hzq.system.domain SysUserDTO
 * @date 2024/8/29 15:17
 * @description 系统用户传输对象
 */
@Data
@Accessors(chain = true)
@ToString
public class SysUserDTO {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private String avatar;
    private Character status;
    private Character delFlag;
}
