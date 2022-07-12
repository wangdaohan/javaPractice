import {reqRealEndAsync, reqRealEnd} from './axiosCommon'
import {config} from './frontConfig'

/**
 * 请求验证码
 */
export const queryCaptcha = (callback) => {
    return reqRealEndAsync("post", config.real_domain, "/login/captcha", {}, callback);
}

/**
 * 登录接口
 */
export const login = (params, callback) => {
    return reqRealEndAsync("post", config.real_domain, "/login/userlogin", params, callback);
}