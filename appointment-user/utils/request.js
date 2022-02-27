import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import cookie from 'js-cookie'

// 创建axios实例
const service = axios.create({
  baseURL: 'http://localhost',
  timeout: 15000 // 请求超时时间
})

// http request 拦截器
service.interceptors.request.use(
  config => {
    //判断cookie中是否有token值
    if (cookie.get('token')) {
      //若有值则将token值放到请求头header里,命名为token
      config.headers['token'] = cookie.get('token')
    }
    return config
  },
  err => {
    return Promise.reject(err)
  })
// http response 拦截器
service.interceptors.response.use(
  response => {
    if (response.data.code === 208) {//状态码为208表示未登录
      loginEvent.$emit('loginDialogEvent')//弹出登录框
      return
    } else {
      if (response.data.code !== 200) {
        Message({
          message: response.data.message,
          type: 'error',
          duration: 5 * 1000
        })
        return Promise.reject(response.data)
      } else {
        return response.data
      }
    }
  },
  error => {
    return Promise.reject(error.response)
  })
export default service
