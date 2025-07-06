import http from "../request/request.js";


//添加
export const addNurseItem=(params)=>{
    return http.post(`/nursecontent/addNurseContent`,params)
}
// 更新
export const updateNurseItem=(params)=>{
    return http.post(`/nursecontent/updateNurseItem`,params)
}

//查询
export const findNurseItem=(params)=>{
    return http.post(`/nursecontent/findNurseItemPage`,params)
}

//删除
export const delNurseItem=(id)=>{
     return http.get(`/nursecontent/delNurseItem/${id}`)
    }

