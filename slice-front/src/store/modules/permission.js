import { constantRoutes } from '@/router'

const admin_Routes = [
  // {
  //   path: '/appPage',
  //   component: () => import('@/layout/index.vue'),
  //   children: [
  //     {
  //       path: '',
  //       component: () => import('@/views/appPage/index.vue'),  // 首页组件
  //     }
  //   ]
  // },
  {
    path: '/',
    component: () => import('@/layout/index.vue'),
    redirect: '/dashboard', // 根路径自动重定向到 /dashboard
    children: [
      {
        path: 'applications',
        component: () => import('@/views/applications/index.vue')  // 其他页面
      },
      {
        path:"sliceResultView",
        component: () => import('@/views/sliceResultView/index.vue')
      }
    ]
  },
  { path: '/:pathMatch(.*)*', redirect: '/404', hidden: true },

]


const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  // generateRoutes({ commit, admin_Routes, enterprise_Routes, carrier_Routes, government_Routes }, roles) {
  generateRoutes({ commit }, roles) {
    console.log('generateRoutes已调用')
    return new Promise(resolve => {
      let accessedRoutes
      console.log('StoreRoles为',roles)
      if (roles.includes('admin')) {
        accessedRoutes = admin_Routes
      }

      // else {
      //   console.log('5561651651')
      //   if (roles.includes('enterprise')) {
      //     accessedRoutes = enterprise_Routes
      //   } else if (roles.includes('carrier')) {
      //     accessedRoutes = carrier_Routes
      //   } else if (roles.includes('government')) {
      //     accessedRoutes = government_Routes
      //   }
      // }

      commit('SET_ROUTES', accessedRoutes)
      console.log("总的accessedRoutes为:",accessedRoutes)
      resolve(accessedRoutes)
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
