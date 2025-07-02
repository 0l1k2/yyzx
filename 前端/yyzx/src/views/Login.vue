<template>
    <div class="login-bg">
        <div class="login">
            <div class="massage">东软老年人中心</div>
            <div class="darkbannerwrap"></div>
            <form >
                <input type="text" placeholder="用户名" v-model="loginForm.username"/>
                <hr class="hr15">
                <input type="password" placeholder="密码" v-model="loginForm.password"/>
                <hr class="hr15">
                <input type="button" value="登录" style="width:100%;" @click="userlogin"/>
            </form>
        </div>
    </div>
</template>

<script setup>
import {login} from '../api/userApi.js';
import { ref ,getCurrentInstance} from 'vue';
import { setSessionStorage } from '../utils/common.js';
import { useUserStore } from '../store/index.js';
import { ElMessage } from 'element-plus';

const { proxy } = getCurrentInstance();
const userStore = useUserStore();


let loginForm=ref({
    username: '',
    password: ''
});

const userlogin = () => {
    login(loginForm.value).then(res=>{
        if(res.flag){
            //将token存入sessionStorage
            setSessionStorage('token', res.message);
            //将当前登录的用户信息存入sessionStorage
            setSessionStorage('user', res.data);
            //将菜单列表存入pinia中
            userStore.addMenus(res.data.menuList);
            //路由到第一个子菜单
            proxy.$router.push(res.data.menuList[0].path );
        }else{
            ElMessage.error(res.message);
        }
    })
};

</script>