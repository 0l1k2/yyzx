import { createRouter, createWebHistory } from "vue-router"

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
        path: "/home",
        component: () => import("../views/layout/LayOut.vue"),
        children: [
            {
                path: "/bed/bedMap",
                component: () => import("../views/bed/BedMap.vue"),
            },
            {
                 path: "/bed/bedManage",
                component: () => import("../views/bed/BedManage.vue"),
            },
             {
                 path: "/customer/checkIn",
                component: () => import("../views/customer/Checkin.vue"),
            },
            {
                 path: "/customer/outRecords",
                component: () => import("../views/customer/OutRecords.vue"),
            },
            {
                 path: "/customer/checkOut",
                component: () => import("../views/customer/CheckOut.vue"),
            },

        ]
    },
    {
        path: "/:catchAll(.*)*",
        component: () => import("../views/error/NotFound.vue")
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router