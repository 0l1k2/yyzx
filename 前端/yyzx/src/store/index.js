import { defineStore } from "pinia";
import { getSessionStorage,sessionStorage} from "@/utils/common.js";
import { ref } from "vue";


export const useUserStore = defineStore("user", {
    state: () => ({
        tabs: [], // 存储选项卡
        menus:getSessionStorage("user").menuList || [], // 存储菜单
    }),
    getters:{
        tabs: (state) => state.tabs, // 获取选项卡
       menus: (state) => state.menus, // 获取菜单
    },
    actions:{
        //添加菜单
        addMenus(param){
            this.menus=param;
            // 将菜单存储到 sessionStorage
            sessionStorage('menuList',param)
        },
        //根据索引删除tab
        delectTabByIndex(index) {
            this.tabs.splice(index, 1);
        },
        //清空tab
        clearTabs() {
            this.tabs = [];
        },
        //添加tab
        addTab(payload){
            const path=payload.path;
            //判断是否存在，如果不存在才需要加入到tab中
            if(path){
                const result=this.tabs.filter((item)=>item.path === path)
                if(result.length === 0){
                    if(this.tabs.length === 10){
                        //删除第二个
                        this.tabs.splice(1, 1);
                }
                //需要添加
                this.tabs.push(payload);
                }
            }
        }
    }
})
