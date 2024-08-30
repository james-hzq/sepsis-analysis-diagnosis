package com.hzq.common.util.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * @author hua
 * @className com.hzq.common.validation ValidatorConfig
 * @date 2024/8/29 22:12
 * @description 参数校验配置
 */
@Configuration
public class ValidatorConfig {
    /**
     * @author hua
     * @date 2024/8/29 22:16
     * @return org.springframework.validation.beanvalidation.MethodValidationPostProcessor
     * @apiNote 配置方法级别验证器后处理器
     **/
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        postProcessor.setValidator(validator());
        return postProcessor;
    }

    /**
     * @return jakarta.validation.Validator
     * @author hua
     * @date 2024/8/29 22:14
     * @apiNote 配置验证器
     **/
    @Bean
    public Validator validator() {
        try (ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                // 启用快速失败模式，不用等全部的参数校验完，只要有错，马上抛出
                .addProperty("hibernate.validator.fail_fast", "true")
                .buildValidatorFactory()
        ) {
            return validatorFactory.getValidator();
        }
    }
}
