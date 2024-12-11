package com.hzq.system.server.domain.dto;

import com.hzq.web.validation.ValidationInterface;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author hua
 * @className com.hzq.system.server.domain.dto SysUserForm
 * @date 2024/10/4 17:33
 * @description 用户 CRUD 表单
 */
@Data
@ToString
@EqualsAndHashCode
public class SysUserForm {

    @NotBlank(message = "当前操作用户不得为空", groups = {
            ValidationInterface.create.class,
            ValidationInterface.read.class,
            ValidationInterface.update.class,
            ValidationInterface.delete.class,
    })
    private String currUsername;

    @NotBlank(message = "用户编号不得为空", groups = {
            ValidationInterface.update.class,
            ValidationInterface.delete.class
    })
    private String userId;

    @NotBlank(message = "用户名称不得为空", groups = {
            ValidationInterface.create.class,
            ValidationInterface.update.class
    })
    @Length(message = "用户名称长度必须在 1 ~ 20 个字符内", min = 1, max = 20,
            groups = {
                    ValidationInterface.create.class,
                    ValidationInterface.update.class
            }
    )
    private String username;

    @NotBlank(message = "用户密码不得为空", groups = {
            ValidationInterface.create.class,
            ValidationInterface.update.class
    })
    @Length(message = "用户密码长度必须在 5 ~ 20 个字符内", min = 5, max = 20,
            groups = {
                    ValidationInterface.create.class,
                    ValidationInterface.update.class
            }
    )
    private String password;

    @NotBlank(message = "用户邮箱不得为空", groups = {
            ValidationInterface.create.class,
            ValidationInterface.update.class
    })
    @Email(message = "绑定邮箱格式错误", groups = {
            ValidationInterface.create.class,
            ValidationInterface.update.class
    })
    private String email;

    @NotEmpty(message = "当前操作用户不得为空", groups = {
            ValidationInterface.create.class,
            ValidationInterface.update.class,
    })
    private List<String> roles;

    @NotNull(message = "用户状态不得为空", groups = {
            ValidationInterface.create.class,
            ValidationInterface.update.class
    })
    private Character status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
