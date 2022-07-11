import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-plus'
import 'element-plus/theme-chalk/index.css'
import * as ElIconModules from "@element-plus/icons-vue"
import installIcon from './icon/index.js'
//bus
import Vue3Bus from 'vue3-eventbus'

const app = createApp(App)
app.use(ElementUI).use(store).use(router).mount('#app')
app.use(installIcon)
app.use(Vue3Bus)
for (const iconName in ElIconModules) {
    if (Reflect.has(ElIconModules, iconName)) {
        const item = ElIconModules[iconName]
        app.component(iconName, item)
    }
}

