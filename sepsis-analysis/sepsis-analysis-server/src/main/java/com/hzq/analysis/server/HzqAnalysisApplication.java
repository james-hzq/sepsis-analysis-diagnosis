package com.hzq.analysis.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hua
 * @className com.hzq.analysis.server HzqAnalysisApplication
 * @date 2024/12/13 18:24
 * @description TODO
 */
@Slf4j
@ComponentScan(basePackages = "com.hzq")
@SpringBootApplication
public class HzqAnalysisApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzqAnalysisApplication.class, args);
        log.info("(●′ω`●) sepsis-analysis application running successfully (●′ω`●)");
    }
}
