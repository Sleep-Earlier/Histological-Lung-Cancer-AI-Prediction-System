import { createApp } from 'vue'
// // 手动引入全局样式
import './style.css'
import App from './App.vue'
import store from './store' 
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'  // 引入 Element Plus 样式
import './permission'  // 引入权限控制逻辑

const app = createApp(App)

app.use(ElementPlus)  // 使用 Element Plus
app.use(router)
app.use(store)  // 注册 Vuex store
app.mount('#app')