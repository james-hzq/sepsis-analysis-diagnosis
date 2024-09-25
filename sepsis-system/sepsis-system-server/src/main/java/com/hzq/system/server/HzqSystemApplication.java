package com.hzq.system.server;

import lombok.extern.slf4j.Slf4j;
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
//@EnableJpaRepositories(basePackages = {"com.hzq.system.server.dao"})
//@EntityScan(basePackages = {"com.hzq.system.server.domain.entity"})
@Slf4j
@ComponentScan(basePackages = "com.hzq")
@SpringBootApplication
public class HzqSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(HzqSystemApplication.class, args);
        log.info("◠‿◠ HzqSystemApplication start successfully ◠‿◠");
    }
}
