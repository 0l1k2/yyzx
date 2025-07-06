import http from "../request/request.js";


//添加
export const addMeal=(params)=>{
    return http.post(`/meal/addMeal`,params)
}
// 更新
export const updateMeal=(params)=>{
    return http.post(`/meal/updateMeal`,params)
}

//查询
export const findMeal=(params)=>{
    return http.post('/meal/listMealPage',params)
}

//删除
export const delMeal=(id)=>{
     return http.get(`/meal/removeMeal/${id}`)
    }
