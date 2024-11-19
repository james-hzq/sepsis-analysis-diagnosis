package com.hzq.auth.controller;

import com.hzq.auth.login.user.LoginUserInfo;
import com.hzq.core.result.Result;
import com.hzq.redis.cache.RedisCache;
import com.hzq.security.annotation.RequiresPermissions;
import com.hzq.security.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

    public Result<LoginUserInfo> getLoginUserInfo(HttpServletRequest request) {
        Authentication authentication = SecurityUtils.getAuthentication();
    }
}
