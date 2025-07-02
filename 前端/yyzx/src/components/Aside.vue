<template>
    <el-aside width="200px" id="asideNav">
        <div class="logo-name">
            <p>东软颐养中心</p >
            <img src="../assets/logo.png" class="logo-png">
        </div>
        <el-menu active-text-color="#ffd04b" background-color="#3CA2E0" :default-active="currentPath" :router="true"
            unique-opened text-color="#fff">
            <template v-for="menu in menus" :key="menu.id">
                <el-sub-menu :index="menu.menusIndex">
                    <template #title>
                        <el-icon>
                            <!-- 动态获取图标 -->
                            <component :is="menu.icon" />
                        </el-icon>
                        <span>{{ menu.title }}</span>
                    </template>
                    <template v-for="child in menu.children" :key="child.id">
                        <el-menu-item :index="child.path">
                            <el-icon>
                                <component :is="child.icon"></component>
                            </el-icon>
                            <span>{{ child.title }}</span>
                        </el-menu-item>
                    </template>
                </el-sub-menu>
            </template>
        </el-menu>
    </el-aside>
</template>

<script setup>
import { watch, getCurrentInstance, computed } from 'vue';
import { useUserStore } from '../store/index.js';
import { useRoute } from 'vue-router';
const { proxy } = getCurrentInstance();
const userStore = useUserStore();
const route = useRoute();

// 获取菜单数据
const menus = computed(() => userStore.getMenus)

// 获取当前路由
const currentPath = computed(() => route.path);
// 监听浏览器直接输入路由，将此路由添加到tabs
watch(currentPath, (newVal, oldVal) => {
    // 判断当前tab中是否已存在
    let index = userStore.getTabs.findIndex((tab) => tab.path === newVal);
    if (index !== -1 || newVal === '/login') {
        return; // 如果已存在或是登录页，则不添加
    }
    // 获取菜单名字（需要实现getMenuNameByUrl方法）
    // const menuName = userStore.getters.getMenuNameByUrl(newVal);
    const menuName =userStore.getMenuNameByUrl(newVal);
    // 添加新标签页
    userStore.addTab({
        title: menuName,
        path: newVal
    })
}, { immediate: true });
</script>

<style scoped>
#asideNav {
    display: flex;
    flex-direction: column;
}


#asideNav .logo-name {
    width: 100%;
    height: 150px;
    background-color: #3CA2E0;
}

#asideNav .logo-name .logo-png {
    width: 165px;
    height: 165px;
    margin: -30px 0 0 10px;
}

#asideNav .logo-name p {
    font-weight: 800;
    height: 50px;
    line-height: 50px;
    text-align: center;
    font-size: 25px;
    margin: 0px;
    color: #fff;
}

#asideNav .el-menu {
    /* font-weight: 550; */
    flex: 1;
    border-right: none;
}
</style>