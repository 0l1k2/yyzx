<template>
     <div class="main">
        <div class="query">
            <table class="query-table">
                <tbody>
                     <tr>
                        <td>
                             <div class="query-form">
                                <el-form :inline="true">
                                    <el-form-item label="楼层">
                                        <el-select v-model="form.floor" >
                                        <el-option label="一楼" value="一楼" />
                                        <el-option label="二楼" value="二楼" />
                                        </el-select>
                                    </el-form-item>
                                </el-form>
                            </div>
                    </td>
                    <td style="width: 120px;">
                    <div>
                        <img src="../../assets/bed/all.png">
                        <span class="pic-text">总床数:</span>{{ count.zcw }}
                    </div>
                </td>
                  <td style="width: 120px;">
                    <div>
                        <img src="../../assets/bed/kx.png">
                        <span class="pic-text">空闲:</span>{{ count.kx }}
                    </div>
                </td>
                 <td style="width: 120px;">
                    <div>
                        <img src="../../assets/bed/yr.png">
                        <span class="pic-text">有人:</span>{{ count.yr }}
                    </div>
                </td>
                 <td style="width: 120px;">
                    <div>
                        <img src="../../assets/bed/wc.png">
                        <span class="pic-text">外出:</span>{{  count.wc }}
                    </div>
                </td>
                </tr>
                </tbody>
            </table>
        </div>
        <div class="map">
            <table border="1px" cellspacing="20px" style="border-collapse: collapse;width: 1200px;">
                <tbody>
                    <tr class="cwsyt_thead">
                        <td></td>
                    </tr>
                </tbody>
            </table>
        </div>
     </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import { findShowBedVo } from '../../api/bedApi.js';

let form=ref({
    floor:"一楼"
}) 
let count=ref({
    zcw:'',
    kx:'',
    yr:'',
    wc:''
})
let listRoom=ref([])

//查询床位房间列表，床位统计信息
const findShowBedVoList =  () => {
   findShowBedVo(form.value.floor).then(res => {
        count.value.zcw = res.data.data.totalBed;
        count.value.kx = res.data.data.freeBed;
        count.value.yr = res.data.data.usedBed;
        count.value.wc = res.data.data.outBed;
        listRoom.value = res.data.data.roomList;
    })
}
onMounted(() => {
    findShowBedVoList();
});
</script>

<style scoped>
.query-form .el-select {
    width: 218px;
}

.main .query {
    height: 60px;
}

.main .query .query-table {
    height: 60px;
}

.main .query .query-table .query-form {
    margin-top: 12px;
}

.main .query .query-table img {
    width: 25px;
    height: 13px;
}

.main .query .query-table .pic-text {
    margin-left: 5px;
    margin-right: 5px;
    font-weight: 700;
}

.main .map {
    margin-top: 3px;
}

.main .map .cwsyt_thead {
    background-color: #0168b7;
    color: white;
    text-align: center;
}

.main .map .cwsyt_tbody {
    height: 140px;
    text-align: center;
}

.main .map .cwsyt_tbody div {
    width: 50px;
    margin-top: 5px;
}

.main .map .cwsyt_tbody div>img {
    width: 100%;
    height: 100%;
    margin-left: 35%;
}

.main .map table>tbody>tr>td {
    border: 1px solid #ddd;
}

a {
    color: #333;
}
</style>