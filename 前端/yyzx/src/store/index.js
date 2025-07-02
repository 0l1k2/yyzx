import { defineStore } from "pinia";
import { getSessionStorage, setSessionStorage } from "../utils/common.js";
export const useUserStore = defineStore("user", {
    state: () => ({
        tabs: [],
        menus: getSessionStorage("menuList") || [],
    }),
    getters: {
        // 获取所有的tab
        getTabs: (state) => state.tabs,
        // 获取所有的菜单
        getMenus: (state) => state.menus,
        // 获取菜单名字
        getMenuNameByUrl: (state) => (url) => {
            const menu = state.menus.find((menu) => {
                    return menu.children.some((child) => child.path === url);
            });
            if (menu) {
                const child = menu.children.find((child) => child.path === url);
                return child ? child.title : '';
            }
            return '';
        },
    },
    actions: {
        // 添加菜单
        addMenus(menuList) {
            console.log('************************');
            console.log(menuList);

            this.menus = menuList;
            // 将菜单存储到sessionStorage中
            setSessionStorage('menuList', menuList)
        },
        // 根据索引删除tab
        deleteTabByIndex(index) {
            this.tabs.splice(index, 1);
        },
        // 清空tab
        clearTabs(param) {
            this.tabs = param;
        },
        // 添加tab
        addTab(payload) {
            const path = payload.path;
            
            if (path) {
                const result = this.tabs.filter((item) => item.path === path)
                // 判断是否存在，如果不存在才需要加入到state的tabs中
                if (result.length == 0) {
                    if (this.tabs.length == 10) {
                        // 删除第二个
                        this.tabs.splice(1, 1);
                    }
                    // 需要添加
                    this.tabs.push(payload)
                }
            }
        }
    },
    persist:true,// 开启持久化存储（默认存在localstorage中）
})