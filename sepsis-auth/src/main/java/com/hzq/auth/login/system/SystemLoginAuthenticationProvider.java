package com.hzq.auth.login.system;

import com.hzq.core.result.ResultEnum;
import com.hzq.web.exception.SystemException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author hua
 * @className com.hzq.auth.login.system SystemLoginAuthenticationProvider
 * @date 2024/10/13 15:43
 * @description 系统用户密码登录授权身份验证提供者
 */
@Slf4j
@Setter
public class SystemLoginAuthenticationProvider implements AuthenticationProvider {
    // OAuth2令牌生成器
    private OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    // 注册客户端仓库
    private RegisteredClientRepository registeredClientRepository;
    // 授权信息存储服务
    private OAuth2AuthorizationService authorizationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SystemLoginAuthenticationToken systemLoginAuthenticationToken = (SystemLoginAuthenticationToken) authentication;

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) systemLoginAuthenticationToken.getPrincipal();

        // 从客户端仓库中取出签发认证的客户端
        RegisteredClient registeredClient = Optional.ofNullable(registeredClientRepository.findByClientId(SystemLoginAuthenticationProperty.REGISTERED_CLIENT_ID))
                .orElseThrow(() -> new SystemException(ResultEnum.SYSTEM_CLIENT_NOT_REGISTERED));

        // 验证客户端是否支持授权类型
        if (!registeredClient.getAuthorizationGrantTypes().contains(SystemLoginAuthenticationProperty.AUTH_TYPE)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }


        // 验证申请访问范围是否符合
        Set<String> containedScopes = registeredClient.getScopes();
        Set<String> requestedScopes = systemLoginAuthenticationToken.getScopes();
        Set<String> scopes = new HashSet<>();
        if (!CollectionUtils.isEmpty(requestedScopes)) {
            scopes = requestedScopes.stream()
                    .filter(containedScopes::contains)
                    .collect(Collectors.toSet());
        }
        if (CollectionUtils.isEmpty(scopes)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
        }

        systemLoginAuthenticationToken.setAuthenticated(true);

        // 访问令牌(Access Token) 构造器
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                // 设置注册的客户端信息
                .registeredClient(registeredClient)
                // 设置当前的主体（用户或系统）
                .principal(systemLoginAuthenticationToken)
                // 设置当前的授权服务器上下文
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                // 设置已授权的作用域（scopes）
                .authorizedScopes(scopes)
                // 设置授权类型
                .authorizationGrantType(SystemLoginAuthenticationProperty.AUTH_TYPE)
                // 设置当前的授权请求信息
                .authorizationGrant(systemLoginAuthenticationToken);

        // 生成访问令牌(Access Token)
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType((OAuth2TokenType.ACCESS_TOKEN)).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);

        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(),
                generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(),
                tokenContext.getAuthorizedScopes()
        );

        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(usernamePasswordAuthenticationToken.getName())
                .authorizationGrantType(SystemLoginAuthenticationProperty.AUTH_TYPE)
                .authorizedScopes(scopes)
                .attribute(Principal.class.getName(), usernamePasswordAuthenticationToken);

        // 生成刷新令牌(Refresh Token)
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN)) {
            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
            }
            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }

        return new OAuth2AccessTokenAuthenticationToken(
                registeredClient,
                usernamePasswordAuthenticationToken,
                accessToken,
                refreshToken,
                systemLoginAuthenticationToken.getAdditionalParameters()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SystemLoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
