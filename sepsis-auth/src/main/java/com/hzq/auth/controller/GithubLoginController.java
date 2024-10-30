package com.hzq.auth.controller;

import com.hzq.auth.login.github.GithubLoginClient;
import com.hzq.core.result.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author gc
 * @class com.hzq.auth.controller GithubLoginController
 * @date 2024/10/22 17:31
 * @description TODO
 */
@Slf4j
@AllArgsConstructor
@RestController
public class GithubLoginController {

    @GetMapping("/login/oauth2/code/github")
    public Result<String> githubCallback(@RequestParam("code") String code, @RequestParam("state") String state) {
        log.info("收到 GitHub 授权码: {}, 收到 Github 状态: {}", code, state);
        return Result.success();
    }
}
