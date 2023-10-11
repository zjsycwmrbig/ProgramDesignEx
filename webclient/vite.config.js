import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  // 解决代理问题
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080', // 代理目标的基础路径
        changeOrigin: true, // 支持跨域
        pathRewrite: {
          '^/api': '/', // 重写路径，将/api前缀去掉
        },
      },
    },
  },
})