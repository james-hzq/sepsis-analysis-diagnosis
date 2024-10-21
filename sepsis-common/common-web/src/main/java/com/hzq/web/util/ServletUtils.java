package com.hzq.web.util;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import com.hzq.jackson.JacksonUtil;
import com.hzq.web.cache.request.CachedBodyHttpServletRequest;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author gc
 * @class com.hzq.web.util HttpUtils
 * @date 2024/10/11 14:37
 * @description 客户端工具类
 */
@Slf4j
public class ServletUtils {

    /**
     * @param name 请求的参数
     * @return java.lang.String
     * @author gc
     * @date 2024/10/11 15:03
     * @apiNote 获取请求中指定参数的值
     **/
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    /**
     * @param name 请求的参数
     * @param defaultValue 没用该参数的默认值
     * @return java.lang.String
     * @author gc
     * @date 2024/10/11 15:03
     * @apiNote 获取请求中指定参数的值，并在参数值为null时返回一个默认值。
     **/
    public static String getParameter(String name, String defaultValue) {
        return MoreObjects.firstNonNull(getParameter(name), defaultValue);
    }

    /**
     * @param name 请求的参数
     * @return java.lang.Integer
     * @author gc
     * @date 2024/10/11 15:05
     * @apiNote 获取请求中指定参数的值，并在参数值为null时返回一个默认值。
     **/
    public static Integer getParameterToInt(String name) {
        return Ints.tryParse(getRequest().getParameter(name));
    }

    /**
     * @param name 请求的参数
     * @param defaultValue 没用该参数的默认值
     * @return java.lang.Integer
     * @author gc
     * @date 2024/10/11 15:05
     * @apiNote 获取请求中指定参数的值，并在参数值为null时返回一个默认值。
     **/
    public static Integer getParameterToInt(String name, int defaultValue) {
        return MoreObjects.firstNonNull(getParameterToInt(name), defaultValue);
    }

    /**
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @author gc
     * @date 2024/10/11 14:55
     * @apiNote 获取所有的请求参数
     **/
    public static Map<String, String> getRequestParams(ServletRequest request) {
        Map<String, String> params = new HashMap<>();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            params.put(entry.getKey(), Joiner.on(",").join(entry.getValue()));
        }
        return params;
    }

    /**
     * @param request 请求对象
     * @param clazz 请求体解析后的类型
     * @return T
     * @author gc
     * @date 2024/10/16 11:12
     * @apiNote 从传入请求的请求体内反序列化为Java对象
     **/
    public static <T> T getRequestBody(HttpServletRequest request, Class<T> clazz) {
        try {
            if (!(request instanceof CachedBodyHttpServletRequest cachedRequest)) {
                throw new IllegalArgumentException("请求对象必须是 CachedBodyHttpServletRequest 的实例");
            }
            // 直接使用 CachedBodyHttpServletRequest 中的缓存请求体
            byte[] cachedBody = cachedRequest.getCachedBody();
            return JacksonUtil.parseObject(cachedBody, clazz);
        } catch (Exception e) {
            log.error("反序列化请求体出错：{}", e.getMessage(), e);
            throw new RuntimeException("反序列化请求体出错", e);
        }
    }

    /**
     * @return jakarta.servlet.http.HttpServletRequest
     * @author gc
     * @date 2024/10/11 14:46
     * @apiNote 获取当前请求的 HttpServletRequest 对象
     **/
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * @return jakarta.servlet.http.HttpServletResponse
     * @author gc
     * @date 2024/10/11 14:58
     * @apiNote 获取当前请求的 HttpServletResponse 对象
     **/
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /**
     * @return org.springframework.web.context.request.ServletRequestAttributes
     * @author gc
     * @date 2024/10/11 14:41
     * @apiNote 从Spring应用程序上下文中获取当前请求的ServletRequestAttributes对象。
     **/
    public static ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * @param request 请求对象
     * @param name 请求头参数名称
     * @return java.lang.String
     * @author gc
     * @date 2024/10/11 15:15
     * @apiNote 从传入的 HttpServletRequest 对象中获取指定名称的请求头，并对其进行 URL 解码。
     **/
    public static String getHeader(HttpServletRequest request, String name) {
        String value = request.getHeader(name);
        return Strings.isNullOrEmpty(value) ? "" : urlDecode(value);
    }

    /**
     * @param request 请求对象
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @author gc
     * @date 2024/10/11 15:20
     * @apiNote 从给定的 HttpServletRequest 对象中提取所有的请求头信息
     **/
    public static Map<String, String> getHeaders(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames()).stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                request::getHeader
                        )
                );
    }

    /**
     * @param str  待编码的 URL 字符串
     * @return java.lang.String
     * @author gc
     * @date 2024/10/11 15:13
     * @apiNote 使用 UTF-8 字符集对传入的字符串进行 URL 编码
     **/
    public static String urlEncode(String str) {
        return URLEncoder.encode(str, StandardCharsets.UTF_8);
    }

    /**
     * @param str URL 编码字符串
     * @return java.lang.String
     * @author gc
     * @date 2024/10/11 15:12
     * @apiNote 对传入的 URL 编码字符串进行解码，并使用 UTF-8 字符集进行解码。
     **/
    public static String urlDecode(String str) {
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }
}
