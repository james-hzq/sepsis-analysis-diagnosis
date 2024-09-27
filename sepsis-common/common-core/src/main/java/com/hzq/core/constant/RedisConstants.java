package com.hzq.core.constant;

/**
 * @author gc
 * @class com.hzq.core.constant RedisConstants
 * @date 2024/9/4 16:44
 * @description TODO
 */
public class RedisConstants {
    /**
     * 黑名单TOKEN Key前缀
     */
    public static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";

    /**
     * 图形验证码key前缀
     */
    public static final String CAPTCHA_CODE_PREFIX = "captcha_code:";

    /**
     * 登录短信验证码key前缀
     */
    public static final String LOGIN_SMS_CODE_PREFIX = "sms_code:login";

    /**
     * 注册短信验证码key前缀
     */
    public static final String REGISTER_SMS_CODE_PREFIX = "sms_code:register";


    /**
     * 角色和权限缓存前缀
     */
    public static final String ROLE_PERMS_PREFIX = "role_perms:";


    /**
     * JWT 密钥对(包含公钥和私钥)
     */
    public static final String JWK_SET_KEY = "jwk_set";
}
