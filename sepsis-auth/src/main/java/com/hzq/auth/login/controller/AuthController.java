package com.hzq.auth.login.controller;

import com.hzq.core.result.Result;
import com.hzq.core.result.ResultEnum;
import com.hzq.jackson.util.JacksonUtils;
import com.hzq.redis.cache.RedisCache;
import com.hzq.security.authentication.LoginUserInfo;
import com.hzq.security.constant.SecurityConstants;
import com.hzq.web.exception.SystemException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author gc
 * @class com.hzq.auth.controller AuthController
 * @date 2024/11/19 14:48
 * @description 授权服务器白名单请求处理器
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RedisCache redisCache;

    @PostMapping("/user/info")
    public Result<LoginUserInfo> getLoginUserInfo(HttpServletRequest request) {
        String redisKey = Optional.ofNullable(getRedisUserInfoKey(request))
                .orElseThrow(() -> new SystemException(ResultEnum.TOKEN_INVALID_OR_EXPIRED));
        LoginUserInfo loginUserInfo = JacksonUtils.parseObject((String) redisCache.getCacheObject(redisKey), LoginUserInfo.class);
        return Result.success(loginUserInfo);
    }

    private String getRedisUserInfoKey(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.REQUEST_HEAD_AUTHENTICATION);
        // 如果 token 为空，则返回空字符串。
        if (!StringUtils.hasText(token) || !token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return null;
        }
        // 裁剪前缀
        return token.replaceFirst(SecurityConstants.TOKEN_PREFIX, "");
    }
}
