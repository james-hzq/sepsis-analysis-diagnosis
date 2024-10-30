package com.hzq.auth.handler;

import com.hzq.auth.domain.LoginUser;
import com.hzq.core.result.Result;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.DefaultOAuth2AccessTokenResponseMapConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
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
@Component
@Slf4j
public class HzqAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final OAuth2AuthorizedClientService authorizedClientService;

    public HzqAuthenticationSuccessHandler(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }
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
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("成功");
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            // 获取授权客户端
            OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                    oauthToken.getAuthorizedClientRegistrationId(),
                    oauthToken.getName()
            );

            if (authorizedClient != null) {
                // 获取 access_token
                String accessToken = authorizedClient.getAccessToken().getTokenValue();

                // 返回 access_token 给前端
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"access_token\":\"" + accessToken + "\"}");
                response.getWriter().flush();
            }
        }
    }
}
