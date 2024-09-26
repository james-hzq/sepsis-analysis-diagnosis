package com.hzq.system.server.domain.dto;

import com.hzq.web.validation.ValidationInterface;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author gc
 * @class com.hzq.system.domain SysUserDTO
 * @date 2024/8/29 15:17
 * @description 用户表单传输对象
 */
@Data
@ToString
public class SysUserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "userId不得为空", groups = {
            ValidationInterface.delete.class,
            ValidationInterface.update.class,
    })
    private Long userId;

    @NotBlank(message = "用户昵称不得为空", groups = {
            ValidationInterface.add.class,
            ValidationInterface.update.class
    })
    @Length(min = 1, max = 20, message = "用户昵称长度必须在 1 ~ 20 个字符之间", groups = {
            ValidationInterface.add.class,
            ValidationInterface.update.class,
    })
    private String username;

    @NotBlank(message = "用户密码不得为空", groups = {
            ValidationInterface.add.class,
            ValidationInterface.update.class
    })
    @Length(min = 5, max = 16, message = "用户密码长度必须在 5 ~ 16 个字符之间", groups = {
            ValidationInterface.add.class,
            ValidationInterface.update.class,
    })
    private String password;

    @NotBlank(message = "邮箱不得为空", groups = {
            ValidationInterface.add.class,
            ValidationInterface.update.class
    })
    @Email(message = "邮箱格式无效", groups = {
            ValidationInterface.add.class,
            ValidationInterface.update.class,
    })
    private String email;

    @NotNull(message = "用户状态不得为空", groups = {
            ValidationInterface.add.class,
            ValidationInterface.update.class
    })
    private Character status;

    @NotBlank(message = "所属角色不得为空", groups = {
            ValidationInterface.add.class,
            ValidationInterface.update.class
    })
    private String roleKey;

    private Date startTime;
    private Date endTime;
}
