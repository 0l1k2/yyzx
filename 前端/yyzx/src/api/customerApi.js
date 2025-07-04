import http from "../request/request.js";


//入住登记-客户信息列表--分页
export const selectCustomer=(params)=>{
    return http.post(`/customer/KhxxFindCustomer`,params)
}
// 入住登记
export const addCustomer=(params)=>{
    return http.post(`/customer/addCustomer`,params)
}

//修改客户信息
export const editCustomer=(params)=>{
    return http.post('/customer/editCustomer',params)
}

//删除客户信息
export const delCustomer=(id, bedId)=>{
     return http.get(`/customer/removeCustomer/${id}/${bedId}`)
}

