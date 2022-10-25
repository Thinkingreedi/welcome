<template>
  <view>
    <view class="user">
      <view class="divHead">
        <!-- 头像 -->
        <u-avatar size="60" shape="circle" :src="user.avatarUrl"></u-avatar>
        <!-- 名称 -->
        <view class="divUserName" v-if="user">
          <u-text :text="user.nickName" bold="true"></u-text>
        </view>
        <!-- 登录(条件渲染) -->
        <view class="divLogin" v-else>
          <button style="background: #ffc200;margin-left: 0;padding-left: 0" @click="login" size="mini">登录</button>
        </view>
        <!-- 无效 -->
        <view class="userPhone">
          <u-text :text="phoneNumber"></u-text>
        </view>
        <u-modal :show="show" :content='content'>
          <view style="font-size: 36rpx;text-align: center;">绑定手机号
            <view style="color: lightgray; font-size: 28rpx;text-align: center">请先绑定手机号再进行此操作</view>
          </view>
          <u-button openType="getPhoneNumber" @getphonenumber="confirm" type="success" slot="confirmButton"
            shape="circle" text="微信用户一键绑定"></u-button>
        </u-modal>
      </view>

      <scroll-view scroll-y="true" :style="{height: wh-75 + 'px'}">
        <view class="divContent">
          <view class="divLinks">
            <view @click="toAddress" class="item">
              <image src="../../static/images/locations.png"></image>
              <text>我的地址</text>
              <view>
                <u-icon name="arrow-right"></u-icon>
              </view>
            </view>
            <view class="divSplit"></view>
            <view @click="toOrderList" class="item">
              <image src="../../static/images/orders.png"></image>
              <text>历史订单</text>
              <view>
                <u-icon name="arrow-right"></u-icon>
              </view>
            </view>
          </view>

          <!-- 无效 -->
          <view class="divOrders" v-if="flag && user">
            <view class="title">最新订单</view>
            <view class="timeStatus">
              <text>{{order[0].orderTime}}</text>
              <text>{{getStatus(order[0].status)}}</text>
              <!-- <text>正在派送</text> -->
            </view>
            <view class="dishList">
              <view v-for="(item,index) in order[0].orderDetails" :key="index" class="item">
                <text>{{item.name}}</text>
                <text>x{{item.number}}</text>
              </view>
            </view>
            <view class="result">
              <text>共{{order[0].sumNum}} 件商品,实付</text>
              <text class="price">￥{{order[0].amount}}</text>
            </view>
            <view class="btn" v-if="order[0].status === 4">
              <view class="btnAgain" @click="addOrderAgain">再来一单</view>
            </view>
          </view>

          <view v-if="user" class="foot">
            <view @click="logout" class="logout">
              <text>退出登录</text>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script>
