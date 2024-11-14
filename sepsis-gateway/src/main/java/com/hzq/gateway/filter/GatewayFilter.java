package com.hzq.gateway.filter;

import com.hzq.gateway.constant.TokenConstants;
import com.hzq.core.result.ResultEnum;
import com.hzq.gateway.util.WebFluxUtils;
import com.nimbusds.jose.JWSObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

/**
 * @author hua
 * @className com.hzq.gateway.config AuthFilter
 * @date 2024/9/16 22:31
 * @description 网关认证过滤器，设置最高优先级执行，为请求执行的第一个过滤器，执行完毕将请求传递给资源服务器安全配置
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GatewayFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 请求对象
        ServerHttpRequest request = exchange.getRequest();
        // 响应对象
        ServerHttpResponse response = exchange.getResponse();
        log.info("收到请求 {}", request.getURI());
        // 从请求头中获取 token
        String authorization = getAuthorization(request);
        // 如果 authorization 是空字符串，说明没有得到授权服务器授权，需要将请求转发到授权服务器进行授权
        if (authorization.isEmpty()) {
            return chain.filter(exchange);
        }

        log.info("请求 {}, authorization 不为 null or empty", request.getURI());

        String payload;
        try {
            // 如果 authorization 不是空字符串，获取 JWT 主体，并加入请求头，转发到其他微服务
            payload = JWSObject.parse(authorization).getPayload().toString();
            log.info("payload = {}", payload);
        } catch (ParseException e) {
            log.error("在请求路径 {} 上发生错误 {}", request.getURI(), ResultEnum.JWT_PARSE_ERROR.getMessage());
            return WebFluxUtils.writeResponse(response, ResultEnum.JWT_PARSE_ERROR);
        }

        request = exchange.getRequest()
                .mutate()
                .header(TokenConstants.LOGIN_USER_INFO_HEADER, URLEncoder.encode(payload, StandardCharsets.UTF_8))
                .build();

        exchange = exchange.mutate().request(request).build();
        return chain.filter(exchange);
    }

    /**
     * @author hua
     * @date 2024/9/22 18:43
     * @param request http请求
     * @return java.lang.String
     * @apiNote 获取请求头中的 Authorization 参数的 token 值
     **/
    private String getAuthorization(ServerHttpRequest request) {
        // 从请求头中获取 token
        String token = request.getHeaders().getFirst(TokenConstants.AUTHENTICATION);
        // 如果 token 为空，则返回空字符串。
        if (!StringUtils.hasText(token)) {
            return "";
        }
        // 裁剪前缀
        if (token.startsWith(TokenConstants.TOKEN_PREFIX)) {
            token = token.replaceFirst(TokenConstants.TOKEN_PREFIX, "");
        }
        return token;
    }
}
