package com.hzq.auth.handler;

import com.hzq.core.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.DefaultOAuth2AccessTokenResponseMapConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * @author gc
 * @class com.hzq.auth.handler AuthenticationSuccessHandler
 * @date 2024/9/4 15:50
 * @description TODO
 */
public class HzqAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    // MappingJackson2HttpMessageConverter 是 Spring 提供的一个 HttpMessageConverter 实现，它将 Java 对象转换为 JSON 格式的 HTTP 响应，或者将 JSON 格式的 HTTP 请求转换为 Java 对象。
    private final HttpMessageConverter<Object> accessTokenHttpResponseConverter = new MappingJackson2HttpMessageConverter();
    // Converter 是 Spring 中的一个通用接口，用于将一种类型的对象转换为另一种类型的对象。此处作用是将 OAuth2AccessTokenResponse 对象转换为一个 Map<String, Object>
    private final Converter<OAuth2AccessTokenResponse, Map<String, Object>> accessTokenResponseParametersConverter = new DefaultOAuth2AccessTokenResponseMapConverter();

    /**
     * @param request 请求对象
     * @param response 响应对象
     * @param authentication Authentication 认证对象
     * @author gc
     * @date 2024/9/4 15:52
     * @apiNote 当身份验证成功时调用此方法。参数包括HTTP请求、响应对象和认证对象。
     **/
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 将传入的Authentication对象转换为OAuth2AccessTokenAuthenticationToken类型。该类型包含访问令牌、刷新令牌和其他认证信息。
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;
        // 从认证对象中获取访问令牌。
        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        // 从认证对象中获取刷新令牌。
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        // 获取附加参数，通常包括自定义的令牌信息。
        Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

        // 创建一个 OAuth2AccessTokenResponse.Builder 对象，初始化时传入访问令牌值和令牌类型。
        OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse
                .withToken(accessToken.getTokenValue())
                .tokenType(accessToken.getTokenType());

        // 如果令牌的发行时间和过期时间不为空，计算它们之间的秒数，并设置到响应构建器中。
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }

        // 如果刷新令牌不为空，将其值设置到响应构建器中。
        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }

        // 如果附加参数不为空，将它们设置到响应构建器中
        if (!CollectionUtils.isEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters);
        }

        // 使用构建器生成OAuth2AccessTokenResponse对象，包含所有设置好的令牌信息。
        OAuth2AccessTokenResponse accessTokenResponse = builder.build();

        // 将OAuth2AccessTokenResponse对象转换为一个包含响应参数的Map对象。
        Map<String, Object> tokenResponseParameters = this.accessTokenResponseParametersConverter.convert(accessTokenResponse);

        // 将响应写回客户端。
        this.accessTokenHttpResponseConverter.write(
                Result.success(tokenResponseParameters), null, new ServletServerHttpResponse(response)
        );
    }
}
