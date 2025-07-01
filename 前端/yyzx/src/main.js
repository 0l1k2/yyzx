import { createApp } from 'vue'
import { createPinia } from 'pinia' // 导入 Pinia
import './style.css'
import App from './App.vue'
import router from './router' // 导入路由
const pinia = createPinia()
const app=createApp(App)

createApp(App).use(router).use(pinia).mount('#app')