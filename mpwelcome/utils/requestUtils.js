const baseUrl = "https://j607523a59.oicp.vip";
// const baseUrl = "http://localhost:8080";

export const getBaseUrl = () => {
    return baseUrl;
}
export const getWxLogin = () => {
    return new Promise((resolve, reject) => {
        wx.login({
            timeout: 1000,
            success: (res) => {
                resolve(res)
            },
            fail: (err) => {
                reject(err)
            }
        })
    }).catch((e) => { });
}

export const getUserProfile = () => {
    return new Promise((resolve, reject) => {
        wx.getUserProfile({
            desc: '获取用户信息',
            success: (res) => {
                resolve(res)
                wx.setStorageSync('userInfo', res.userInfo)
            },
            fail: (err) => {
                reject(err)
            }
        })
    }).catch((e) => { });
}

let ajaxTimes = 0;

export const requestUtil = (params) => {
    const token = wx.getStorageSync('token')
    // let header = { ...params.header };
    let header = getApp().globalData.header;
    // console.log(params.url)
    if (params.url.includes("/")) {
        header["token"] = wx.getStorageSync('token')
    }
    var start = new Date().getTime();
    // console.log("ajaxTimes=" + ajaxTimes)
    ajaxTimes++;

    return new Promise((resolve, reject) => {
        wx.request({
            ...params,
            header,
            url: baseUrl + params.url,
            success: (res) => {
                resolve(res.data)
            },
            fail: (err) => {
                reject(err)
            },
            complete: () => {
            }
        })
    }).catch((e) => { });
}