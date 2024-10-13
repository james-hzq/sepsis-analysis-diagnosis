package com.hzq.auth.config;

import com.hzq.auth.domain.LoginUser;
import com.hzq.core.constant.LoginConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class JwtTokenCustomizerConfig {

    /**
     * @author hua
     * @date 2024/10/13 9:15
     * @return org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer<org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext>
     * @apiNote 自定义 JWT 令牌
     **/
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return context -> {
            // 检查令牌类型和主体类型
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) &&
                    context.getPrincipal() instanceof UsernamePasswordAuthenticationToken
            ) {
                Optional.ofNullable(context.getPrincipal().getPrincipal()).ifPresent(principal -> {
                    JwtClaimsSet.Builder claims = context.getClaims();
                    // 如果主体是LoginUser类型，即系统用户登录，则添加自定义字段
                    if (principal instanceof LoginUser loginUser) {

                        claims.claim(LoginConstants.SYSTEM_LOGIN_USER_ID, loginUser.getUserId());
                        claims.claim(LoginConstants.SYSTEM_LOGIN_USER_NAME, loginUser.getUsername());

                        // 这里存入角色至JWT，解析JWT的角色用于鉴权的位置: ResourceServerConfig#jwtAuthenticationConverter
                        Set<String> roles = AuthorityUtils.authorityListToSet(context.getPrincipal().getAuthorities())
                                .stream()
                                .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
                        claims.claim(LoginConstants.SYSTEM_LOGIN_USER_ROLES, roles);
                    }
                });
            }
        };
    }
}
