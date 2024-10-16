package com.hzq.auth.handler;

import com.hzq.auth.domain.LoginUser;
import com.hzq.core.result.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import java.util.Collections;
import java.util.Map;

/**
 * @author gc
 * @class com.hzq.auth.handler HzqAuthenticationSuccessHandler
 * @date 2024/10/12 16:58
 * @description 认证成功处理器
 */
public class HzqAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    // HttpMessageConverter 是 Spring MVC 提供的一个策略接口。可以将 HTTP请求转换为Java对象，将Java对象转换为HTTP响应
    private final HttpMessageConverter<Object> httpMessageConverter = new MappingJackson2HttpMessageConverter();
    // 当客户端请求成功获取访问令牌时，服务器会返回一个OAuth2AccessTokenResponse对象，其中包含了访问令牌的相关信息.
    // Converter<OAuth2AccessTokenResponse, Map<String, Object>> 将 OAuth2AccessTokenResponse对象转换为 Map<String, Object> 类型的转换器
    private final Converter<OAuth2AccessTokenResponse, Map<String, Object>> accessTokenResponseConverter = new DefaultOAuth2AccessTokenResponseMapConverter();

    /**
     * @param request 请求对象
     * @param response 响应对象
     * @param authentication 认证对象
     * @author gc
     * @date 2024/10/12 16:59
     * @apiNote 认证成功处理逻辑
     **/
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, {
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;

        // 获取访问令牌和刷新令牌
        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();

        // 获取额外的参数
        Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

        // 构建 OAuth2AccessTokenResponse 对象
        OAuth2AccessTokenResponse accessTokenResponse = OAuth2AccessTokenResponse
                .withToken(accessToken.getTokenValue())
                .tokenType(accessToken.getTokenType())
                .expiresIn(accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null ?
                        ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()) : 0
                )
                .refreshToken(refreshToken != null ? refreshToken.getTokenValue() : null)
                .additionalParameters(!CollectionUtils.isEmpty(additionalParameters) ? additionalParameters : Collections.emptyMap())
                .build();

        // 将 OAuth2AccessTokenResponse 对象转换为 Map<String, Object> 类型的参数集合
        Map<String, Object> tokenResponseParameters = accessTokenResponseConverter.convert(accessTokenResponse);

        // 将 HttpServletResponse 对象包装成 Spring 框架中的 ServerHttpResponse 的实现，以便后续的消息转换处理
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

        httpMessageConverter.write(
                Result.success(tokenResponseParameters),
                null,
                httpResponse
        );
    }
}
