package com.hzq.auth.login.info;

import com.hzq.security.authentication.LoginUserInfo;

/**
 * @author hua
 * @className com.hzq.auth.login.info LoginUserInfoStrategy
 * @date 2024/11/30 15:03
 * @description 获取用户信息策略接口
 */
public interface LoginUserInfoStrategy {

    /**
     * @author hua
     * @date 2024/11/30 15:04
     * @return com.hzq.auth.login.info.TokenType
     * @apiNote 获取当前转换策略锁认证的令牌类型
     **/
    TokenType getTokenType();

    /**
     * @author hua
     * @date 2024/11/30 15:05
     * @return com.hzq.security.authentication.LoginUserInfo
     * @apiNote 获取用户信息
     **/
    LoginUserInfo getLoginUserInfo(String token);

    /**
     * @author hua
     * @date 2024/11/30 20:51
     * @apiNote 登录
     **/
    boolean logout(String token);
}
