<template>
    <div class="common-layout">
        <el-container>
            <el-header>
                <div>
                    <el-row :gutter="30">
                        <el-col :span="7">
                            <el-input placeholder="客户姓名" v-model="condition.customerName" clearable @clear="query"
                                size="large">
                                <template #append>
                                    <el-button type="info" @click="query" style="color:black">查询</el-button>
                                </template>
                            </el-input>
                        </el-col>
                        <el-col :span="17">
                            <el-button type="primary" @click="dj" color="#337ab7" style="margin-top:4px">
                                <el-icon>
                                    <Plus />
                                </el-icon>
                                <span>登记</span>
                            </el-button>
                        </el-col>
                    </el-row>
                </div>
            </el-header>
            <el-divider style="margin:0"></el-divider>
            <el-main>
                <div>
                    <el-row class="mb-4">
                        <el-button :class="[{ activeBtn: btnFlag }]" style="border: 1px white solid;margin-left: 0px"
                            @click="selfCareMan">自理老人</el-button>
                        <el-button :class="[{ activeBtn: !btnFlag }]" style="border: 1px white solid;margin-left: 0px"
                            @click="careMan">护理老人</el-button>
                    </el-row>
                </div>
                <div>
                    <!-- 表格 -->
                    <el-table :data="khxxList" style="width: 100% ;color:black;" stripe>
                        <el-table-column align="center" fixed type="index" :index="indexMethod" label="序号" width="60" />
                        <el-table-column align="center" fixed prop="customerName" label="客户姓名" width="100" />
                        <el-table-column align="center" prop="bloodType" label="血型" width="60" />
                        <el-table-column align="center" prop="contactTel" label="联系电话" width="150" />
                        <el-table-column align="center" prop="customerAge" label="年龄" width="60" />
                        <el-table-column align="center" prop="customerSex" label="性别" width="60">
                            <template #default="scope">{{ scope.row.customerSex == 0 ? '男' : '女' }}</template>
                        </el-table-column>
                        <el-table-column align="center" prop="familyMember" label="家属" width="100" />
                        <el-table-column align="center" prop="idcard" label="身份证号" width="180" />
                        <el-table-column align="center" prop="buildingNo" label="所属楼房" width="80" />
                        <el-table-column align="center" prop="roomNo" label="房间号" width="80" />
                        <el-table-column align="center" prop="bedNo" label="床号" width="80" />
                        <el-table-column align="center" prop="birthday" label="出生日期" width="150" />
                        <el-table-column align="center" prop="checkinDate" label="入住时间" width="150" />
                        <el-table-column align="center" prop="expirationDate" label="合同到期时间" width="150" />
                        <el-table-column align="center" prop="levelName" label="护理级别" width="80" />
                        <el-table-column align="center" prop="nickName" label="健康管家(护工)" width="120" />
                        <el-table-column align="center" prop="psychosomaticState" label="身心状况" width="200" />
                        <el-table-column align="center" fixed="right" label="操作" width="180">
                            <template #default="scope">
                                <el-button type="danger" link size="small"
                                    v-if="scope.row.isDeleted == 1">逻辑上已删除,只查询不操作</el-button>
                                <el-button type="danger" link v-if="scope.row.isDeleted == 0"
                                    @click="del(scope.row)">删除</el-button>
                                <el-button type="primary" link v-if="scope.row.isDeleted == 0"
                                    @click="edit(scope.row)">修改</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
                <!-- 分页插件 -->
                <div style="margin-top:15px">
                    <!-- 
              page-size:每页显示条目个数
              current-page:页码 配合@current-change使用
              disabled:是否禁用分页 
              background:是否为分页按钮添加背景色
              layout:组件布局，子组件名用逗号分隔
              total:总条目数
          -->
                    <el-pagination :page-size="page.pageSize" background :current-page="page.currentPage"
                        layout=" prev, pager, next" :total="page.total" @current-change="handleCurrentChange" />
                </div>
            </el-main>
            <!-- 模态框 :入住登记-->
            <el-dialog v-model="djDialog.dialogVisible" :title="djDialog.tops" width="60%" align-center draggable
                :before-close="djhandleClose">
                <el-divider border-style="double" style="margin:0;" />
                <el-form label-position="right" label-width="auto" style="max-width:800px;margin:20px auto"
                    class="demo-form-inline" ref="customerForm" :inline="true" :model="djDialog.customerForm"
                    :rules="rules">
                    <el-form-item label="客户姓名：" prop="customerName">
                        <el-input placeholder="输入[客户姓名]" v-model="djDialog.customerForm.customerName" />
                    </el-form-item>
                    <el-form-item label="楼栋： " prop="buildingNo">
                        <el-input readonly v-model="djDialog.customerForm.buildingNo" />
                    </el-form-item>
                    <el-form-item label="年龄：" prop="customerAge">
                        <el-input v-model="djDialog.customerForm.customerAge" />
                    </el-form-item>

                    <el-form-item label="房间号： " prop="roomNo">
                        <el-input readonly v-if="isShow" v-model="djDialog.customerForm.roomNo" />
                        <el-select v-if="!isShow" v-model="djDialog.customerForm.roomNo" style="width:200px"
                            @change="getBed" placeholder="请选择房间号">
                            <el-option-group v-for="group in djDialog.roomList" :key="group.label" :label="group.label">
                                <el-option v-for="item in group.options" :key="item.roomNo" :label="item.roomNo"
                                    :value="item.roomNo"></el-option>
                            </el-option-group>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="性别：" prop="customerSex">
                        <el-select v-model="djDialog.customerForm.customerSex" style="width:200px" placeholder="性别">
                            <el-option v-for="item in djDialog.sex" :key="item.value" :label="item.label"
                                :value="item.value" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="床位号： " prop="bedId">
                        <el-input readonly v-if="isShow" v-model="djDialog.customerForm.bedId" />
                        <el-select v-if="!isShow" style="width:200px" v-model="djDialog.customerForm.bedId"
                            placeholder="请选择床位">
                            <el-option v-for="item in djDialog.bedList" :key="item.bedNo" :label="item.bedNo"
                                :value="item.id"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item label="出生日期： " prop="birthday">
                        <el-date-picker @change="getAge" style="width:200px" format="YYYY/MM/DD"
                            value-format="YYYY-MM-DD" v-model="djDialog.customerForm.birthday" type="date"
                            placeholder="选择日期"></el-date-picker>
                    </el-form-item>
                    <el-form-item label="入住时间： " prop="checkinDate">
                        <el-date-picker style="width:200px" format="YYYY/MM/DD" value-format="YYYY-MM-DD"
                            v-model="djDialog.customerForm.checkinDate" type="date" :disabled-date="disabledDate"
                            placeholder="选择日期"></el-date-picker>
                    </el-form-item>
                    <el-form-item label="身份证号：" prop="idcard">
                        <el-input v-model="djDialog.customerForm.idcard" />
                    </el-form-item>
                    <el-form-item label="合同到期时间： " prop="expirationDate">
                        <el-date-picker style="width:200px" v-model="djDialog.customerForm.expirationDate" type="date"
                            format="YYYY/MM/DD" value-format="YYYY-MM-DD" :disabled-date="disabledDate0"
                            placeholder="选择日期"></el-date-picker>
                    </el-form-item>
                    <el-form-item label="血型：" prop="bloodType">
                        <el-input v-model="djDialog.customerForm.bloodType" />
                    </el-form-item>
                    <el-form-item label="联系电话：" prop="contactTel">
                        <el-input v-model="djDialog.customerForm.contactTel" />
                    </el-form-item>
                    <el-form-item label="家属：" prop="familyMember">
                        <el-input v-model="djDialog.customerForm.familyMember" />
                    </el-form-item>
                    <el-form-item label="身心状况： " prop="psychosomaticState">
                        <el-input v-model="djDialog.customerForm.psychosomaticState" />
                    </el-form-item>
                </el-form>
                <el-divider border-style="double" style="margin:0;" />
                <template #footer>
                    <span class="dialog-footer" style="padding-top: 0px">
                        <el-button type="primary" @click="djsave('customerForm')">保存</el-button>
                        <el-button v-if="!djDialog.customerForm.id" type="primary"
                            @click="resetForm('customerForm')">重置</el-button>
                        <el-button @click="djcancel">取消</el-button>
                    </span>
                </template>
            </el-dialog>
        </el-container>
    </div>
