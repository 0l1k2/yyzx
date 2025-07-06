import http from "../request/request.js";

// 为顾客添加护理项目
export const addItemToCustomer = (params) => {
    return http.post(`/customernurseitem/addItemToCustomer`, params);
};

// 移除顾客护理等级及其级联移除项目
export const removeCustomerLevelAndItem = ({levelId, customerId}) => {
    return http.get(`/customernurseitem/removeCustomerLevelAndItem/${levelId}/${customerId}`);
};

// 查看客户项目
export const listCustomerItem = (params) => {
    return http.post(`/customernurseitem/listCustomerItem`, params);
};

// 客户续费
export const enewNurseItem = (params) => {
    return http.post(`/customernurseitem/enewNurseItem`, params);
};

// 删除客户护理项目
export const removeCustomerItem = (id) => {
    return http.get(`/customernurseitem/removeNurseItem/${id}`);
};

// 判断客户是否已经配置了指定的某个项目
export const isIncludeItemCustormer = ({customerId, itemId}) => {
    return http.get(`/customernurseitem/isIncludeItemCustormer/${customerId}/${itemId}`);
};


// import http from "../request/request.js";


// //为顾客添加护理项目
// export const addItemToCustomer=(params)=>{
//     return http.post(`/customernurseitem/addItemToCustomer`,params)
// }


// //移除顾客护理等级及其级联移除项目
// export const removeCustomerLevelAndItem=(levelId, customerId)=>{
//      return http.get(`/customernurseitem/removeCustomerLevelAndItem/${levelId}/${customerId}`)
//     }

// //查看客户项目
// export const listCustomerItem=(params)=>{
//     return http.post(`/customernurseitem/listCustomerItem`,params)
// }

// //客户续费
// export const enewNurseItem=(params)=>{
//     return http.post(`/customernurseitem/enewNurseItem`,params)
// }

// //删除客户信息
// export const removeCustomerItem=(id)=>{
//      return http.get(` /customernurseitem/removeNurseItem/${id}`)
//     }


