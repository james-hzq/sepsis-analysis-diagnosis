package com.hzq.gateway.util;

import com.google.common.collect.ImmutableMap;
import com.hzq.core.result.Result;
import com.hzq.core.result.ResultEnum;
import com.hzq.jackson.util.JacksonUtils;
import jakarta.validation.constraints.NotNull;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

/**
 * @author hua
 * @className com.hzq.gateway.util WebFluxUtils
 * @date 2024/9/23 0:50
 * @description 基于 WebFlux 非阻塞的响应式 web 的异常响应工具类
 */
public class WebFluxUtils {

    // 定义响应处理策略函数
    @FunctionalInterface
    public interface ResponseStrategy {
        HttpStatus resolveStatus(ResultEnum resultEnum);
    }

    // 预定义常用响应映射, 不可变集合, 只读不写, 无线程安全问题
    private static final Map<ResultEnum, HttpStatus> STATUS_MAPPING = ImmutableMap.<ResultEnum, HttpStatus>builder()
            .put(ResultEnum.BAD_REQUEST_FORMAT, HttpStatus.BAD_REQUEST)
            .put(ResultEnum.BAD_REQUEST_METHOD, HttpStatus.BAD_REQUEST)
            .put(ResultEnum.TOKEN_INVALID_OR_EXPIRED, HttpStatus.UNAUTHORIZED)
            .put(ResultEnum.AUTHENTICATION_FAILED, HttpStatus.UNAUTHORIZED)
            .put(ResultEnum.ACCESS_FORBIDDEN, HttpStatus.FORBIDDEN)
            .build();

    // 默认响应策略
    private static final ResponseStrategy DEFAULT_STRATEGY = resultEnum -> Optional.ofNullable(STATUS_MAPPING.get(resultEnum)).orElse(HttpStatus.INTERNAL_SERVER_ERROR);

    /**
     * @param response 响应对象
     * @param resultEnum 结果集封装
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author hzq
     * @date 2024/11/26 14:36
     * @apiNote 高性能响应写入 - 主方法
     **/
    public static Mono<Void> writeResponse(@NotNull ServerHttpResponse response, @NotNull ResultEnum resultEnum) {
        return writeResponse(response, resultEnum, null, DEFAULT_STRATEGY);
    }

    /**
     * @param response 响应对象
     * @param resultEnum 结果集封装
     * @param errMsg 详细错误信息
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author hzq
     * @date 2024/11/26 14:36
     * @apiNote 高性能响应写入 - 主方法
     **/
    public static Mono<Void> writeResponse(@NotNull ServerHttpResponse response, @NotNull ResultEnum resultEnum, String errMsg) {
        return writeResponse(response, resultEnum, errMsg, DEFAULT_STRATEGY);
    }

    /**
     * @param response 响应对象
     * @param resultEnum 结果集合
     * @param strategy 响应策略
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author hzq
     * @date 2024/11/26 15:01
     * @apiNote 优化的响应体写入
     **/
    public static Mono<Void> writeResponse(
            @NotNull ServerHttpResponse response,
            @NotNull ResultEnum resultEnum,
            String errMsg,
            @NotNull ResponseStrategy strategy
    ) {
        return Mono.defer(() -> {
            HttpStatus httpStatus = strategy.resolveStatus(resultEnum);
            Result<?> result = new Result<>(resultEnum, errMsg);

            return Mono.fromSupplier(() -> Optional.ofNullable(JacksonUtils.toJsonString(result)).orElse(""))
                    // 使用并行调度器
                    .publishOn(Schedulers.parallel())
                    // 写入响应
                    .flatMap(resultStr -> writeResponseBody(response, httpStatus, resultStr));
        });
    }

    /**
     * @param response 响应对象
     * @param httpStatus 响应状态码
     * @param resultStr 结果字符串
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author hzq
     * @date 2024/11/26 15:11
     * @apiNote 写入响应内容至响应体
     **/
    private static Mono<Void> writeResponseBody(ServerHttpResponse response, HttpStatus httpStatus, String resultStr) {
        return Mono.fromSupplier(() -> {
                    // 配置响应头
                    configureResponseHeaders(response, httpStatus);
                    // 创建 DataBuffer 写入响应
                    return response.bufferFactory().wrap(resultStr.getBytes(StandardCharsets.UTF_8));
                })
                .flatMap(buffer ->
                        // 使用Flux提高写入性能，如果出现错误，释放 DataBuffer 内存
                        response.writeWith(Flux.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer))
                );
    }

    /**
     * @param response 响应对象
     * @param httpStatus HTTP状态码
     * @author hzq
     * @date 2024/11/26 15:07
     * @apiNote 响应头配置
     **/
    private static void configureResponseHeaders(ServerHttpResponse response, HttpStatus httpStatus) {
        // 设置响应的状态码
        response.setStatusCode(httpStatus);
        // 获取响应头
        HttpHeaders headers = response.getHeaders();
        // 设置响应内容格式
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 设置跨域访问
        headers.setAccessControlAllowOrigin("*");
        // 设置不使用缓存
        headers.setCacheControl("no-cache");
    }

    /**
     * @param response 响应对象
     * @param resultEnum 结果枚举
     * @return reactor.core.publisher.Mono<java.lang.Void>
     * @author hzq
     * @date 2024/11/26 15:17
     * @apiNote 性能分析和日志记录，带性能追踪的响应信号
     **/
    public static Mono<Void> writeResponseWithPerformanceTracking(@NotNull ServerHttpResponse response, @NotNull ResultEnum resultEnum) {
        long startTime = System.nanoTime();
        return writeResponse(response, resultEnum)
                .doOnTerminate(() -> {
                    long duration = (System.nanoTime() - startTime) / 1_000_000;
                    // 可以接入性能监控系统或日志
                    System.out.println("Response writing took " + duration + " ms");
                });
    }
}
