package com.hzq.system.server.domain.dto;

import com.hzq.web.validation.ValidationInterface;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * @author hua
 * @className com.hzq.system.server.domain.dto SysUserForm
 * @date 2024/10/4 17:33
 * @description TODO
 */
@Data
@ToString
@EqualsAndHashCode
public class SysUserForm {
    @NotBlank(message = "用户名称不得为空", groups = {
            ValidationInterface.add.class,
            ValidationInterface.update.class
    })
    @Length(message = "用户名称长度必须在 1 ~ 20 个字符内", groups = {
            ValidationInterface.add.class,
            ValidationInterface.update.class
    })
    private String username;
    @NotBlank(message = "用户状态不得为空", groups = {
            ValidationInterface.add.class,
            ValidationInterface.update.class
    })
    private Character status;
    private Date startTime;
    private Date endTime;
}
