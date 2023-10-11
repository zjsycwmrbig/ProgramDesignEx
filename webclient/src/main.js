import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'


import App from './App.vue'

const app = createApp(App)

// 使用pinia
app.use(createPinia())
app.use(ElementPlus)
// 挂载
app.mount('#app')
