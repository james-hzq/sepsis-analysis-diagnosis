package com.hzq.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hua
 * @className com.hzq.auth HzqAuthApplication
 * @date 2024/9/28 9:11
 * @description TODO
 */
@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "com.hzq")
public class HzqAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(HzqAuthApplication.class, args);
        log.info("(●′ω`●) sepsis-auth application running successfully (●′ω`●)");
    }
}
