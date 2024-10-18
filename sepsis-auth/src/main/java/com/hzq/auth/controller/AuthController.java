package com.hzq.auth.controller;

import com.hzq.auth.domain.LoginBody;
import com.hzq.auth.domain.LoginUser;
import com.hzq.auth.domain.LoginUserInfo;
import com.hzq.auth.service.TokenGeneratorService;
import com.hzq.core.constant.LoginConstants;
import com.hzq.core.result.Result;
import com.hzq.core.result.ResultEnum;
import com.hzq.jackson.JacksonUtil;
import com.hzq.web.exception.SystemException;
import com.hzq.web.util.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author hua
 * @className com.hzq.auth.controller AuthController
 * @date 2024/9/28 9:35
 * @description 授权服务请求处理器
 */
@AllArgsConstructor
@RestController
@RequestMapping("/oauth2")
public class AuthController {
//    private final AuthenticationManager authenticationManager;
//    private final TokenGeneratorService tokenGeneratorService;
//
//    /**
//     * @author hua
//     * @date 2024/9/28 9:47
//     * @return com.hzq.core.result.Result<java.lang.String>
//     * @apiNote 走系统账户名和密码登录的请求
//     **/
    @PostMapping("/login")
    public Result<String> login(@Validated @RequestBody LoginBody loginBody) {
        System.out.println("进入方法");
//        String code = loginBody.getCode();
//        String username = loginBody.getUsername();
//        String password = loginBody.getPassword();
//        // TODO 验证码服务校验验证码
//        // 使用配置的认证器进行用户名密码认证, 认证成功返回用户的登录信息
//        Authentication authentication = new UsernamePasswordAuthenticationToken(username, password);
//        try {
//            authentication = authenticationManager.authenticate(authentication);
//        } catch (BadCredentialsException | UsernameNotFoundException e) {
//            throw new SystemException(ResultEnum.USERNAME_OR_PASSWORD_ERROR);
//        } catch (InsufficientAuthenticationException e) {
//            throw new SystemException(ResultEnum.ACCESS_UNAUTHORIZED);
//        } catch (DisabledException e) {
//            throw new SystemException(ResultEnum.USER_DISABLED);
//        } catch (InternalAuthenticationServiceException e) {
//            throw new SystemException("系统内部错误，引发认证异常");
//        } catch (AuthenticationServiceException e) {
//            throw new SystemException("系统外部错误，引发认证异常");
//        }
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        // Token生成服务生成 access_token
//        String accessToken = tokenGeneratorService.generateAccessToken(loginUser);
        return Result.success();
    }
}
