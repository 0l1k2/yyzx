import http from "../request/request.js";
import axios from 'axios'
import request from '../request/request.js'  // 导入您封装的 axios 实例

//用户登录
export const login=(data)=>{
    return http.post("/user/login", data);
}
//添加用户
export const addUser=(data)=>{
    return http.post("/user/addUser", data);
}
//修改用户
export const updateUser=(data)=>{
    return http.post("/user/updateUser", data);
}
//删除用户
export const delUser=(id)=>{
    return http.get(`/user/deleteUser/${id}`);
}
//查询用户
export const findAllUser=(data)=>{
    return http.post("/user/findUserPage", data);
}
//根据用户id查询用户

export const findAllUserByRoldId=(data)=>{
    return http.post(`/user/findUserPageByRoleId`, data);
}

// 获取管家用户列表
// export const  getUserList=(params) =>{
//     return http.post(`/user/findUserPage`, params);
// }


// src/api/userApi.js


export function getUserList(params) {
  return request({  // 使用正确的变量名 request
    url: '/user/findUserPage',
    method: 'post',
    data: params
  })
}