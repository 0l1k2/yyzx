<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <div>
          <el-row :gutter="30">
            <el-col :span="5">
              <el-form-item label="姓名：">
                <el-input
                  v-model="queryParams.customerName"
                  clearable
                />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="入住日期：">
                <el-date-picker
                  value-format="YYYY-MM-DD"
                  format="YYYY/MM/DD"
                  v-model="dateChange"
                  type="daterange"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  range-separator="至"
                />
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-button type="primary" color="#337ab7" icon="Search" @click="query">查询</el-button>
            </el-col>
          </el-row>
        </div>
      </el-header>
      <!-- *******************头部查询************************* -->
      <el-divider style="margin:0;"></el-divider>
      <el-main>
        <div>
          <!-- *************正在使用，使用历史**************** -->
          <el-row class="mb-4">
            <el-button :class="[{activeBtn:btnFlag}]" style="border:1px white solid;margin-left: 0px;"@click="doing">正在使用</el-button>
            <el-button :class="[{activeBtn:!btnFlag}]" style="border:1px white solid;margin-left: 0px;"@click="history">使用历史</el-button>
          </el-row>
        </div>
        <div>
          <el-table :data="bedDetailsList" style="width: 100%;color:black;" >
            <el-table-column align="center" type="index" :index="indexMethod"  label="序号" width="60"/>
            <el-table-column align="center" prop="customerName"  label="客户姓名" width="120"/>
            <el-table-column align="center" prop="customerSex"  label="性别" width="100">
              <template #default="scope">
                {{ scope.row.customerSex === 0 ? '男' : '女' }}
              </template>
            </el-table-column>
            <el-table-column align="center" prop="bedDetails"  label="床位详情" width="120"/>
            <el-table-column align="center" prop="startDate"  label="床位使用起始日期" width="150"/>
            <el-table-column align="center" prop="endDate"  label="床位使用结束日期" width="150"/>
            <el-table-column align="center" fixed="right" label="操作" width="220">
              <template #default="scope">
                <el-button type="warning" link icon="Switch" v-if="btnFlag" size="small" @click="exchange(scope.row)">床位调换</el-button>
                <el-button type="primary" link icon="Edit" v-if="btnFlag" size="small"
                @click="editMessage(scope.row)">修改</el-button>
                <el-button type="danger"  icon="Delete" v-if="!btnFlag" size="small" @click="del(scope.row.id)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div style="margin-top: 15px;">
          <!-- <el-pagination background layout="prev, pager, next" :total="page.total"
          v-model="page.currentPage" v-model:page-size="page.pageSize"
          @current-change="handleCurrentChange"/> -->
        <el-pagination
            background
            layout="prev, pager, next"
            :total="page.total"
            :current-page="page.currentPage"
            :page-size="page.pageSize"
            @current-change="handleCurrentChange"/>

        </div>
      </el-main>
    </el-container>
    <el-dialog
      v-model="exchangeDate.dialogFormVisible"
      title="床位调换"
      width="60%"
      :before-close="exchangeClose"
      align-center
      draggable>
      <el-divider border-style="double" style="margin: 0;"></el-divider>

      <el-form
        :model="exchangeDate.exchangeForm"
        label-position="right"
        label-width="auto"
        :inline="true"
        style="margin: 20px auto;"
        ref="exchangeForm"
      >
        <el-form-item label="客户姓名" prop="customerName">
          <el-input readonly v-model="exchangeDate.exchangeForm.customerName" />
        </el-form-item>

        <el-form-item label="旧床位详情" prop="bedDetails">
          <el-input readonly v-model="exchangeDate.exchangeForm.bedDetails" />
        </el-form-item>

        <el-form-item label="性别" prop="customerSex">
          <el-input
            readonly
            :value="exchangeDate.exchangeForm.customerSex == 0 ? '男' : '女'"
          />
        </el-form-item>

        <el-form-item label="新楼号" prop="buildingId">
          <el-input readonly v-model="exchangeDate.exchangeForm.buildingId" />
        </el-form-item>

        <el-form-item label="新房号" prop="newRoomNo">
          <el-select
            v-model="exchangeDate.exchangeForm.newRoomNo"
            placeholder="请选择房间号"
            style="width: 200px;"
            @change="getBed"
          >
            <el-option-group
              v-for="group in exchangeDate.roomList"
              :key="group.label"
              :label="group.label"
            >
              <el-option
                v-for="item in group.options"
                :key="item.roomNo"
                :label="item.roomNo"
                :value="item.roomNo"
              />
            </el-option-group>
          </el-select>
        </el-form-item>

        <el-form-item label="当前床位使用起始日期" prop="startDate">
          <el-input readonly v-model="exchangeDate.exchangeForm.startDate" />
        </el-form-item>

        <!-- <el-form-item label="新床号" prop="newBedId">
          <el-select
            v-model="exchangeDate.exchangeForm.newBedId"
            placeholder="请选择床位"
            style="width: 200px;"
          />
        </el-form-item> -->
        <!-- <el-form-item label="新床号" prop="newBedId">
  <el-select
    v-model="exchangeDate.exchangeForm.newBedId"
    placeholder="请选择床位"
    style="width: 200px;">
    <el-option
      v-for="bed in exchangeDate.bedList"
      :key="bed.bedId" :label="bed.bedNo" :value="bed.bedId"
    />
  </el-select>
