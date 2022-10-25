import { getBaseUrl, requestUtil, getUserProfile, getWxLogin } from '../utils/requestUtils';
import regeneratorRuntime, { async } from '../lib/runtime/runtime';

//购物车中添加商品
export const addCartApi = (data) => {
    return new Promise((resolve, reject) => {
        requestUtil({
            url: '/shoppingCart/add',
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
//购物车中修改商品
export const updateCartApi = (data) => {
    return new Promise((resolve, reject) => {
        requestUtil({
            url: '/shoppingCart/sub',
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
//删除购物车的商品
export const clearCartApi = () => {
    return new Promise((resolve, reject) => {
        requestUtil({
            url: '/shoppingCart/clean',
            method: 'delete'

        }).then((res) => {
            console.log(res.data)
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    });
}
//获取套餐的全部菜品
export const setMealDishDetailsApi = (id) => {
    return new Promise((resolve, reject) => {
        requestUtil({
            // 'url': `/setmeal/dish/${id}`,
            url: `/setmeal/${id}`,
            method: "get",

        }).then((res) => {
            console.log(res.data)
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    });
}
//获取菜品分类对应的菜品
export const dishListApi = (data) => {
    return new Promise((resolve, reject) => {
        requestUtil({
            url: '/dish/list',
            method: "get",
            data: { ...data }
        }).then((res) => {
            console.log(res.data)
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    });
}
//获取菜品分类对应的套餐
export const setmealListApi = (data) => {
    return new Promise((resolve, reject) => {
        requestUtil({
            url: '/setmeal/list',
            method: "get",
            data: { ...data }
        }).then((res) => {
            console.log(res.data)
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    });
}
//获取所有的菜品分类
export const categoryListApi = () => {
    return new Promise((resolve, reject) => {
        requestUtil({
            url: "/category/list",
            method: "get"
        }).then((res) => {
            console.log(res.data)
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    });
}
//获取购物车内商品的集合
export const cartListApi = (data) => {
    return new Promise((resolve, reject) => {
        requestUtil({
            url: "/shoppingCart/list",
            method: "get",
            data: { ...data }
        }).then((res) => {
            console.log(res.data)
            resolve(res)
        }).catch((err) => {
            reject(err)
        })
    });
}
