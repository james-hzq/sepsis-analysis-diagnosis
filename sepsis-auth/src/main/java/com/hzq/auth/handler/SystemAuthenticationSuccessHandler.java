package com.hzq.auth.handler;

import com.hzq.auth.config.token.CustomJwtGenerator;
import com.hzq.auth.login.info.TokenType;
import com.hzq.auth.login.user.SysUserDetail;
import com.hzq.core.result.Result;
import com.hzq.jackson.util.JacksonUtils;
import com.hzq.redis.cache.RedisCache;
import com.hzq.security.authentication.LoginUserInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * @author gc
 * @class com.hzq.auth.handler SystemAuthenticationSuccessHandler
 * @date 2024/11/28 17:38
 * @description 自定义系统用户名密码登录成功处理器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SystemAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String TOKEN_TYPE_PREFIX = TokenType.SYSTEM_LOGIN_JWT_TOKEN.getPrefix();
    private static final Integer JWT_EXPIRE_SECONDS = 10800;

    private final CustomJwtGenerator jwtGenerator;
    private final RedisCache redisCache;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
            SysUserDetail sysUserDetail = (SysUserDetail) usernamePasswordAuthenticationToken.getPrincipal();
            // 创建时间和过期时间
            Instant issuedAt = Instant.now();
            Instant expiresAt = issuedAt.plus(JWT_EXPIRE_SECONDS, ChronoUnit.SECONDS);
            Integer secondsDifference = (int) Duration.between(issuedAt, expiresAt).getSeconds();
            // 生成 JWT
            String jwtToken = jwtGenerator.createJwt(sysUserDetail.getUsername(), issuedAt, expiresAt);
            // 生成用户信息
            LoginUserInfo loginUserInfo = new LoginUserInfo()
                    .setLoginType(sysUserDetail.getLoginType())
                    .setUsername(sysUserDetail.getUsername())
                    .setRoles(sysUserDetail.getRoles())
                    .setIssuedAt(issuedAt)
                    .setExpiresAt(expiresAt);
            log.info("The system login are successfully logged in, and the redis user information is stored below.");
            // 生成 redis key，将用户信息存入至 redis
            String redisKey = TOKEN_TYPE_PREFIX + sysUserDetail.getUsername();
            redisCache.setCacheObject(redisKey, JacksonUtils.toJsonString(loginUserInfo), secondsDifference, TimeUnit.SECONDS);
            // 返回 JWT 给前端
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(JacksonUtils.toJsonString(Result.success(TOKEN_TYPE_PREFIX + jwtToken)));
        }
    }
}