</template>

<!-- <script setup>

<script> -->



<script setup>
import {
    selectCustomer, addCustomer, editCustomer, delCustomer
} from '../../api/customerApi';
import { ElMessage, ElMessageBox } from 'element-plus'
import { ref, reactive, onMounted, computed } from 'vue'
import { listRoom } from '../../api/roomApi';
import { findBedByRoom } from '../../api/bedApi';
import { id } from 'element-plus/es/locales.mjs';


//编辑和新增使用同一个对话框。isshow用来判别展示不同的标签
let isShow = ref(false)

// 查询条件
let condition = ref({
    customerName: "",
    currentPage: 1,
    pageSize: 6,
    manType: 1 //1-自理老人   2-护理老人  3-历史老人
})

// 分页信息
let page = ref({
    currentPage: 1,
    pageSize: 6,
    total: 0,
    pageCount: 0
})
// 查询客户信息
const query = () => {
    condition.value.currentPage = 1;
    //入住登记，客户信息列表（分页）
    //************************* */
    getKhxxList();
}


const dj = () => {
    isShow.value = true
    //查询房间列表
    getRoomList()
    djDialog.value.dialogVisible = true
    djDialog.value.tops = "入住登记"

}


//点击登录按钮


// 按钮激活状态,点击每个按钮改一下状态
let btnFlag = ref(true)
// let btnFlag3=ref(false)


//客户信息列表
let khxxList = ref([])

// 删除客户
const del = (row) => {
    ElMessageBox.confirm(
        '此操作删除记录，是否删除?',
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    ).then(() => {
        delBedDetails({ id: row.id, bedId: row.bedId }).then(res => {
            if (res.flag) {
                ElMessage({
                    type: 'success',
                    message: res.message,
                })
                //调用findBedDtails
                getKhxxList();
            }
            else {
                ElMessage({
                    type: 'error',
                    message: res.message,
                })
            }
        })
    })
}

