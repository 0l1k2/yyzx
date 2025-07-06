import http from "../request/request.js";


//添加
export const addNurseLevel=(params)=>{
    return http.post(`/nurselevel/addNurseLevel`,params)
}
//为项目添加级别
export const addItemToLevel=(params)=>{
    return http.post(`/nurselevel/addItemToLevel`,params)
}
// 更新
export const updateNurseLevel=(params)=>{
    return http.post(`/nurselevel/updateNurseLevel`,params)
}

//查询
export const listNurseLevel=(params)=>{
    return http.post('/nurselevel/listNurseLevel',params)
}

//删除
export const removeNurseLevel=({levelId,itemId})=>{
     return http.get(`/nurselevel/removeNurseLevelItem/${levelId}/${itemId}`)
    }

 //根据护理级别查询护理信息
export const listNurseItemByLevel=(levelId)=>{
    return http.get(`/nurselevel/listNurseItemByLevel/${levelId}`)
}


    
