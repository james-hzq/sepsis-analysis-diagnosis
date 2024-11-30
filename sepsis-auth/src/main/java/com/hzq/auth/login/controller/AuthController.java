package com.hzq.auth.login.controller;

import com.hzq.auth.login.info.LoginUserInfoStrategyFactory;
import com.hzq.auth.login.info.TokenType;
import com.hzq.core.result.Result;
import com.hzq.core.result.ResultEnum;
import com.hzq.security.authentication.LoginUserInfo;
import com.hzq.security.constant.SecurityConstants;
import com.hzq.web.exception.SystemException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * @author gc
 * @class com.hzq.auth.controller AuthController
 * @date 2024/11/19 14:48
 * @description 授权服务器白名单请求处理器
 */
@RestController
@RequestMapping("/auth/user")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUserInfoStrategyFactory loginUserInfoStrategyFactory;

    @PostMapping("/info")
    public Result<LoginUserInfo> getLoginUserInfo(HttpServletRequest request) {
        // 从请求头解析 token
        String token = Optional.ofNullable(getToken(request))
                .orElseThrow(() -> new SystemException(ResultEnum.TOKEN_INVALID_OR_EXPIRED));
        TokenType tokenType = Optional.ofNullable(TokenType.getTokenType(token))
                .orElseThrow(() -> new SystemException(ResultEnum.TOKEN_INVALID_OR_EXPIRED));
        // 根据 tokenType 使用对应的策略进行用户信息提取
        LoginUserInfo loginUserInfo = Optional.ofNullable(loginUserInfoStrategyFactory.getStrategy(tokenType).getLoginUserInfo(token))
                .orElseThrow(() -> new SystemException(ResultEnum.TOKEN_INVALID_OR_EXPIRED));
        return Result.success(loginUserInfo);
    }

    @GetMapping("/logout")
    public Result<Boolean> logout(HttpServletRequest request) {
        // 从请求头解析 token
        String token = Optional.ofNullable(getToken(request))
                .orElseThrow(() -> new SystemException(ResultEnum.TOKEN_INVALID_OR_EXPIRED));
        TokenType tokenType = Optional.ofNullable(TokenType.getTokenType(token))
                .orElseThrow(() -> new SystemException(ResultEnum.TOKEN_INVALID_OR_EXPIRED));
        // 根据 tokenType 使用对应的策略进行用户信息删除
        return Result.success(loginUserInfoStrategyFactory.getStrategy(tokenType).logout(token));
    }

    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.REQUEST_HEAD_AUTHENTICATION);
        // 如果 token 为空，则返回空字符串。
        if (!StringUtils.hasText(token) || !token.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            return null;
        }
        // 裁剪前缀
        return token.replaceFirst(SecurityConstants.TOKEN_PREFIX, "");
    }
}
