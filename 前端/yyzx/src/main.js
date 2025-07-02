import { createApp } from 'vue'
import { createPinia } from 'pinia'
import './style.css'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
// import router from '/src/router/index.js';
const pinia = createPinia()
createApp(App).use(router).use(pinia).use(ElementPlus).mount('#app')