package com.hzq.auth.login.system;

import com.hzq.auth.domain.LoginUser;
import com.hzq.system.api.SysRoleFeignClient;
import com.hzq.system.api.SysUserFeignClient;
import com.hzq.system.api.SysUserRoleFeignClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;

/**
 * @author hua
 * @className com.hzq.auth.login.system SystemLoginAuthenticationProvider
 * @date 2024/10/13 15:43
 * @description 系统用户密码登录身份验证提供者
 */
@Slf4j
@Component
@AllArgsConstructor
public class SystemLoginAuthenticationProvider implements AuthenticationProvider {
    // 用户信息处理类
    private UserDetailsService userDetailsService;
    // OAuth2令牌生成器
    private OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    // 注册客户端仓库
    private RegisteredClientRepository registeredClientRepository;
    // 授权信息存储服务
    private OAuth2AuthorizationService authorizationService;

    @Autowired
    public void setTokenGenerator(OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof UsernamePasswordAuthenticationToken)) return null;

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        LoginUser userDetails = (LoginUser) userDetailsService.loadUserByUsername(username);

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
