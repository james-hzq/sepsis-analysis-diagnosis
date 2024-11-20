package com.hzq.auth.oidc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.converter.ClaimConversionService;
import org.springframework.security.oauth2.core.converter.ClaimTypeConverter;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.*;
import java.util.function.Function;

/**
 * @author gc
 * @class com.hzq.auth.oidc.service GithubOidcUserService
 * @date 2024/11/20 14:38
 * @description 用户获取github用户信息的oidc服务
 */
@Slf4j
public class GithubOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    // 无效的用户信息响应
    private static final String INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response";

    // 默认的声明类型转换器，使用指定的默认转换规则
    private static final Converter<Map<String, Object>, Map<String, Object>> DEFAULT_CLAIM_TYPE_CONVERTER = new ClaimTypeConverter(
            createDefaultClaimTypeConverters()
    );

    // 客户端能够访问的 OIDC Scope（范围）
    private final Set<String> accessibleScopes = new HashSet<>(
            Arrays.asList(OidcScopes.PROFILE, OidcScopes.EMAIL, OidcScopes.ADDRESS, OidcScopes.PHONE)
    );

    // 处理 OAuth2 用户请求
    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService = new GithubOAuth2UserService();

    // 声明类型转换器的生成逻辑
    private final Function<ClientRegistration, Converter<Map<String, Object>, Map<String, Object>>> claimTypeConverterFactory = (clientRegistration) -> DEFAULT_CLAIM_TYPE_CONVERTER;

    /**
     * @return java.util.Map<java.lang.String,org.springframework.core.convert.converter.Converter<java.lang.Object,?>>
     * @author hzq
     * @date 2024/11/20 14:56
     * @apiNote 创建默认的声明类型转换器映射
     **/
    public static Map<String, Converter<Object, ?>> createDefaultClaimTypeConverters() {
        // 定义类型转换器
        Converter<Object, ?> booleanConverter = getConverter(TypeDescriptor.valueOf(Boolean.class));
        Converter<Object, ?> instantConverter = getConverter(TypeDescriptor.valueOf(Instant.class));
        // 创建存放声明类型转换器的映射
        Map<String, Converter<Object, ?>> claimTypeConverters = new HashMap<>();
        // 添加用于验证邮箱是否已验证的布尔转换器
        claimTypeConverters.put(StandardClaimNames.EMAIL_VERIFIED, booleanConverter);
        // 添加用于验证手机号是否已验证的布尔转换器
        claimTypeConverters.put(StandardClaimNames.PHONE_NUMBER_VERIFIED, booleanConverter);
        // 添加用于解析更新时间的时间戳转换器
        claimTypeConverters.put(StandardClaimNames.UPDATED_AT, instantConverter);
        return claimTypeConverters;
    }

    /**
     * @param targetDescriptor 要转换的源或目标类型的上下文描述符
     * @return org.springframework.core.convert.converter.Converter<java.lang.Object,?>
     * @author hzq
     * @date 2024/11/20 14:57
     * @apiNote 获取指定目标类型的转换器
     **/
    private static Converter<Object, ?> getConverter(TypeDescriptor targetDescriptor) {
        TypeDescriptor sourceDescriptor = TypeDescriptor.valueOf(Object.class);
        return (source) -> ClaimConversionService.getSharedInstance()
                .convert(source, sourceDescriptor, targetDescriptor);
    }

    /**
     * @param userRequest OIDC 用户请求
     * @return org.springframework.security.oauth2.core.oidc.user.OidcUser
     * @author hzq
     * @date 2024/11/20 15:16
     * @apiNote 加载 OIDC 用户信息
     **/
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(userRequest, "userRequest cannot be null");
        log.info("授权服务器携带 access_token 向 OAuth2 客户端请求用户信息");
        OidcUserInfo userInfo = null;
        if (this.shouldRetrieveUserInfo(userRequest)) {
            // 加载 OAuth2 用户信息
            OAuth2User oauth2User = this.oauth2UserService.loadUser(userRequest);
            // 提取声明信息
            Map<String, Object> claims = getClaims(userRequest, oauth2User);
            // 创建 OIDC 用户信息
            userInfo = new OidcUserInfo(claims);
            // 检查用户信息中是否包含主题
            if (userInfo.getSubject() == null) {
                OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE);
                throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
            }
            // 检查用户信息中的主题是否匹配 ID 令牌中的主题
            if (!userInfo.getSubject().equals(userRequest.getIdToken().getSubject())) {
                OAuth2Error oauth2Error = new OAuth2Error(INVALID_USER_INFO_RESPONSE_ERROR_CODE);
                throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
            }
        }
        // 创建用户的授权信息
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.add(new OidcUserAuthority(userRequest.getIdToken(), userInfo));
        // 获取用户访问令牌
        OAuth2AccessToken token = userRequest.getAccessToken();
        // 添加用户的权限
        for (String authority : token.getScopes()) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }

        // 获取用户信息
        return getUser(userRequest, userInfo, authorities);
    }

    /**
     * @param userRequest OIDC 用户请求
     * @param userInfo OIDC 用户信息
     * @param authorities 用户权限集合
     * @return org.springframework.security.oauth2.core.oidc.user.OidcUser
     * @author hzq
     * @date 2024/11/20 15:17
     * @apiNote 获取用户信息
     **/
    private OidcUser getUser(OidcUserRequest userRequest, OidcUserInfo userInfo, Set<GrantedAuthority> authorities) {
        ClientRegistration.ProviderDetails providerDetails = userRequest.getClientRegistration().getProviderDetails();
        String userNameAttributeName = providerDetails.getUserInfoEndpoint().getUserNameAttributeName();
        if (StringUtils.hasText(userNameAttributeName)) {
            return new DefaultOidcUser(authorities, userRequest.getIdToken(), userInfo, userNameAttributeName);
        }
        return new DefaultOidcUser(authorities, userRequest.getIdToken(), userInfo);
    }

    /**
     * @param userRequest 用户信息请求
     * @return boolean
     * @author hzq
     * @date 2024/11/20 15:00
     * @apiNote 判断是否需要从 UserInfo Endpoint 获取用户信息
     **/
    private boolean shouldRetrieveUserInfo(OidcUserRequest userRequest) {
        // 如果未提供 UserInfo Endpoint URI，则自动禁用
        ClientRegistration.ProviderDetails providerDetails = userRequest.getClientRegistration().getProviderDetails();
        if (!StringUtils.hasLength(providerDetails.getUserInfoEndpoint().getUri())) {
            return false;
        }
        // 只有在 Authorization Code 授权类型下，才会有可能需要访问 UserInfo Endpoint，因为这种模式会返回 Access Token。
        if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(userRequest.getClientRegistration().getAuthorizationGrantType())) {
            // 判断是否需要从 UserInfo Endpoint 检索用户信息
            return this.accessibleScopes.isEmpty()
                    || CollectionUtils.isEmpty(userRequest.getAccessToken().getScopes())
                    || CollectionUtils.containsAny(userRequest.getAccessToken().getScopes(), this.accessibleScopes);
        }
        return false;
    }

    /**
     * @param userRequest OIDC 用户请求
     * @param oauth2User OAuth2 用户
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @author hzq
     * @date 2024/11/20 15:10
     * @apiNote 从 OAuth2User 中提取声明信息
     **/
    private Map<String, Object> getClaims(OidcUserRequest userRequest, OAuth2User oauth2User) {
        // 创建一个转换器，将声明信息从一个Map转换为另一个Map
        Converter<Map<String, Object>, Map<String, Object>> converter = this.claimTypeConverterFactory.apply(userRequest.getClientRegistration());
        // 如果转换器不为空，则使用该转换器转换OAuth2用户的属性，否则使用默认的转换器转换OAuth2用户的属性
        return Objects.requireNonNullElse(converter, DEFAULT_CLAIM_TYPE_CONVERTER).convert(oauth2User.getAttributes());
    }
}
