<template>
    <el-header id="header">
        <div>
            <img src="../assets/welcome.jpg" style="width:200px;height: 40px;margin:20px 0 0 -12px;">
        </div>
        <el-dropdown split-button type="default" >
            {{ user.nickname }}
            <template #dropdown>
                <el-dropdown-menu>
                    <el-dropdown-item  @click="logout">注销</el-dropdown-item>
                </el-dropdown-menu>
            </template>
        </el-dropdown>
    </el-header>
</template>

<script setup>
import { getSessionStorage,setSessionStorage} from '../utils/common.js';
import { useUserStore } from '../store/index.js';
import { getCurrentInstance } from 'vue';
const { proxy } = getCurrentInstance();
const userStore = useUserStore();
const user = getSessionStorage('user');

const logout = () => {
    setSessionStorage('token', null);
    setSessionStorage('user', null);
    userStore.addMenus([]);
    userStore.clearTabs([]);
    proxy.$router.push('/login');
};
</script>

<style scoped>
#header{
    max-height: 50px;
    line-height: 50px;
    display: flex;
    justify-content: space-between;
    align-items: center;
}
#header>h2{
    color:#0b67b8;
}

</style>