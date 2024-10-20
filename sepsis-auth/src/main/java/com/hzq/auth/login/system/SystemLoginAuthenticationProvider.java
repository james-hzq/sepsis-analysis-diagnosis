package com.hzq.auth.login.system;

import com.hzq.auth.service.LoginUserService;
import com.hzq.core.result.ResultEnum;
import com.hzq.web.exception.SystemException;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.stereotype.Component;
import java.util.Optional;


/**
 * @author hua
 * @className com.hzq.auth.login.system SystemLoginAuthenticationProvider
 * @date 2024/10/13 15:43
 * @description 系统用户密码登录身份验证提供者
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
        log.error("进入");
        if (!(authentication instanceof SystemLoginAuthenticationToken)) return null;

        // 从客户端仓库中取出签发认证的客户端
        RegisteredClient registeredClient = Optional.ofNullable(registeredClientRepository.findByClientId("sepsis-web-client"))
                .orElseThrow(() -> new SystemException(ResultEnum.SYSTEM_CLIENT_NOT_REGISTERED));

        // 验证客户端是否支持授权类型
        if (!registeredClient.getAuthorizationGrantTypes().contains(SystemLoginAuthenticationToken.AUTH_TYPE)) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }


//        // 验证申请访问范围是否符合
//        Set<String> containedScopes = registeredClient.getScopes();
//        Set<String> requestedScopes = loginUser.getRoles();
//        Set<String> scopes = new HashSet<>();
//        if (!CollectionUtils.isEmpty(requestedScopes)) {
//            scopes = requestedScopes.stream()
//                    .filter(containedScopes::contains)
//                    .collect(Collectors.toSet());
//        }
//        if (!CollectionUtils.isEmpty(scopes)) {
//            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.INVALID_SCOPE);
//        }
//
//        // 设置认证成功
//        UsernamePasswordAuthenticationToken clientPrincipal = (UsernamePasswordAuthenticationToken) authentication.getPrincipal();
//        clientPrincipal.setAuthenticated(true);
//        authentication.setAuthenticated(true);
//
//        // 访问令牌(Access Token) 构造器
//        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
//                .registeredClient(registeredClient)
//                .principal(authentication)
//                // 身份验证成功的认证信息(用户名、权限等信息)
//                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
//                .authorizedScopes(scopes)
//                // 授权方式
//                .authorizationGrantType(SystemLoginAuthenticationToken.AUTH_TYPE)
//                // 授权具体对象
//                .authorizationGrant(authentication);
//
//        // 生成访问令牌(Access Token)
//        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType((OAuth2TokenType.ACCESS_TOKEN)).build();
//        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
//        if (generatedAccessToken == null) {
//            throw new OAuth2AuthenticationException(error);
//        }
//
//        OAuth2AccessToken accessToken = new OAuth2AccessToken(
//                OAuth2AccessToken.TokenType.BEARER,
//                generatedAccessToken.getTokenValue(),
//                generatedAccessToken.getIssuedAt(),
//                generatedAccessToken.getExpiresAt(),
//                tokenContext.getAuthorizedScopes()
//        );
//
//        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization.withRegisteredClient(registeredClient)
//                .principalName(usernamePasswordAuthentication.getName())
//                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
//                .authorizedScopes(authorizedScopes)
//                .attribute(Principal.class.getName(), usernamePasswordAuthentication); // attribute 字段

        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SystemLoginAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
