package com.hzq.web.cache.request;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author gc
 * @class com.hzq.web.cache.request CachedBodyServletInputStream
 * @date 2024/10/21 9:13
 * @description 允许将缓存的请求主体作为输入流读取。
 */
public class CachedBodyServletInputStream extends ServletInputStream {

    private final InputStream cachedBodyInputStream;

    public CachedBodyServletInputStream(byte[] cachedBody) {
        this.cachedBodyInputStream = new ByteArrayInputStream(cachedBody);
    }

    @Override
    public int read() throws IOException {
        return cachedBodyInputStream.read();
    }

    @Override
    public boolean isFinished() {
        try {
            return cachedBodyInputStream.available() == 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
    }
}
