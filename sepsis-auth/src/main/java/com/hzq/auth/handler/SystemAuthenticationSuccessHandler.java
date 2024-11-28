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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

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
            String redisKey = TOKEN_TYPE_PREFIX;

            log.info("The system login are successfully logged in, and the redis user information is stored below.");
        }
    }
}
