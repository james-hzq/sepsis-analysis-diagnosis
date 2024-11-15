package com.hzq.auth.config.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.keygen.Base64StringKeyGenerator;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * @author gc
 * @class com.hzq.auth.config CustomAuthorizationRequestResolver
 * @date 2024/11/4 9:53
 * @description 配置 OAuth2AuthorizationRequest 的解析器
 */
@Slf4j
@Component
public final class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private static final String AUTHORIZATION_REQUEST_BASE_URI = "/oauth2/authorization";
    private static final String REGISTRATION_ID_URI_VARIABLE_NAME = "registrationId";
    private static final char PATH_DELIMITER = '/';
    private static final StringKeyGenerator DEFAULT_STATE_GENERATOR = new Base64StringKeyGenerator(Base64.getUrlEncoder());
    private static final StringKeyGenerator DEFAULT_SECURE_KEY_GENERATOR = new Base64StringKeyGenerator(Base64.getUrlEncoder().withoutPadding(), 96);
    private static final Consumer<OAuth2AuthorizationRequest.Builder> DEFAULT_PKCE_APPLIER = OAuth2AuthorizationRequestCustomizers.withPkce();
    private final Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer = (customizer) -> {
    };
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final AntPathRequestMatcher authorizationRequestMatcher;

    public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
        Assert.notNull(clientRegistrationRepository, "clientRegistrationRepository cannot be null");
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.authorizationRequestMatcher = new AntPathRequestMatcher(
                AUTHORIZATION_REQUEST_BASE_URI + "/{" + REGISTRATION_ID_URI_VARIABLE_NAME + "}"
        );
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        String registrationId = resolveRegistrationId(request);
        if (registrationId == null) {
            return null;
        }
        String redirectUriAction = getAction(request, "login");
        return resolve(request, registrationId, redirectUriAction);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String registrationId) {
        if (registrationId == null) {
            return null;
        }
        String redirectUriAction = getAction(request, "authorize");
        return resolve(request, registrationId, redirectUriAction);
    }

    /**
     * @param request 请求对象
     * @param registrationId 注册ID
     * @param redirectUriAction 重定向URI操作
     * @return org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
     * @author hzq
     * @date 2024/11/4 10:31
     * @apiNote 根据请求、注册ID和重定向URI操作解析OAuth2授权请求。
     **/
    private OAuth2AuthorizationRequest resolve(HttpServletRequest request, String registrationId, String redirectUriAction) {
        // 根据注册ID从客户端注册库中获取客户端注册信息
        ClientRegistration clientRegistration = Optional.ofNullable(this.clientRegistrationRepository.findByRegistrationId(registrationId))
                .orElseThrow(() -> new OAuth2AuthenticationException("客户端ID无效"));

        // 获取OAuth2AuthorizationRequest的构建器
        OAuth2AuthorizationRequest.Builder builder = getBuilder(clientRegistration);
        // 根据请求、客户端注册信息和重定向URI操作展开重定向URI
        String redirectUriStr = expandRedirectUri(request, clientRegistration, redirectUriAction);

        // 配置OAuth2AuthorizationRequest的各个属性
        builder.clientId(clientRegistration.getClientId())
                .authorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri())
                .redirectUri(redirectUriStr)
                .scopes(clientRegistration.getScopes())
                .state(DEFAULT_STATE_GENERATOR.generateKey());
        // @formatter:on

        // 自定义授权请求
        this.authorizationRequestCustomizer.accept(builder);

        return builder.build();
    }

    /**
     * @param request Http 请求对象
     * @return java.lang.String
     * @author gc
     * @date 2024/11/4 10:22
     * @apiNote 根据HttpServletRequest解析注册ID。
     **/
    private String resolveRegistrationId(HttpServletRequest request) {
        // 如果请求与授权请求匹配，则获取变量中的注册ID
        if (this.authorizationRequestMatcher.matches(request)) {
            return this.authorizationRequestMatcher.matcher(request)
                    .getVariables()
                    .get(REGISTRATION_ID_URI_VARIABLE_NAME);
        }
        return null;
    }

    /**
     * @param request HttpServletRequest对象
     * @param defaultAction 默认操作动作
     * @return java.lang.String
     * @author gc
     * @date 2024/11/4 10:26
     * @apiNote  请求中的操作动作或默认操作动作
     **/
    private String getAction(HttpServletRequest request, String defaultAction) {
        return Optional.ofNullable(request.getParameter("action")).orElse(defaultAction);
    }

    /**
     * @param clientRegistration 客户端
     * @return org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest.Builder
     * @author gc
     * @date 2024/11/4 10:55
     * @apiNote 获取OAuth2授权请求构建器
     **/
    private OAuth2AuthorizationRequest.Builder getBuilder(ClientRegistration clientRegistration) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.equals(clientRegistration.getAuthorizationGrantType())) {
            // 创建OAuth2AuthorizationRequest.Builder对象
            OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.authorizationCode()
                    // 添加属性，包括registration_id
                    .attributes(attrs -> attrs.put(OAuth2ParameterNames.REGISTRATION_ID, clientRegistration.getRegistrationId()));

            // 检查是否包含openid scope
            if (!CollectionUtils.isEmpty(clientRegistration.getScopes()) && clientRegistration.getScopes().contains(OidcScopes.OPENID)) {
                applyNonce(builder);
            }
            // 检查客户端认证方法是否为NONE，如果是则应用默认PKCE方法
            if (ClientAuthenticationMethod.NONE.equals(clientRegistration.getClientAuthenticationMethod())) {
                DEFAULT_PKCE_APPLIER.accept(builder);
            }
            return builder;
        }
        throw new IllegalArgumentException("Invalid Authorization Grant Type (" +
                clientRegistration.getAuthorizationGrantType().getValue() +
                ") for Client Registration with Id: " + clientRegistration.getRegistrationId()
        );
    }

    /**
     * @param builder OAuth2授权请求构建器
     * @author gc
     * @date 2024/11/4 10:52
     * @apiNote 为OAuth2授权请求构建器（OAuth2AuthorizationRequest.Builder）应用Nonce（一次性随机数）的功能
     **/
    private static void applyNonce(OAuth2AuthorizationRequest.Builder builder) {
        try {
            // 生成随机Nonce
            String nonce = DEFAULT_SECURE_KEY_GENERATOR.generateKey();
            // 创建Nonce的哈希值
            String nonceHash = createHash(nonce);

            builder
                    // 将Nonce添加到属性中
                    .attributes(attrs -> attrs.put(OidcParameterNames.NONCE, nonce))
                    // 将Nonce的哈希值添加到附加参数中
                    .additionalParameters(params -> params.put(OidcParameterNames.NONCE, nonceHash));
        } catch (NoSuchAlgorithmException ex) {
            // 捕获NoSuchAlgorithmException异常
        }
    }

    /**
     * @param value 数值
     * @return java.lang.String
     * @author gc
     * @date 2024/11/4 10:54
     * @apiNote 用于生成Nonce的哈希值，将给定的数值（value）使用SHA-256哈希算法进行哈希处理，并将结果通过Base64编码转换为字符串形式
     **/
    private static String createHash(String value) throws NoSuchAlgorithmException {
        // 使用SHA-256算法创建MessageDigest实例
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        // 对数值进行SHA-256哈希处理
        byte[] digest = md.digest(value.getBytes(StandardCharsets.US_ASCII));
        // 对哈希值进行Base64编码
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
    }

    /**
     * @param request 请求对象
     * @param clientRegistration 客户端对象
     * @param action 操作
     * @return java.lang.String
     * @author gc
     * @date 2024/11/4 10:58
     * @apiNote 根据请求的信息、客户端注册信息以及特定操作，扩展并构建一个重定向URI
     **/
    private static String expandRedirectUri(HttpServletRequest request, ClientRegistration clientRegistration, String action) {
        // 构建UriComponents对象，基于请求构建完整的URL
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(UrlUtils.buildFullRequestUrl(request))
                .replacePath(request.getContextPath())
                .replaceQuery(null)
                .fragment(null)
                .build();

        // 创建用于存储URI变量的Map
        Map<String, String> uriVariables = new HashMap<>();

        // 存储客户端ID
        uriVariables.put("registrationId", clientRegistration.getRegistrationId());
        // 获取URL的scheme（协议）
        uriVariables.put("baseScheme", Optional.ofNullable(uriComponents.getScheme()).orElse(""));
        // 获取URL的host（主机）
        uriVariables.put("baseHost", Optional.ofNullable(uriComponents.getHost()).orElse(""));
        // 获取URL的port（端口）
        uriVariables.put("basePort", (uriComponents.getPort() == -1) ? "" : ":" + uriComponents.getPort());
        // 获取URL的path（路径），确保path以/开头
        uriVariables.put("basePath",
                Optional.ofNullable(uriComponents.getPath())
                        .filter(StringUtils::hasLength)
                        .map(path -> path.charAt(0) != PATH_DELIMITER ? PATH_DELIMITER + path : path)
                        .orElse("")
        );
        // 获取URL的完整base URL
        uriVariables.put("baseUrl", uriComponents.toUriString());
        // 存储操作（action）
        uriVariables.put("action", Optional.ofNullable(action).orElse(""));

        // 根据URI模板替换变量并构建最终的重定向URI
        return UriComponentsBuilder.fromUriString(clientRegistration.getRedirectUri())
                .buildAndExpand(uriVariables)
                .toUriString();
    }
}
