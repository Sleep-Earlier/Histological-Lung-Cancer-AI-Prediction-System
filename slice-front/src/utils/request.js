import axios from 'axios'
import { ElMessageBox, ElMessage } from 'element-plus'  // 使用 element-plus 替代 element-ui
import store from '@/store'
import { getToken } from '@/utils/auth'

// 创建 axios 实例
const service = axios.create({
  baseURL: "http://127.0.0.1:8083/", // 请求基础 URL
  timeout: 10000 // 请求超时时间
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    // 如果存在 token，则在请求头中添加
    if (store.getters.token) {
      // 将 Token 加到 Authorization 中
      config.headers['Authorization'] = `Token:${getToken()}`
    }
    return config
  },
  error => {
    // 请求错误处理
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data

    // 如果响应没有 `code`，或 `code` 不等于 200，显示错误信息
    if (typeof res !== 'object' || res === null || !('code' in res)) {
      console.log('Invalid response: code is missing')
      return response
    }

    // 响应中的 code 不是 200 的处理
    if (res.code !== 200) {
      ElMessage({
        message: res.msg || 'Error',
        type: 'error',
        duration: 5 * 1000
      })

      // 特殊错误码处理，处理 token 失效等情况
      if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
        ElMessageBox.confirm(
          'You have been logged out, you can cancel to stay on this page, or log in again',
          'Confirm logout',
          {
            confirmButtonText: 'Re-Login',
            cancelButtonText: 'Cancel',
            type: 'warning'
          }
        ).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload() // 刷新页面重新登录
          })
        })
      }

      return Promise.reject(new Error(res.msg || 'Error'))
    } else {
      return res
    }
  },
  error => {
    // 处理 HTTP 错误
    console.log('requestErr: ' + error) // for debug
    ElMessage({
      message: "requestErr: " + error,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
