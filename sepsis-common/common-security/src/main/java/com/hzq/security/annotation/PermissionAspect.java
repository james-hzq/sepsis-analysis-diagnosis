package com.hzq.security.annotation;

import com.hzq.core.result.Result;
import com.hzq.core.result.ResultEnum;
import com.hzq.security.service.IPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author gc
 * @class com.hzq.security.annotation PermissionAspect
 * @date 2024/11/18 17:18
 * @description RequiresPermissions 权限校验注解的AOP实现
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAspect {

    private final ApplicationContext applicationContext;
    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();

    /**
     * @author hzq
     * @date 2024/11/18 17:24
     * @apiNote 定义切入点
     **/
    @Pointcut("@annotation(com.hzq.security.annotation.RequiresPermissions)")
    public void pointCut() {

    }

    /**
     * @author hzq
     * @date 2024/11/18 17:24
     * @apiNote Advice（通知）的一种，切入点的方法体执行之前执行。
     **/
    @Before(value = "pointCut()")
    public void beforePointCut() {
        log.debug("拦截请求校验权限------开始");
    }

    /**
     * @author hzq
     * @date 2024/11/18 17:24
     * @apiNote Advice（通知）的一种，切入点的方法体执行之后执行。
     **/
    @After(value = "pointCut()")
    public void afterPointCut() {
        log.debug("拦截请求校验权限------结束");
    }

    /**
     * @param joinPoint 连接点，允许访问被拦截方法的信息
     * @return com.hzq.core.result.Result<?>
     * @author hzq
     * @date 2024/11/18 17:44
     * @apiNote 校验权限
     **/
    @Around(value = "pointCut()")
    public Result<?> checkPermissions(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取权限字符串，即校验条件
        RequiresPermissions requiresPermissions = getAnnotation(joinPoint);
        String condition = requiresPermissions.value();
        // 使用 SpEL 表达式解析器解析权限验证条件
        Expression expression = EXPRESSION_PARSER.parseExpression(condition);
        // 获取 Spring 容器中的 PermissionService Bean
        IPermissionService IPermissionService = applicationContext.getBean(IPermissionService.class);
        // 执行权限验证条件
        return Boolean.TRUE.equals(expression.getValue(IPermissionService, Boolean.class))
                ? (Result<?>) joinPoint.proceed()
                : Result.error(ResultEnum.ACCESS_FORBIDDEN);
    }

    /**
     * @param joinPoint 连接点，允许访问被拦截方法的信息
     * @return java.lang.String
     * @author hzq
     * @date 2024/11/18 17:44
     * @apiNote 获取注解上的权限字符串
     **/
    private static RequiresPermissions getAnnotation(ProceedingJoinPoint joinPoint) {
        // 获取方法签名
        MethodSignature methodSignature = Optional.ofNullable(
                joinPoint.getSignature() instanceof MethodSignature
                        ? ((MethodSignature) joinPoint.getSignature())
                        : null
                ).orElseThrow(() -> new IllegalArgumentException("JoinPoint must be a method execution"));

        // 获取方法上的 @RequiresPermissions 注解
        return methodSignature.getMethod().getAnnotation(RequiresPermissions.class);
    }
}