</el-form-item> -->
<el-form-item label="新床号:" prop="newBedId">
  <el-select 
    v-model="exchangeDate.exchangeForm.newBedId" 
    placeholder="请选择床位"
    style="width: 200px"
  >
    <el-option 
      v-for="item in exchangeDate.bedList" 
      :key="item.id" 
      :label="item.bedNo"
      :value="item.id"
    ></el-option>
  </el-select>
</el-form-item>


        <el-form-item label="当前床位使用结束日期" prop="endDate">
          <el-input readonly v-model="exchangeDate.exchangeForm.endDate" />
        </el-form-item>
      </el-form>

      <el-divider border-style="double" style="margin: 0;"></el-divider>

      <template #footer>
        <span class="dialog-footer" style="margin-top: 0px;">
          <el-button @click="exchangeSave">保 存</el-button>
          <el-button type="primary" @click="exchangeCancle">取 消</el-button>
        </span>
      </template>
    </el-dialog>



    <!-- 修改对话框 -->
      <el-dialog
      v-model="edit.dialogVisible"
      title="信息修改"
      width="60%"
      :before-close="editClose"
      align-center
      draggable>
      <el-divider border-style="double" style="margin: 0;"></el-divider>
      <el-form
        :model="edit.bedDetailsForm"
        :rules="rules"
        label-position="right"
        label-width="auto"
        class="demo-form-inline"
        :inline="true"
        style="max-width:800px; margin: 20px auto;"
        ref="editForm">
        <el-form-item label="客户姓名" prop="customerName">
          <el-input readonly v-model="edit.bedDetailsForm.customerName" />
        </el-form-item>


        <el-form-item label="性别" prop="customerSex">
          <el-input
            readonly
            :value="edit.bedDetailsForm.customerSex == 0 ? '男' : '女'"
          />
        </el-form-item>
           <el-form-item label="床位详情" prop="bedDetails">
          <el-input readonly v-model="edit.bedDetailsForm.startDate" />
        </el-form-item>

        <el-form-item label="床位使用起始日期" prop="startDate">
          <el-input readonly v-model="edit.bedDetailsForm.startDate" />
        </el-form-item>

        <el-form-item label="床位使用结束日期" prop="endDate">
          <el-date-picker  v-model="edit.bedDetailsForm.endDate" style="width:200px;"
          format="YYYY/MM/DD" value-format="YYYY-MM-DD" type="date" placehoder="选择日期"/>
        </el-form-item>
      </el-form>

      <el-divider border-style="double" style="margin: 0;"></el-divider>

      <template #footer>
        <span class="dialog-footer" style="margin-top: 0px;">
          <el-button type="primary" @click="editSave()">保 存</el-button>
          <el-button  @click="editCancle">取 消</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { listRoom } from '../../api/roomApi';
import { listBeddetails,findBedByRoom,exchangeBed,updateDetailsStartDate, delBedDetails } from '../../api/bedApi';
import { computed, nextTick, onMounted,ref } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus'

let btnFlag = ref(true); //按钮状态
let page =ref({
  total: 0,
  pageSize: 6,
  currentPage: 1,
//   pageCount: 0
})

//查询房间列表
const getRoomList =()=>{
  listRoom().then(res=>{
    //构建房间列表数据
    let floor1=res.data.filter((item,i,arr)=>{
      return item.roomFloor=="一楼"
    })
    let floor2=res.data.filter((item,i,arr)=>{
      return item.roomFloor=="二楼"
    })
    exchangeDate.value.roomList=[
      {label:"一楼",options:floor1},
      {label:"二楼",options:floor2}
    ]
  })
}

