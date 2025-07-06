import http from "../request/request.js";


//查询护理记录-分页
export const listNurseRecordsVo=(params)=>{
    return http.post(`/nurserecord/listNurseRecordsVo`,params)
}
// 添加护理记录
export const addNurseRecord=(params)=>{
    return http.post(`/nurserecord/addNurseRecord`,params)
}


//删除护理记录
export const removeCustomerRecord=(id)=>{
     return http.get(`/nurserecord/removeNurseRecord/${id}`)
    }


