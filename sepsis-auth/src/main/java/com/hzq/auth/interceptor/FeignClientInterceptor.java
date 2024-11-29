package com.hzq.auth.interceptor;

import com.hzq.security.constant.SecurityConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author gc
 * @class com.hzq.auth.config.interceptor FeignClientInterceptor
 * @date 2024/11/29 14:16
 * @description feign 拦截配置
 */
@Slf4j
@Configuration
public class FeignClientInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        // 为 feign 内部请求，添加请求头
        template.header(SecurityConstants.REQUEST_HEAD_INTERNAL_TOKEN, "hzq");
    }
}
