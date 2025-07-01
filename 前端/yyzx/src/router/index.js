import { createRouter, createWebHistory } from "vue-router"
// import music from "../views/music.vue"
// import { Component } from "react"

const routes = [
    {
        // 路由重定向
         path: "/",
         redirect: "/login"
    },
    {
         path: "/login",  
         component: () => import("../views/Login.vue")
    },
    {
         path:"/:catchAll(.*)*",  // 匹配所有未定义的路由
         component: () => import("../views/error/NotFound.vue")
    },
]

// linkActiveClass:"active-link", // 设置激活链接的类名
// linkExactActiveClass:"test" // 设置精确激活链接的类名


const router = createRouter({
    history: createWebHistory(),
    routes
})

// 设置激活链接的类名
router.options.linkActiveClass = "active-link"
router.options.linkExactActiveClass = "test"

export default router