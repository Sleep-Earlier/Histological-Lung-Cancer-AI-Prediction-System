// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router'

// 定义公共路由
export const constantRoutes = [
    {
      path: '/login',
      component: () => import('@/views/login/index.vue'),
      hidden: true
    },
    {
      path: '/register',
      component: () => import('@/views/Register/index.vue'),
      hidden: true
    },
    {
      path: '/404',
      component: () => import('@/views/404.vue'),
      hidden: true
    },
    // 有子路由时，子路由的 path 会拼接父路由的 path
    {
      path: '/',
      component: () => import('@/layout/index.vue'),
      redirect: '/dashboard', // 根路径自动重定向到 /dashboard
      children: [
        {
          path: 'dashboard',
          component: () => import('@/views/dashboard/index.vue')  // 首页组件
        },

      ]
    }
    
  ]

const router = createRouter({
    history: createWebHistory(), // 使用 history 模式
    scrollBehavior: () => ({ y: 0 }), // 控制页面滚动行为
    routes: constantRoutes  // 引用静态路由配置
})

// 用于重置路由
export function resetRouter() {
   // 重新创建路由实例，并使用与原始路由相同的配置
   const newRouter = createRouter({
    history: createWebHistory(),  // 保持相同的 history 配置
    routes: constantRoutes  // 使用相同的路由配置
  })
  router.matcher = newRouter.matcher // reset router
}

export default router
