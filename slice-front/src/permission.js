// src/router/index.js
import router from './router'
import store from './store'
import { ElMessage } from 'element-plus'  // 使用 Element Plus 替代 Element UI
import NProgress from 'nprogress'         // progress bar
import 'nprogress/nprogress.css'          // progress bar style
import { getToken } from '@/utils/auth'   // get token from cookie
import getPageTitle from '@/utils/get-page-title'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

// 定义不需要权限就能访问的路由
const whiteList = ['/login', '/register','/dashboard'] // no redirect whitelist

// 路由前置守卫
router.beforeEach(async (to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title)

  // determine whether the user has logged in
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      // 如果已经登录，跳转到主页
      next({ path: '/' })
      NProgress.done()
    } else {
      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      if (hasRoles) {
        next()
      } else {
        try {
          // 获取用户信息
          const { roles } = await store.dispatch('user/getInfo')

          // 生成基于角色的路由
          const accessRoutes = await store.dispatch('permission/generateRoutes', roles)

          // 注意：Vue Router 4 不再支持 addRoutes，我们需要使用 router.addRoute 动态添加每个路由
          accessRoutes.forEach(route => {
            router.addRoute(route)  // 使用 addRoute 动态添加路由
          })

          // 动态添加完路由后，确保使用 replace 来避免重复导航
          next({ ...to, replace: true })
        } catch (error) {
          // 获取用户信息失败，重置 token 并跳转到登录页面
          await store.dispatch('user/resetToken')
          ElMessage.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* 没有 token */
    if (whiteList.indexOf(to.path) !== -1) {
      // 在白名单中，直接访问
      next()
    } else {
      // 没有访问权限，跳转到登录页
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

// 路由后置守卫
router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