//查询房间列表
const getRoomList = () => {
    listRoom().then(res => {
        //构建房间列表数据
        let floor1 = res.data.filter((item, i, arr) => {
            return item.roomFloor == "一楼"
        })
        let floor2 = res.data.filter((item, i, arr) => {
            return item.roomFloor == "二楼"
        })
        djDialog.value.roomList = [
            { label: "一楼", options: floor1 },
            { label: "二楼", options: floor2 }
        ]
    })
}
//根据选择的房间号查询床位
const getBed = () => {
    //清空床位下拉列表
    djDialog.value.bedList = [];
    djDialog.value.customerForm.newBedId = "";
    findBedByRoom({
        bedStatus: 1, 
        // roomNo: djDialog.value.djDialog.newRoomNo
        roomNo: djDialog.value.customerForm.roomNo
    }).then(res => {
        djDialog.value.bedList = res.data;
    })
}

// 修改客户信息
const edit = (row) => {
    djDialog.dialogVisible = true
    djDialog.tops = '修改客户信息'
    isShow.value = true
    Object.assign(djDialog.customerForm, row)
    
}

// 入住登记对话框/信息编辑对话框模态框数据
let djDialog = ref({
    dialogVisible: false,
    tops: '入住登记',
    customerForm: {
        id: "",
        bedId: "",
        bloodType: "",
        buildingNo: "606",
        contactTel: "",
        customerAge: "",
        customerName: "",
        customerSex: 0,
        roomNo: "",
        idcard: "",
        psychosomaticState: "",
        familyMember: "",
        filepath: "../../assets/tou.png",
        birthDate: "",
        checkinDate: "",
        expirationDate: ""
    },
    roomList: [],
    bedList: [],
    sex: [
        {
            value: 0,
            label: "男"
        },
        {
            value: 1,
            label: "女"
        }
    ]
})

// 表单校验规则
let rules = ref([{
    bedId: [{ required: true, message: "选择床位", trigger: "change" }],
    bloodType: [{ required: true, message: "请输入血型", trigger: "blur" }],
    contactTel: [{ required: true, message: "请输入手机号", trigger: "blur" }],
    customerAge: [{ required: true, message: "请输入年龄", trigger: "blur" }],
    customerName: [{ required: true, message: "请输入客户姓名", trigger: "blur" }],
    roomNo: [{ required: true, message: "请选择房间号", trigger: "change" }],
    idcard: [{ required: true, message: "请输入身份证号", trigger: "blur" }],
    familyMember: [{ required: true, message: "请输入家属", trigger: "blur" }],
    checkinDate: [{
        type: "date",
        required: true,
        message: "请选择时间",
        trigger: "change"
    }
    ], expirationDate: [{
        type: "date",
        required: true,
        message: "请选择时间",
        trigger: "change"
    }
    ]
}
])

//保存按钮
// 保存客户信息
const djsave = (formName) => {
    customerForm.value.validate(async (valid) => {
        if (!valid) return

        try {
            // 判断是新增还是修改
            if (djDialog.customerForm.id) {
                await editCustomer(djDialog.customerForm)
            } else {
                await addCustomer(djDialog.customerForm)
            }

            ElMessage.success('保存成功')
            djDialog.dialogVisible = false
            query()
        } catch (error) {
            ElMessage.error('保存失败：' + error.message)
        }
    })
}
// 重置表单
const customerForm = ref(null)
const resetForm = (formRef) => {
    if (formRef.value) {
        formRef.value.resetFields();
    }
};


// 取消按钮操作
const djcancel = () => {
    djDialog.dialogVisible = false
    //重置表单
    resetForm("customerForm")
    //将id初始化
    djDialog.customerForm.id = ""
}

//自理老人
const selfCareMan = () => {
    btnFlag.value = true,
        condition.value.manType = "1",
        condition.pageSize = 1
    //重新渲染表格数据
     getKhxxList()

}
//护理老人
const careMan = () => {
    btnFlag.value = false,
        condition.value.manType = "2",
        condition.pageSize = 1
    //重新渲染表格数据
     getKhxxList()

}
const handleCurrentChange = (current) => {
    condition.value.currentPage = current
    getKhxxList()
}
const getKhxxList = () => {
    selectCustomer(condition.value).then(res => {
        khxxList.value = res.data.records
        page.value.total = res.data.total;
        page.value.pageCount = res.data.pages;
        page.value.currentPage = res.data.current;
        page.value.pageSize = res.data.size;
    })
}

//序号计算
const indexMethod = computed(() => {
    console.log("************");
    console.log(page.value.currentPage + "ewq");
    
    return page.value.currentPage * page.value.pageSize - page.value.pageSize + 1;
})



// 组件挂载时加载数据
onMounted(() => {
    getKhxxList()
    query() 
})
</script>

<style scoped>
.activeBtn {
    color: #1890ff;
    background: rgb(232, 244, 255);
}
</style>