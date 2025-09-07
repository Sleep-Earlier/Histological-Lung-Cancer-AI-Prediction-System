// 在mock端口中
// window.location.origin 会自动适配项目当前的域名和端口，
// 无论你是在本地开发环境还是生产环境中，都可以确保正确的 API 调用
import request from '@/utils/request'

export function login(data) {
  // console.log('login请求已经被发出,为:', data);
  return request({
    url: '/user/login',
    // 使用get吗
    method: 'get',
    params: data // 通过 params 传递查询参数,注意和post的区别
  })
}

export function register(data) {
  return request({
    url: '/user/register',
    method: 'post',
    data: data,
  })
}

export function getInfo(token) {
  return request({
    // url: 'http://127.0.0.1:8083/api/tokenInfo',
    url: `${window.location.origin}/api/user/tokenInfo`,
    method: 'get',
  })
}

export function logout() {
  return request({
    url: `${window.location.origin}/api/user/logoutVerify`,
    method: 'post'
  })
}
