import http from "../request/request.js";


//入住登记-客户信息列表--分页
export const selectCustomer=(params)=>{
    return http.post(`/customer/KhxxFindCustomer`,params)
}
// 入住登记
export const addCustomer=(params)=>{
    return http.post(`/customer/addCustomer`,params)
}

//修改客户信息//客户管理下的编辑客户信息

export const editCustomer=(params)=>{
    return http.post('/customer/editCustomer',params)
}

//删除客户信息
export const delCustomer=(id, bedId)=>{
     return http.get(`/customer/removeCustomer/${id}/${bedId}`)
    }
//客户管理下的获取客户信息
// export const getKhxxList=(params)=>{
//     return http.post(`/customer/KhxxFindCustomer`,params)
// }
export const getKhxxList = (params) => {
  // 手动构建符合后端要求的请求体
  const requestData = {
    currentPage: Number(params.currentPage) || 1,
    pageSize: Number(params.pageSize) || 6,
    customerName: params.customerName || null,
    manType: Number(params.manType) || 3
  };
  // 保持与您其他方法完全一致的调用方式
  return http.post('/customernurseitem/listCustomerItem', requestData);
};



