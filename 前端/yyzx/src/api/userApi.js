import http from "../request/request.js";

// 用户登录
export const login=(data)=>{
    return http.post("/user/login", data);
}