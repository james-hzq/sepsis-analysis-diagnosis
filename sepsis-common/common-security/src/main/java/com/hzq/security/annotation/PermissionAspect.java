package com.hzq.security.annotation;

import com.hzq.core.result.Result;
import com.hzq.core.result.ResultEnum;
import com.hzq.security.permission.PermissionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;
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

    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();
    private final ApplicationContext applicationContext;
    private final PermissionService permissionService;
    private StandardEvaluationContext standardEvaluationContext;

    /**
     * @author hua
     * @date 2024/12/3 14:39
     * @apiNote SpelExpressionParser 在进行bean解析时需要使用到 StandardEvaluationContext, 否则会报错：EL1057E: No bean resolver registered in the context to resolve access to bean 'ps'
     **/
    @PostConstruct
    public void init() {
        // 确保 ApplicationContext 完全加载后初始化 StandardEvaluationContext
        this.standardEvaluationContext = new StandardEvaluationContext(permissionService);
        this.standardEvaluationContext.setBeanResolver(new BeanFactoryResolver(applicationContext));
        log.info("PermissionAspect 中的 StandardEvaluationContext 注入了微服务的 ApplicationContext ");
    }

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
        String permissionCondition = requiresPermissions.value();
        // 使用 SpEL 表达式解析器解析权限验证条件
        Expression expression = EXPRESSION_PARSER.parseExpression(permissionCondition);
        // 执行权限验证条件
        return Boolean.TRUE.equals(expression.getValue(standardEvaluationContext, Boolean.class))
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
