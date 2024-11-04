package com.hzq.web.cache.request;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.io.*;

/**
 * @author gc
 * @class com.hzq.web.cache.request CachedBodyHttpServletRequest
 * @date 2024/10/21 9:07
 * @description 解决 Springboot 中 HttpServletRequest 请求体只能读取一次的问题（本质上是因为流读取到了末尾）。
 */
@Getter
@Slf4j
public class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {

    private final byte[] cachedBody;

    public CachedBodyHttpServletRequest(HttpServletRequest request) {
        super(request);
        try (InputStream requestInputStream = request.getInputStream()) {
            this.cachedBody = StreamUtils.copyToByteArray(requestInputStream);
        } catch (IOException e) {
            log.error("从请求中读取请求体出现异常 {}", e.getMessage());
            throw new RuntimeException("Failed to read request body", e);
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachedBodyServletInputStream(this.cachedBody);
    }

    @Override
    public BufferedReader getReader() {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(this.cachedBody);
        InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
        return new BufferedReader(inputStreamReader);
    }
}
