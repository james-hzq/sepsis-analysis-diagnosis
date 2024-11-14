package com.hzq.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
/**
 * @author hua
 * @className com.hzq.gateway HzqGatewayApplication
 * @date 2024/9/26 16:45
 * @description 网关服务启动类，网关的目的只有两个，拦截请求进行认证和路由转发
 */
@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "com.hzq")
public class HzqGatewayApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(HzqGatewayApplication.class, args);
        System.out.println(run);
        log.info("(●′ω`●) sepsis-gateway application running successfully (●′ω`●)");
    }
}
