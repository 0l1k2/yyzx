import http from "../request/request.js";


//添加外出审批
export const addOutwardRecord=(params)=>{
    return http.post(`/outward/addOutward`,params)
}

// //根据用户id查询对应的客户信息
export const listOutwardRecord=(params)=>{
    return http.post(`/outward/selectOutwardVo`,params)
}



//撤回外出审批
export const deleteOutwardRecord=(id)=>{
     return http.get(`/outward/delOutward/${id}`)
}

//外出审批
export const auditOutwardRecord=(params)=>{
    return http.post('/outward/examineOutward',params)
}


//登记回院时间
export const updateOutwardRecord=(params)=>{
    return http.post('/outward/updateBackTime',params)
}

//根据用户id查询对应的客户信息
// export const queryOutwardVo=(params)=>{
//     return http.post('/outward/selectOutwardVo',params)
// }
