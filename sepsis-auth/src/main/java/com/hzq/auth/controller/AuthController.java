package com.hzq.auth.controller;

import com.hzq.auth.oidc.user.LoginUserInfo;
import com.hzq.core.result.Result;
import com.hzq.redis.cache.RedisCache;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gc
 * @class com.hzq.auth.controller AuthController
 * @date 2024/11/19 14:48
 * @description TODO
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RedisCache redisCache;

    @GetMapping("/user/info")
    public Result<LoginUserInfo> getLoginUserInfo(HttpServletRequest request) {
        return Result.success();
    }
}