let queryParams = ref({
  "currentPage": 1,
  "pageSize": 6,
  "customerName": "",
  "startDate": "",
  "endDate": "",
  "isDeleted": 0
})
//查询床位详情信息-分页
let bedDetailsList = ref([]);
// const findbedDetailsList = () => {
//   listBeddetails(queryParams.value).then(res => {
//     console.log(res.data);
//     bedDetailsList.value = res.data.records;
//     page.value.total = res.data.total;
//     page.value.pageCount = res.data.pages;
//     page.value.currentPage = res.data.currentPage;
//     page.value.pageSize = res.data.size;
   
//   })

// }
const findbedDetailsList = () => {
  listBeddetails(queryParams.value).then(res => {
    bedDetailsList.value = res.data.records;
    page.value.total = res.data.total;
    page.value.pageCount=res.data.pages;
    page.value.currentPage = res.data.currentPage;
    page.value.pageSize = res.data.size;

    // 如果当前页大于最大页数，自动跳转到最后一页
    if (res.data.currentPage > res.data.pages && res.data.pages > 0) {
      page.value.currentPage = res.data.pages;
      queryParams.value.currentPage = res.data.pages;
      findbedDetailsList(); // 重新请求最后一页数据
    }
  });
}

//序号计算
const indexMethod= computed(()=>{
  return page.value.currentPage*page.value.pageSize- page.value.pageSize + 1;
})

//查询
const dateChange = ref([]);//日期区间
const query=()=>{
  if(dateChange.value != null){
    //如果输入了日期
    queryParams.value.startDate = dateChange.value[0];
    queryParams.value.endDate = dateChange.value[1];
  }else{
    //如果没有输入日期
    queryParams.value.startDate = "";
    queryParams.value.endDate = "";
  }
  queryParams.value.currentPage = 1; //重置当前页为1
  //重载表格
  findbedDetailsList();
}


//分页查询操作
const handleCurrentChange =(curPage)=>{
  queryParams.value.currentPage = curPage;
  page.value.currentPage = curPage;
  //重新渲染表格
  findbedDetailsList();
}

//床位调换
let exchangeDate = ref({
  dialogFormVisible: false,
  exchangeForm:{
    customerName:"",
    customerSex:"",
    bedDetails:"",
    startDate:"",
    id:"",
    customerId:"",
    buildingId:606,
    oldBedId:"",
    newRoomNo:"",
    newBedId:"",
    oldRoomNo:"",
    endDate:"",
  },
  roomList:[],
  bedList:[]
})

//点击床位调换
// const exchange = (row) =>{
//   exchangeDate.value.dialogFormVisible = true;
//   //初始化菜单
//   nextTick(()=>{
//     exchangeDate.value.exchangeForm.customerName = row.customerName;
//     exchangeDate.value.exchangeForm.customerSex = row.customerSex;
//     exchangeDate.value.exchangeForm.bedDetails = row.bedDetails;
//     exchangeDate.value.exchangeForm.startDate = row.startDate;
//     exchangeDate.value.exchangeForm.id = row.id;
//     exchangeDate.value.exchangeForm.customerId = row.customerId;
//     exchangeDate.value.exchangeForm.buildingId = row.buildingId; // 默认值
//     exchangeDate.value.exchangeForm.oldBedId = row.oldBedId;
//     exchangeDate.value.exchangeForm.oldRoomNo = row.oldRoomNo;
//     exchangeDate.value.exchangeForm.endDate = row.endDate;

//     // 清空新床位相关字段（避免沿用之前的数据）
//     exchangeDate.value.exchangeForm.newRoomNo = '';
//     exchangeDate.value.exchangeForm.newBedId = '';

//   })
//   getRoomList();
// }
const exchange = (row) =>{
  exchangeDate.value.dialogFormVisible = true;
  // Initialize form
  nextTick(()=>{
    exchangeDate.value.exchangeForm.customerName = row.customerName;
    exchangeDate.value.exchangeForm.customerSex = row.customerSex; // Note: You had 'customersex' in your instructions, but 'customerSex' in the code. Sticking to 'customerSex'.
    exchangeDate.value.exchangeForm.bedDetails = row.bedDetails;
    exchangeDate.value.exchangeForm.startDate = row.startDate;
    exchangeDate.value.exchangeForm.endDate = row.endDate; // Added this line based on your instruction
    exchangeDate.value.exchangeForm.id = row.id;
    exchangeDate.value.exchangeForm.customerId = row.customerId;
    exchangeDate.value.exchangeForm.oldRoomNo = row.roomNo; 
    exchangeDate.value.exchangeForm.newRoomNo = ''; 
         
    exchangeDate.value.exchangeForm.oldBedId = row.bedId; 
    exchangeDate.value.exchangeForm.newBedId = ""; })
  getRoomList();
}