import {
  addOrderApi,
  orderListApi,
  orderPagingApi,
  orderAgainApi,
  deleteOrderApi
} from '../../api/orderList.js'
import {
  getBaseUrl,
  requestUtil,
  getWxLogin,
  getUserProfile,
} from '../../utils/requestUtils.js';
import {
  loginApi,
  loginoutApi
} from "../../api/login.js"
import regeneratorRuntime from '../../lib/runtime/runtime.js';
import { request } from 'http';
export default {
  data() {
    return {
      userId: '',
      content: "微信用户快速登录",
      flag: false,
      phoneNumber: "",
      show: false,
      user: {},
      wh: 0,
      QiNiuYunUrl: getApp().globalData.QiNiuYunUrl,
      imageUrl: '',
      ruleForm: {
        'id': '',
        'phone': '',
        'sex': '男',
        'status': '',
        'avatar': '',
        'idNumber': '',
      },
      form: {
        phone: '',
        code: ''
      },
      msgFlag: false,
      order: [{
        orderTime: '', //下单时间
        status: undefined, //订单状态 1已结账，2未结账，3已退单，4已完成，5已取消
        orderDetails: [{
          name: '', //菜品名称
          number: undefined, //数量
        }], //明细
        amount: undefined, //实收金额
        sumNum: 0, //菜品总数
      }]
    };
  },
  created() {
  },
  onShow() {
    this.getUserInfo()
    this.initData()
  },
  mounted() {
    const sysInfo = uni.getSystemInfoSync()
    this.wh = sysInfo.windowHeight
    this.getUserInfo()
    this.initData()
  },
  methods: {
    initData() {
      if (this.userId) {
        this.getLatestOrder()
      }
    },

    async getLatestOrder() {
      const params = {
        order: "desc",
        orderField: "order_time",
        page: 1,
        pageSize: 1
      }
      const res = await orderPagingApi(params)
      if (res.code === 1) {
        if (res.data.records.length != 0) {
          this.flag = true
        }
        this.order = res.data.records
        if (this.order && this.order[0].orderDetails) {
          let number = 0
          this.order[0].orderDetails.forEach(item => {
            number += item.number
          })
          this.order[0].sumNum = number
        }
      } else {
        return uni.$showMsg(res.msg)
      }
    },

    getStatus(status) {
      let str = ''
      switch (status) {
        case 1:
          str = '待付款'
          break;
        case 2:
          str = '正在派送'
          break;
        case 3:
          str = '已派送'
          break;
        case 4:
          str = '已完成'
          break;
        case 5:
          str = '已取消'
          break;

      }
      return str
    },

    // 非tabar页面采用navigateTo
    toAddress() {
      uni.navigateTo({
        url: '/pages/address2/address2'
      })
    },

    // tabar页面采用switchTab
    toOrderList() {
      uni.switchTab({
        url: '/pages/orderList/orderList'
      })
    },

    confirm(e) {
      if (e.detail.errMsg == 'getPhoneNumber:ok') {
        let param = {
          code: e.detail.code,
          encryptedData: this.getPhoneParam.encryptedData,
          iv: this.getPhoneParam.iv
        }
        this.getPhoneNumber(param)
      } else {
        return uni.$showMsg('请授权登录!')
      }
    },

    async wxLogin(loginParam) {
      const res = await requestUtil({
        url: "/user/login",
        data: loginParam,
        method: "post"
      });
      // console.log("wxLogin -- res", res)
      getApp().globalData.header.Cookie = 'JSESSIONID=' + res.data;

      if (res.code === 0) {
        wx.setStorageSync('token', res.data.token)
        wx.setStorageSync('userId', res.data.userId)
        if (res.data.phone == null) {
          this.show = true
          uni.hideTabBar({
            animation: true
          })
        }
        wx.setStorageSync('phoneNumber', res.data.phone)
        this.getUserInfo()
        this.initData()
      }
    },

    login() {
      Promise.all([getWxLogin(), getUserProfile()]).then((res) => {
        let loginParam = {
          nickName: res[1].userInfo.nickName,
          avatarUrl: res[1].userInfo.avatarUrl,
          gender: res[1].userInfo.gender
        }
        this.getUserInfo()
        this.wxLogin(loginParam);
      })
    },

    async getPhoneNumber(param) {
      let that = this
      const res = await requestUtil({
        url: "/user/wxGetPhone",
        data: param,
        method: "post"
      })
      if (res.code === 1) {
        this.phone_info = res.data
        let phoneNumber = this.phone_info
        wx.setStorageSync('phoneNumber', phoneNumber)
        this.show = false
        this.getUserInfo()
        uni.showTabBar({
          animation: true
        })
        this.initData()
      } else {
        return uni.$showMsg()
      }
    },

    getUserInfo() {
      this.user = wx.getStorageSync('userInfo')
      this.phoneNumber = wx.getStorageSync('phoneNumber')
      // console.log('用户信息' + this.user)
    },

    async logout() {
      //询问用户是否退出登录
      const [err, succ] = await uni.showModal({
        title: '提示',
        content: '确认退出登录吗？'
      }).catch(err => err)
      //用户确认了退出登录的操作
      if (succ && succ.confirm) {
        getApp().globalData.header.Cookie = ' '
        wx.clearStorageSync()
        this.getUserInfo()
      }
    }
  },
}
</script>

<style>
@import url(./my.css);
</style>
