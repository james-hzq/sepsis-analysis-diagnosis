package com.hzq.diagnosis.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author hua
 * @className com.hzq.diagnosis.server HzqDiagnosisApplication
 * @date 2024/12/21 8:54
 * @description TODO
 */
@Slf4j
@ComponentScan(basePackages = "com.hzq")
@SpringBootApplication
public class HzqDiagnosisApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzqDiagnosisApplication.class, args);
        log.info("(●′ω`●) sepsis-diagnosis application running successfully (●′ω`●)");
    }
}
