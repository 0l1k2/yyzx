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
                <input type="button" value="登录" style="width:100%;">
            </form>
        </div>
    </div>
</template>

<script setup>
import {login} from '@/api/userApi.js';
import { ref } from 'vue';
import { setSessionStorage } from '@/utils/common.js';

let loginForm=ref({
    username: '',
    password: ''
});

const login = () => {
    login(loginForm.value).then(res=>{
        if(res.flag){
            //将token存入sessionStorage
            setSessionStorage('token', res.message);
            //将当前登录的用户信息存入sessionStorage
            setSessionStorage('user', res.data);
            //将菜单列表存入pinia中

        }
    })
};

</script>