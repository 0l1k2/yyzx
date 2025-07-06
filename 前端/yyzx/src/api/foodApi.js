import http from "../request/request.js";

//查询所有食品
export const findFood=(currentPage,pageSize)=>{
     return http.get(`/food/listFood/${currentPage}/${pageSize}`)
    }