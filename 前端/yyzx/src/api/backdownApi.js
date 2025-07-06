import http from "../request/request.js";


//退住查询
export const listBackdownRecord=(params)=>{
    return http.post(`/backdown/listBackdownVo`,params)
}


//添加退住申请
export const addBackdownRecord=(params)=>{
    return http.post(`/backdown/addBackdown`,params)
}

//撤回退住申请
export const deleteBackdownRecord=(id)=>{
    return http.get(`backdown/delBackdown${id}`)
}

//审批退住申请
export const auditBackdownRecord=(params)=>{
    return http.post(`/backdown/examineBackdown`,params)
}