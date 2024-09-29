package com.hzq.auth.domian;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @author hua
 * @className com.hzq.auth.domian.dto LoginDTO
 * @date 2024/9/28 9:49
 * @description 账户名和密码登录传输对象
 */
@Data
@ToString
@EqualsAndHashCode
public class LoginBody {
    @NotBlank(message = "用户昵称不得为空")
    @Length(min = 1, max = 20, message = "用户昵称长度必须在 1 ~ 20 个字符之间")
    private String username;

    @NotBlank(message = "用户密码不得为空")
    @Length(min = 5, max = 16, message = "用户密码长度必须在 5 ~ 16 个字符之间")
    private String password;

    @NotBlank(message = "验证码不得为空")
    private String code;
}
