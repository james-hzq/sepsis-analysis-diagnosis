package com.hzq.core.constant;

/**
 * @author hua
 * @className com.hzq.common.constant UserConstants
 * @date 2024/8/31 10:39
 * @description 用户及登录信息常量
 */
public class UserConstants {
    /**
     * 用户ID字段
     */
    public static final String USER_ID = "userId";

    /**
     * 用户名字段
     */
    public static final String USERNAME = "username";

    /**
     * 用户权限字段
     */
    public static final String ROLE_KEY = "roleKey";

    /**
     * 用户名信息字段
     */
    public static final String DETAILS_USER_INFO = "login_user_info";

    /**
     * 授权信息字段
     */
    public static final String AUTHORIZATION_HEADER = "authorization";

    /**
     * 权限(角色Code)集合
     */
    public static final String AUTHORITIES = "authorities";

    /**
     * 请求来源
     */
    public static final String FROM_SOURCE = "from-source";

    /**
     * 内部请求
     */
    public static final String INNER = "inner";

    /**
     * 用户标识
     */
    public static final String USER_KEY = "user_key";

    /**
     * 登录用户
     */
    public static final String LOGIN_USER = "login_user";

    /**
     * 角色权限
     */
    public static final String ROLE_PERMISSION = "role_permission";

    /**
     * 正常状态
     */
    public static final Character STATUS_OK = '0';

    /**
     * 封禁状态
     */
    public static final Character STATUS_DISABLE = '1';

    /**
     * 删除状态
     */
    public static final Character STATUS_DELETE = '2';


    /**
     * 是否为系统默认（是）
     */
    public static final String YES = "Y";

    /**
     * 是否菜单外链（是）
     */
    public static final String YES_FRAME = "0";

    /**
     * 是否菜单外链（否）
     */
    public static final String NO_FRAME = "1";

    /**
     * 菜单类型（目录）
     */
    public static final String TYPE_DIR = "M";

    /**
     * 菜单类型（菜单）
     */
    public static final String TYPE_MENU = "C";

    /**
     * 菜单类型（按钮）
     */
    public static final String TYPE_BUTTON = "F";

    /**
     * Layout组件标识
     */
    public final static String LAYOUT = "Layout";

    /**
     * ParentView组件标识
     */
    public final static String PARENT_VIEW = "ParentView";

    /**
     * InnerLink组件标识
     */
    public final static String INNER_LINK = "InnerLink";

    /**
     * 校验返回结果码
     */
    public final static String UNIQUE = "0";
    public final static String NOT_UNIQUE = "1";
}
