import http from "../request/request.js";

//获取床位示意图
export const findShowBedVo=(params)=>{
    return http.get(`/room/findShowBedVo/${params}`)
}

//查询房间详情信息
export const listBeddetails=(params)=>{
    return http.post('/beddetails/listBeddetails',params)
}
// /yyzx/beddetails/listBeddetails