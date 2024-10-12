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

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return context -> {
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) && context.getPrincipal() instanceof UsernamePasswordAuthenticationToken) {
                Optional.ofNullable(context.getPrincipal().getPrincipal()).ifPresent(principal -> {
                    JwtClaimsSet.Builder claims = context.getClaims();
                    // 系统用户添加自定义字段
                    if (principal instanceof LoginUser userDetails) {

                        claims.claim(LoginConstants.LOGIN_USER_ID, userDetails.getUserId());
                        claims.claim(LoginConstants.LOGIN_USER_NAME, userDetails.getUsername());

                        // 这里存入角色至JWT，解析JWT的角色用于鉴权的位置: ResourceServerConfig#jwtAuthenticationConverter
                        Set<String> roles = AuthorityUtils.authorityListToSet(context.getPrincipal().getAuthorities())
                                .stream()
                                .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
                        claims.claim(LoginConstants.LOGIN_USER_ROLES, roles);
                    }
                });
            }
        };
    }
}
