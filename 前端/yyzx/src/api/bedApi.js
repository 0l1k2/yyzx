import http from "../request/request.js";

//获取床位示意图
export const findShowBedVo=(param)=>{
    return http.get(`/room/findShowBedVo/${param}`)
}