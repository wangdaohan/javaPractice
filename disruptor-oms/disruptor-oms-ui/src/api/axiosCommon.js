//专门处理网络接口



import router from '../router'
/**
 *
 * let a = {name: hello, age: 10}
 * qs.Stringfy(a)  -> name=hello&age=13
 */

import Qs from 'qs'

/**
 *包装了ajax,方便http 调用
 */
import axios from 'axios'


//通用公共方法(包含回调）
export const reqRealEndAsync = (method, baseUrl, url, params, callback) => {
    params.toekn = sessionStorage.getItem('token');
    return axios({
        timeout: 5000,
        baseURL: baseUrl,
        url: url,
        method: method,
        headers: {
            'Content-type':'application/x-www-form-urlencoded'
        },
        data: Qs.stringify(params),
        //默认是false -> 即axios会将data进行深度序列化，即将data里的全部数据 转换成 name=hello&age=13&age=13&age=13&age=13的方式在URL后面，这样后台获取参数就很不方便
        //true -> 后台是可以直接获取整个对象
        traditional: true,
    }).then(res => {
        //response里的data格式为： {code:, message:, data: { }}
        let result = res.data;
        if(result.code == 1) {
            //验证失败
            router.replace({
                path: "login",
                query: {
                    msg: result.message
                }
            })
        } else if(result.code == 0){
            //成功回调
            if (callback != undefined) {
                callback(result.code, result.message, result.data);
            }
        } else if(result.code == 2) {
            //验证失败
            if (callback != undefined) {
                callback(result.code, result.message, result.data);
            }
        }
        // else {
        //     console.error(result);
        // }
    });
};
//通用公共方法（不包含回调）
export const reqRealEnd = (method, baseUrl, url, params) => {
    return axios({
        timeout: 5000,
        baseURL: baseUrl,
        url: url,
        method: method,
        headers: {
            'Content-type':'application/x-www-form-urlencoded'
        },
        data: Qs.stringify(params),
        //默认是false -> 即axios会将data进行深度序列化，即将data里的全部数据 转换成 name=hello&age=13&age=13&age=13&age=13的方式在URL后面，这样后台获取参数就很不方便
        //true -> 后台是可以直接获取整个对象
        traditional: true,
    });
};