//根据选择的房间号查询床位
const getBed=()=>{
  console.log("当前房间号:", exchangeDate.value.exchangeForm.newRoomNo);
    //清空床位下拉列表
    exchangeDate.value.bedList=[];
    exchangeDate.value.exchangeForm.newBedId="";
    findBedByRoom({
      bedStatus:1,roomNo: exchangeDate.value.exchangeForm.newRoomNo
    }).then(res=>{
      exchangeDate.value.bedList=res.data;
    })
}


const exchangeSave=()=>{
  console.log(JSON.stringify(exchangeDate.value.exchangeForm));
  if(
     exchangeDate.value.exchangeForm.newRoomNo!="" && exchangeDate.value.exchangeForm.newRoomNo!=null&&
     exchangeDate.value.exchangeForm.newBedId !="" && exchangeDate.value.exchangeForm.newBedId!=null

  ){
    exchangeBed(exchangeDate.value.exchangeForm).then(res=>{
      if(res.flag){
         ElMessage({
         message: res.message,
         type: 'success',
        })
        exchangeClose();
        findbedDetailsList();
      }else{
        ElMessage({
         message: res.message,
         type: 'error',
        })
      }
    })
  }else{
     ElMessage({
         message: "请选择新房间号和床位",
         type: 'error',
        })
  }
}
//关闭床位调换对话框
const exchangeClose=()=>{
  exchangeDate.value.dialogFormVisible=false;
  
  resetForm("exchangeForm");
}

//表单引用
const exchangeForm=ref(null);
const editForm=ref(null);
//重置表单
// const resetForm=(formName)=>{
//     if(formName.value){
//       formName.value.resetFields();
//     }
// }

//点击床位调换取消按钮
const exchangeCancle=()=>{
  exchangeClose();
}

//修改duihuak
let edit=ref({
  dialogVisible: false,
  bedDetailsForm:{
    id:"",
    customerName:"",
    customerSex:"",
    bedDetails:"",
    roomNo:"",
    startDate:"",
    endDate:""
  }
})
let rules=ref({
  endDate:[
    {require: true,message:'请选择床位结束使用日期',trigger: 'change'}
  ]
})
const editMessage=(row)=>{
  edit.value.dialogVisible=true;
  nextTick(()=>{
    edit.value.bedDetailsForm.id=row.id;
    edit.value.bedDetailsForm.customerName=row.customerName;
    edit.value.bedDetailsForm.customerSex=row.customerSex;
    edit.value.bedDetailsForm.bedDetails=row.bedDetails;
    edit.value.bedDetailsForm.startDate=row.startDate;
    edit.value.bedDetailsForm.endDate=row.endDate;
    edit.value.bedDetailsForm.roomNo=row.roomNo
  })
}

const editClose=()=>{
  edit.value.dialogVisible=false;
  //重置表单
  resetForm("editForm");
}
const resetForm = (formName) => {
  if (formName.value) {
    formName.value.resetFields();
  }
};
// 用法：resetForm(editForm)
//点击对话框取消按钮
const editCancle=()=>{
  editClose();
}
const editSave = () => {
  editForm.value.validate((valid) => {
    if (valid) {
      updateDetailsStartDate(edit.value.bedDetailsForm).then(res => {
        if (res.flag) {
          ElMessage({
            message: res.message,
            type: 'success',
          });
          editClose();
          findbedDetailsList();
        } else {
          ElMessage({
            message: res.message,
            type: 'error',
          });
        }
      });
    }
  });
};

//使用历史
const history=()=>{
  btnFlag.value=false;
  queryParams.value.isDeleted=1;
  queryParams.value.currentPage=1;//重置当前页为1
  findbedDetailsList();
}
//删除
const del=(id)=>{
  ElMessageBox.confirm(
    '此操作删除记录，是否删除?',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    }
  ).then(() => {
    delBedDetails(id).then(res=>{
      if(res.flag){
        ElMessage({
        type: 'success',
        message: res.message,
      })
      //调用findBedDtails
      findbedDetailsList();
      }
      else{
        ElMessage({
        type: 'error',
        message: res.message,
           })
            console.log('删除失败:', res);
      }
    })   
    })
}
//正在使用
const doing=()=>{
  btnFlag.value=true;
  queryParams.value.isDeleted=0;
  queryParams.value.currentPage=1;
   findbedDetailsList();
}


onMounted(()=>{
  // getRoomList();
  findbedDetailsList()
})
</script>

<style scoped>
.activeBtn{
  color:#1890ff;
  background-color: rgb(232, 244, 255);
}
</style>