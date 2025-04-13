package com.hzq.system.server;

import com.hzq.core.util.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author hua
 * @className com.hzq.system.server HzqSystemApplication
 * @date 2024/9/26 11:12
 * @description TODO
 */
@Slf4j
@ComponentScan(basePackages = "com.hzq")
@SpringBootApplication
public class HzqSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(HzqSystemApplication.class, args);
        log.info("(●′ω`●) sepsis-system application running successfully (●′ω`●)");
    }
}
