package com.hzq.auth.controller;

import com.hzq.auth.domian.LoginBody;
import com.hzq.auth.service.LoginUserService;
import com.hzq.core.result.Result;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hua
 * @className com.hzq.auth.controller AuthController
 * @date 2024/9/28 9:35
 * @description 授权服务请求处理器
 */
@AllArgsConstructor
@RestController
@RequestMapping("/oauth")
public class AuthController {
    private final LoginUserService loginUserService;
    /**
     * @author hua
     * @date 2024/9/28 9:47
     * @return com.hzq.core.result.Result<java.lang.String>
     * @apiNote 走系统账户名和密码登录的请求
     **/
    @PostMapping("/login")
    public Result<String> login(@Validated @RequestBody LoginBody loginBody) {
        String code = loginBody.getCode();
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();
        //
        UserDetails userDetails = loginUserService.loadUserByUsername(username);
        return Result.success();
    }
}
