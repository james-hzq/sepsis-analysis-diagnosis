package com.hzq.auth.config;

import com.hzq.auth.model.LoginUser;
import com.hzq.core.constant.UserConstants;
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

/**
 * @author gc
 * @class com.hzq.auth.config JwtTokenCustomizerConfig
 * @date 2024/9/4 10:49
 * @description JWT（JSON Web Token）生成器配置
 */
@Configuration
public class JwtTokenCustomizerConfig {
    /**
     * @return org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer<org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext>
     * @author gc
     * @date 2024/9/4 10:49
     * @apiNote 自定义 JWT 生产器
     **/
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
        return context -> {
            // 当生成的令牌是"access_token"，并且这个访问令牌是通过用户名和密码登录的用户生成的，往下执行
            if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType()) && context.getPrincipal() instanceof UsernamePasswordAuthenticationToken) {
                Optional.ofNullable(context.getPrincipal().getPrincipal()).ifPresent(principal -> {
                    // 获取 JWT 的声明集合构建器
                    JwtClaimsSet.Builder claims = context.getClaims();
                    // 检查当前登录的用户对象（称为 principal）是不是我们自定义的 LoginUser 类型。
                    if (principal instanceof LoginUser userDetails) {
                        // 添加用户 ID 到 JWT 声明中
                        claims.claim(UserConstants.USER_ID, userDetails.getUserId());
                        // 添加用户名到 JWT 声明中
                        claims.claim(UserConstants.USERNAME, userDetails.getUsername());
                        // 将角色集合转换为不可修改的集合
                        Set<String> authorities = AuthorityUtils.authorityListToSet(context.getPrincipal().getAuthorities())
                                .stream()
                                .collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
                        // 将用户的角色存入 JWT 声明中，角色信息用于鉴权配置
                        claims.claim(UserConstants.AUTHORITIES, authorities);
                    }
                });
            }
        };
    }
}
