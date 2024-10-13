package com.hzq.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hua
 * @className com.hzq.auth HzqAuthApplication
 * @date 2024/9/28 9:11
 * @description 授权服务启动入口，账户密码登录，第三方登录，授权再此处理
 */
@Slf4j
@EnableCaching
@EnableFeignClients(basePackages = {"com.hzq.system.api"})
@SpringBootApplication
@ComponentScan(basePackages = "com.hzq")
public class HzqAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(HzqAuthApplication.class, args);
        log.info("(●′ω`●) sepsis-auth application running successfully (●′ω`●)");
    }
}
