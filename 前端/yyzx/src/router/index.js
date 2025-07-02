import { createRouter, createWebHistory } from "vue-router";

const routes = [
    {
        path: "/",
        redirect: "/login"
    },
    {
        path: "/login",
        name: "Login", // 添加路由名称（推荐）
        component: () => import("../views/Login.vue")
    },
    {
        path: "/:catchAll(.*)*",
        name: "NotFound", // 添加路由名称
        component: () => import("../views/error/NotFound.vue")
    },
];

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL), 
    routes,
    // 直接在此处设置链接类名更清晰
    linkActiveClass: "active-link",
    linkExactActiveClass: "test"
});

export default router; // 保持默认导出