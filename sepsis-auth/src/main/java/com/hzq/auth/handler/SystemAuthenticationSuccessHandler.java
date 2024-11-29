package com.hzq.auth.handler;

import com.hzq.auth.login.user.SysUserDetail;
import com.hzq.redis.cache.RedisCache;
import com.hzq.security.authentication.LoginUserInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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

    private static final String TOKEN_TYPE_PREFIX = "JWT:";
    private static final Integer JWT_EXPIRE_SECONDS = 7200;

    private final JwtEncoder jwtEncoder;
    private final RedisCache redisCache;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
            SysUserDetail sysUserDetail = (SysUserDetail) usernamePasswordAuthenticationToken.getPrincipal();

            // 生成用户信息
            LoginUserInfo loginUserInfo = new LoginUserInfo()
                    .setLoginType(sysUserDetail.getLoginType())
                    .setUsername(sysUserDetail.getUsername())
                    .setRoles(sysUserDetail.getRoles());

            // 创建时间和过期时间
            Instant issuedAt = Instant.now();
            Instant expiresAt = issuedAt.plus(JWT_EXPIRE_SECONDS, ChronoUnit.SECONDS);

            // 构建 JWT Claims
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .subject(loginUserInfo.getUsername())
                    .claim("loginType", loginUserInfo.getLoginType())
                    .claim("roles", loginUserInfo.getRoles())
                    .issuedAt(issuedAt)
                    .expiresAt(expiresAt)
                    .build();

            // 使用 JwtEncoder 生成 JWT
            String jwtToken = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
            String redisKey = TOKEN_TYPE_PREFIX;

            log.info("The system login are successfully logged in, and the redis user information is stored below.");
        }
    }
}
