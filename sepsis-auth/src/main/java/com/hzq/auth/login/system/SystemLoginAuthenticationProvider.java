package com.hzq.auth.login.system;

import com.hzq.auth.util.OAuth2AuthenticationProviderUtils;
import com.hzq.core.result.ResultEnum;
import com.hzq.web.exception.SystemException;
import lombok.Setter;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author hua
 * @className com.hzq.auth.login.system SystemLoginAuthenticationProvider
 * @date 2024/10/13 15:43
 * @description 系统用户密码登录身份验证提供者
 */
@Setter
public class SystemLoginAuthenticationProvider implements AuthenticationProvider {
    // 认证管理器
    private AuthenticationManager authenticationManager;
    // OAuth2令牌生成器
    private OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;
    // 注册客户端仓库
    private RegisteredClientRepository registeredClientRepository;
    // 授权信息存储服务
    private OAuth2AuthorizationService authorizationService;

    public SystemLoginAuthenticationProvider() {
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SystemLoginAuthenticationToken systemLoginAuthenticationToken = (SystemLoginAuthenticationToken) authentication;
        // 从身份验证令牌获取客户端主体
        OAuth2ClientAuthenticationToken clientPrincipal = OAuth2AuthenticationProviderUtils
                .getAuthenticatedClient(systemLoginAuthenticationToken);
        // 获取注册的客户端信息
        RegisteredClient registeredClient = Optional.ofNullable(registeredClientRepository.findByClientId("sepsis-web-client"))
                .orElseThrow(() -> new SystemException(ResultEnum.SYSTEM_CLIENT_NOT_REGISTERED));
        // 获取附加参数
        Map<String, Object> additionalParameters = systemLoginAuthenticationToken.getAdditionalParameters();
        String username = (String) additionalParameters.get(OAuth2ParameterNames.USERNAME);
        String password = (String) additionalParameters.get(OAuth2ParameterNames.PASSWORD);
        Set<String> scopes = systemLoginAuthenticationToken.getScopes();

        // 使用配置的认证器进行用户名密码认证, 认证成功返回用户的登录信息
        Authentication usernamePasswordAuthentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            usernamePasswordAuthentication = authenticationManager.authenticate(usernamePasswordAuthentication);
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new SystemException(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
        } catch (InsufficientAuthenticationException e) {
            throw new SystemException(ResultEnum.ACCESS_UNAUTHORIZED);
        } catch (DisabledException e) {
            throw new SystemException(ResultEnum.USER_DISABLED);
        } catch (InternalAuthenticationServiceException e) {
            throw new SystemException("系统内部错误，引发认证异常");
        } catch (AuthenticationServiceException e) {
            throw new SystemException("系统外部错误，引发认证异常");
        }

        // 验证申请访问范围(Scope)
        Set<String> authorizedScopes = registeredClient.getScopes();
        Set<String> requestedScopes = systemLoginAuthenticationToken.getScopes();
        requestedScopes.stream()
                .filter(scope -> !authorizedScopes.contains(scope))
                .findAny()
                .orElseThrow(() -> new SystemException(ResultEnum.SYSTEM_CLIENT_NO_SCOPE));

        // 访问令牌(Access Token) 构造器
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .principal(usernamePasswordAuthentication)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizationGrantType(SystemLoginAuthenticationToken.AUTH_TYPE)
                .authorizationGrant(systemLoginAuthenticationToken);
        // 构建Access Token
        OAuth2TokenContext accessTokenContext = tokenContextBuilder.tokenType((OAuth2TokenType.ACCESS_TOKEN)).build();
        OAuth2Token generatedAccessToken = Optional.ofNullable(tokenGenerator.generate(accessTokenContext))
                .orElseThrow(() -> new SystemException(ResultEnum.ACCESS_TOKEN_GENERATE_ERROR));

        // 构建访问令牌
        OAuth2AccessToken accessToken = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                generatedAccessToken.getTokenValue(),
                generatedAccessToken.getIssuedAt(),
                generatedAccessToken.getExpiresAt(),
                scopes
        );

        // 构建授权信息
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(usernamePasswordAuthentication.getName())
                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .authorizedScopes(scopes)
                .accessToken(accessToken);

        // 生成刷新令牌(Refresh Token)
        OAuth2TokenContext refreshTokenContext = tokenContextBuilder.tokenType((OAuth2TokenType.ACCESS_TOKEN)).build();
        OAuth2Token generatedRefreshToken = Optional.ofNullable(tokenGenerator.generate(refreshTokenContext))
                .orElseThrow(() -> new SystemException(ResultEnum.REFRESH_TOKEN_GENERATE_ERROR));


        OAuth2RefreshToken refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
        authorizationBuilder.refreshToken(refreshToken);

        OAuth2Authorization authorization = authorizationBuilder.build();
        authorizationService.save(authorization);

        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken, additionalParameters);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SystemLoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
