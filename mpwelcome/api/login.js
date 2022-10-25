import { getBaseUrl, requestUtil, getUserProfile, getWxLogin } from '../utils/requestUtils';
import regeneratorRuntime, { async } from '../lib/runtime/runtime';
//TODO
export const loginoutApi = (data) => {
    return new Promise((resolve, reject) => {
        requestUtil({
            'url': '/user/loginout',
            'method': 'post',
            data: data
        }).then((res) => {
            console.log(res.data)
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    });
}

export const loginApi = (data) => {
    return new Promise((resolve, reject) => {
        requestUtil({
            'url': '/user/login',
            'method': 'post',
            data: data
        }).then((res) => {
            console.log(res.data)
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    });
}