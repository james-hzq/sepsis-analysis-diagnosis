package com.hzq.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author gc
 * @class com.hzq.auth HzqAuthApplication
 * @date 2024/9/2 14:28
 * @description TODO
 */
@EnableFeignClients(basePackages = "com.hzq.api.feign")
@ComponentScan(basePackages = "com.hzq")
@SpringBootApplication
public class HzqAuthApplication {
    private static final Logger log = LoggerFactory.getLogger(HzqAuthApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HzqAuthApplication.class, args);
        log.info("HzqAuthApplication start successfully (~_~)");
    }
}
