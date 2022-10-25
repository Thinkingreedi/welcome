import { getBaseUrl, requestUtil, getUserProfile, getWxLogin } from '../utils/requestUtils';
import regeneratorRuntime, { async } from '../lib/runtime/runtime';
//提交订单
export const addOrderApi = (data) => {
	return new Promise((resolve, reject) => {
		requestUtil({
			url: '/order/submit',
			method: 'post',
			data: data
		}).then((res) => {
			console.log(res.data)
			resolve(res)
		}).catch((err) => {
			reject(err)
		})
	});
}
