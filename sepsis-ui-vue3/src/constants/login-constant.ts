/**
 * @author hzq
 * @date 2024/11/8 10:21
 * @apiNote 定义登录类型，导出登录常量
 **/
export type LoginConstantsType = {
  readonly LOGIN_TYPE: 'login-type';
  readonly ACCESS_TOKEN: 'access-token';
  readonly REFRESH_TOKEN: 'refresh-token';
};

export const LoginConstants: LoginConstantsType = {
  LOGIN_TYPE: 'login-type',
  ACCESS_TOKEN: 'access-token',
  REFRESH_TOKEN: 'refresh-token',
};

export default LoginConstants;
