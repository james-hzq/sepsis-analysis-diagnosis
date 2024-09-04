package com.hzq.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author gc
 * @class com.hzq.gateway HzqGatewayApplication
 * @date 2024/9/3 11:25
 * @description TODO
 */
@ComponentScan(basePackages = "com.hzq")
@SpringBootApplication
public class HzqGatewayApplication {
    private static final Logger log = LoggerFactory.getLogger(HzqGatewayApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HzqGatewayApplication.class, args);
        log.info("HzqGatewayApplication start successfully (~_~)");
    }
}
