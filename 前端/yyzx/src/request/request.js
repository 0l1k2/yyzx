import axios from "axios";
import  router  from "../router";
import qs from "qs";
import {getSessionStorage} from "../utils/common.js";

const instance = axios.create({
    baseURL: "http://localhost/yyzx",
}); 

// 请求拦截器
instance.interceptors.request.use(function(config){
    //检查token是否存在
    let tokeb=getSessionStorage("token");
    if(tokeb){
        //如果存在token,则将token添加到请求头中
        config.headers['token']=tokeb;
    }
    //定义需要post的请求方式为application/json的api集
    let jsonQueryParamsPath = ['/customernurseitem/addItemToCustomer']
    //设置post请求参数为
    if (config.method === 'post' && jsonQueryParamsPath.indexOf(config.url)) {
       config.data=qs.stringify(config.data)
    }
    return config;
}, function(error){
    return Promise.reject(error);
});


// 响应拦截器
instance.interceptors.response.use(function(response){
    //如果token过期或不存在，则跳转到登录页面
    if (!response.data.flag && response.data.data == "token_error") {
        // 清除token
        sessionStorage.removeItem("token");
        // 跳转到登录页面
        router.push({ path: '/login' });
    }
    return response.data;
},function(error){return Promise.reject(error)}
);

export default instance;