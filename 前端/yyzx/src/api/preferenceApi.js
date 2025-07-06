import http from "../request/request.js";


//修改客户喜好
export const updatePreference=(params)=>{
    return http.post(`/customerpreference/updateCustomerpreference`,params)
}

// 查询客户喜好
export const findPreference=(params)=>{
    return http.post(`/customerpreference/getCustomerpreferencesVo`,params)
}

//添加客户喜好
export const addPreference=(params)=>{
    return http.post('/customerpreference/addCustomerpreference',params)
}

//删除
export const delPreference=(id)=>{
     return http.get(`/customerpreference/removeCustomerpreference/${id}`)
}

