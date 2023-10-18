import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import VueCodemirror from 'vue-codemirror'
import { basicSetup } from 'codemirror'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'

const app = createApp(App)

// 使用pinia
app.use(createPinia())
app.use(ElementPlus)


app.use(VueCodemirror, {
    // keep the global default extensions empty
    extensions: [basicSetup]
})

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

// 挂载
app.mount('#app')
