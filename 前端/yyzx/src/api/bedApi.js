import http from "../request/request.js";

//获取床位示意图
export const findShowBedVo=(params)=>{
    return http.get(`/room/findShowBedVo/${params}`)
}

//查询房间详情信息
export const listBeddetails=(params)=>{
    return http.post(`/beddetails/listBeddetails`,params)
}
// /yyzx/beddetails/listBeddetails

//根据房间号查询床位信息
export const findBedByRoom=(params)=>{
    return http.post(`/bed/findBed`,params)
}


export const exchangeBed=(params)=>{
    return http.post(`/beddetails/exchangeBed`,params)
}

export const updateDetailsStartDate=(params)=>{
    return http.post(`beddetails/updateBeddetails`,params)
}


export const delBedDetails=(id)=>{
    return http.get(`/beddetails/delBeddetails/${id}`)
}
