import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { viteMockServe } from 'vite-plugin-mock'
import path from 'path'
import Icons from "unplugin-icons/vite";
import IconsResolver from "unplugin-icons/resolver";
import Components from "unplugin-vue-components/vite";

// 确保正确导入
// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    viteMockServe({
      mockPath: 'mock', // Mock 数据存放的路径
      localEnabled: true // 开发环境下启用 Mock
    }),
    Components({
      // 自动按需导入组件目录
      dirs: ["src/components"],
      resolvers: [
        IconsResolver()
      ]
    }),
    Icons({autoInstall: true}),
  ],
  // 配置 @ 为 src
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src') // 将 @ 映射到 src 目录
    }
  }

})
