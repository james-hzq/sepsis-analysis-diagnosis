package com.hzq.gateway.util;

import com.hzq.core.result.Result;
import com.hzq.core.result.ResultEnum;
import com.hzq.jackson.JacksonUtil;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author hua
 * @className com.hzq.gateway.util WebFluxUtils
 * @date 2024/9/23 0:50
 * @description 基于 WebFlux 非阻塞的响应式 web 的异常响应工具类
 */
public class WebFluxUtils {
    public static Mono<Void> writeResponse(ServerHttpResponse response, ResultEnum resultEnum) {
        HttpStatus httpStatus;
        switch (resultEnum) {
            case ACCESS_UNAUTHORIZED, TOKEN_INVALID_OR_EXPIRED -> httpStatus = HttpStatus.UNAUTHORIZED;
            case ACCESS_FORBIDDEN -> httpStatus = HttpStatus.FORBIDDEN;
            default -> httpStatus = HttpStatus.BAD_REQUEST;
        }
        return webFluxResponseWriter(response, MediaType.APPLICATION_JSON_VALUE, httpStatus, resultEnum);
    }

    /**
     * 设置webflux模型响应
     *
     * @param response    ServerHttpResponse
     * @param contentType content-type
     * @param status      http状态码
     * @param resultEnum  响应状态
     * @return Mono<Void>
     */
    public static Mono<Void> webFluxResponseWriter(ServerHttpResponse response, String contentType, HttpStatus status, ResultEnum resultEnum) {
        // 设置响应的状态码
        response.setStatusCode(status);
        // 添加响应的内容类型到响应头
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, contentType);
        // 设置跨域访问
        response.getHeaders().set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
        // 设置不使用缓存
        response.getHeaders().set(HttpHeaders.CACHE_CONTROL, "no-cache");
        // 创建 Result 对象，封装响应的状态码和内容
        Result<?> result = new Result<>(resultEnum);
        // 将 Result 对象转换为 Json 字符串
        String resultStr = Optional.ofNullable(JacksonUtil.toJsonString(result)).orElse("");
        // 生成 DataBuffer
        DataBuffer buffer = response.bufferFactory().wrap(resultStr.getBytes());
        // 写入 DataBuffer 到响应中
        return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
    }
}
