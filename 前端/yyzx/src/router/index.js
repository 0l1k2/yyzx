import { createRouter, createWebHistory } from "vue-router"
import ServiceBuy from '../views/health/ServiceBuy.vue'; 
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
            {
                 path: "/customer/preference",
                component: () => import("../views/customer/Preference.vue"),
            },
            {
                 path: "/customer/meal",
                component: () => import("../views/customer/Meal.vue"),
            },
            {
                 path: "/nurse/nurseLevel",
                component: () => import("../views/nurse/NurseLevel.vue"),
            },
            {
                 path: "/nurse/nurseItem",
                component: () => import("../views/nurse/NurseItem.vue"),
            },
            {
                 path: "/nurse/nurseSetting",
                component: () => import("../views/nurse/NurseSetting.vue"),
            },
            {
                 path: "/nurse/nurseRecords",
                component: () => import("../views/nurse/NurseRecords.vue"),
            },
            {
                 path: "/health/serviceCustomerSetting",
                component: () => import("../views/health/serviceCustomerSetting.vue"),
            },
            {
                 path: "/health/serviceCare",
                component: () => import("../views/health/ServiceCare.vue"),
            },
            {
                 path: "/user/listUser",
                component: () => import("../views/user/ListUser.vue"),
            },
            {
                 path: "/health/serviceBuy",
                component: () => import("../views/health/ServiceBuy.vue"),
            },
            {
                 path: "/nurse/addItemToLevel",
                component: () => import("../views/nurse/AddItemToLevel.vue"),
            },
            {
                 path: "/health/userToCustomerService",
                component: () => import("../views/health/UserToCustomerService.vue"),
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