package com.hzq.auth.controller;

import com.hzq.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gc
 * @class com.hzq.auth.controller GithubLoginController
 * @date 2024/10/22 17:31
 * @description TODO
 */
@Slf4j
@RestController
@RequestMapping("/oauth2/github")
public class GithubLoginController {

    @GetMapping("/callback")
    public Result<String> githubLogin() {
        log.error("进入Github登录");
        return Result.success();
    }
}
