import {
	getBaseUrl,
	requestUtil,
	getUserProfile,
	getWxLogin
} from '../utils/requestUtils';
import regeneratorRuntime, {
	async
} from '../lib/runtime/runtime';
//获取默认地址
export const getDefaultAddressApi = () => {
	return new Promise((resolve, reject) => {
		requestUtil({
			url: '/addressBook/default',
			method: 'get',
		}).then((res) => {
			console.log(res.data)
			resolve(res)
		}).catch((err) => {
			reject(err)
		})
	});
}
//设置默认地址
export const updateAddressApi = (data) => {
	return new Promise((resolve, reject) => {
		requestUtil({
			url: '/addressBook',
			method: 'put',
			data: data
		}).then((res) => {
			console.log(res.data)
			resolve(res)
		}).catch((err) => {
			reject(err)
		})
	});
}
//新增地址
export const addAddressApi = (data) => {
	return new Promise((resolve, reject) => {
		requestUtil({
			url: '/addressBook',
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
//删除地址
export const deleteAddressApi = (id) => {
	return new Promise((resolve, reject) => {
		requestUtil({
			url: '/addressBook',
			method: 'delete',
			header: {
				"content-type": "application/x-www-form-urlencoded"
			},
			data: id
		}).then((res) => {
			console.log(res.data)
			resolve(res)
		}).catch((err) => {
			reject(err)
		})
	});
}
//查询单个地址
export const addressFindOneApi = (id) => {
	return new Promise((resolve, reject) => {
		requestUtil({
			url: '/addressBook/' + id,
			method: 'get',

		}).then((res) => {
			console.log(res.data)
			resolve(res)
		}).catch((err) => {
			reject(err)
		})
	});
}
//获取所有地址
export const addressListApi = () => {
	return new Promise((resolve, reject) => {
		requestUtil({
			url: '/addressBook/list',
			method: 'get',

		}).then((res) => {
			console.log(res.data)
			resolve(res)
		}).catch((err) => {
			reject(err)
		})
	});
}
//设置默认地址
export const setDefaultAddressApi = (data) => {
	return new Promise((resolve, reject) => {
		requestUtil({
			url: '/addressBook/default',
			method: 'put',
			data: data
		}).then((res) => {
			console.log(res.data)
			resolve(res)
		}).catch((err) => {
			reject(err)
		})
	});
}