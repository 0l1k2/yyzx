import http from "../request/request.js";

//查询房间列表
export const listRoom=()=>{
    return http.get('/room/listRoom')
}
