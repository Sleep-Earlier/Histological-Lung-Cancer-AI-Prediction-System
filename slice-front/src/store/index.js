import { createStore } from 'vuex'
import permission from './modules/permission'
import user from './modules/user'
import getters from './getters'  // 假设你有一个 getters 文件
import appPage from './modules/appPage'; // 导入新模块
import slice from './modules/slice'; // 导入新模块

// 创建 Vuex Store 实例
const store = createStore({
  modules: {
    permission,
    user,
    appPage,
    slice
  },
  getters
})

export default store
