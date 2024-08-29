package com.hzq.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author hua
 * @className com.hua.system HzqSepsisSystemApplication
 * @date 2024/8/24 17:04
 * @description TODO
 */
@ComponentScan(basePackages = "com.hzq")
@EnableJpaRepositories(basePackages = {"com.hzq.system.dao"})
@EntityScan(basePackages = {"com.hzq.api.entity"})
@SpringBootApplication
public class HzqSystemApplication {
    private static final Logger log = LoggerFactory.getLogger(HzqSystemApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HzqSystemApplication.class, args);
        log.info("HzqSystemApplication start successfully (~_~)");
    }
}
