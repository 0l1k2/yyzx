<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <div>
          <el-row :gutter="30">
            <el-col :span="7">
              <el-input
                placeholder="客户姓名"
                @clear="query"
                v-model="condition.customerName"
                clearable
                size="large"
              >
                <template #append>
                  <el-button type="info" @click="query" style="color:black">查询</el-button>
                </template>
              </el-input>
            </el-col>
          </el-row>
        </div>
      </el-header>
      <el-divider style="margin:0"></el-divider>
      <el-main>
        <el-row :gutter="20">
          <el-col :span="9">
            <div class="table-main">
              <div class="table-main-header">客户信息</div>
              <!-- 表格 -->
              <el-table
                :data="khxxList"
                highlight-current-row
                @current-change="handleChangeCustomer"
                size="small"
                style="width: 100% ;color:black;"
                stripe
              >
                <el-table-column
                  align="center"
                  fixed
                  type="index"
                  :index="indexMethod"
                  label="序号"
                  width="40"
                />
                <el-table-column align="center" prop="customerName" label="姓名"  />
                <el-table-column align="center" prop="customerAge" label="年龄"/>
                <el-table-column align="center" prop="customerSex" label="性别">
                  <template #default="scope">{{ scope.row.customerSex==0?'男':'女'}}</template>
                </el-table-column>
                <el-table-column align="center" prop="bedNo" label="床号" width="90" />
                <el-table-column align="center" prop="levelName" label="护理级别" />
              </el-table>
              <!-- 分页插件 -->
              <div style="margin-top:15px">
                <el-pagination
                  :page-size="page.pageSize"
                  background
                  :current-page="page.currentPag"
                  layout=" prev, pager, next"
                  :total="page.total"
                  @current-change="handleCurrentChange"
                />
              </div>
            </div>
          </el-col>
          <el-col :span="15">
            <div class="table-main">
              <div class="table-main-header">护理记录</div>
              <!-- 表格 -->
              <el-table :data="nurseRecordList" size="small">
                <el-table-column
                  align="center"
                  :index="indexMethodRecord"
                  type="index"
                  label="序号"
                  width="40"
                />
                <el-table-column align="center" prop="serialNumber" label="护理项目编号" width="100" />
                <el-table-column align="center" prop="nursingName" label="护理项目名称" width="100" />
                <el-table-column align="center" prop="nursingCount" label="护理数量" width="70" />
                <el-table-column align="center" prop="nursingContent" label="护理内容" width="100" />
                <el-table-column align="center" prop="nickname" label="护理人员" width="80" />
                <el-table-column align="center" prop="phoneNumber" label="护理人员号码" width="100" />
                <el-table-column align="center" prop="nursingTime" label="护理时间" width="100" />
                
                <el-table-column align="center" label="操作" width="100">
                  <template #default="scope">
                    <el-button type="danger" size="small" round plain @click="del(scope.row.id)">移除</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <!-- 分页插件 -->
              <div style="margin-top:15px" v-if="nurseRecordList.length>0">
                <el-pagination
                  :page-size="pageRecord.pageSize"
                  background
                  :current-page="pageRecord.currentPag"
                  layout=" prev, pager, next"
                  :total="pageRecord.total"
                  @current-change="handleRecordChange"
                />
              </div>
            </div>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>
<!-- 
<script>
import { getKhxxList } from "@/api/customerApi.js";
import { listNurseRecordsVo ,removeCustomerRecord} from "@/api/nurseRecord.js";

export default {
  computed: {
    //分页序号不重修 -客户
    indexMethod() {
      return this.page.currentPag * this.page.pageSize - this.page.pageSize + 1;
    },
      //分页序号不重修 -护理记录
      indexMethodRecord () {
      return (
        this.pageRecord.currentPag * this.pageRecord.pageSize -
        this.pageRecord.pageSize +1
      );
    },
  },
  data() {
    return {
      //分页属性封装 --客户
      page: {
        total: 0,
        pageSize: 6,
        currentPag: 1,
        pagCount: 0
      },
        //分页属性封装 --护理记录
        pageRecord: {
        total: 0,
        pageSize: 6,
        currentPag: 1,
        pagCount: 0
      },
      nurseRecordList:[],
      khxxList: [],
      //查询条件封装--客户
      condition: {
        customerName: "",
        pageSize: "1" //默认第一页
      },
      //查询条件封装--护理记录
      conditionRecord: {
        customerId: "", //客户编号
        pageSize: "1" //默认第一页
      },
      
    };
  },
  methods: {
    //点击查询
    query() {
      //清空护理记录数据表
      this.nurseRecordList=[]
      this.conditionRecord.customerId = "";

      this.condition.pageSize = "1"; //回到第一页
      this.getKhxxList();
    },
    //选中页码-客户
    handleCurrentChange(curPage) {
      this.page.currentPag = curPage;
      this.condition.pageSize = curPage; //参数pageSize是服务端接收页码参数名
      //重新渲染表格
      this.getKhxxList();
      //清空护理记录数据表
      this.nurseRecordList=[]
      this.conditionRecord.customerId = "";
    },
   
    //选中某客户行:获取用户的服务项目列表
    handleChangeCustomer(row) {
      if (row != null) {
        //点击页码会清空row因此做出判断逻辑
        //构建查询条件
        this.conditionRecord.customerId = row.id;
        this.listNurseRecordsVo();
      }
    },
    //选中页码-护理记录
    handleRecordChange(curPage) {
      this.pageRecord.currentPag = curPage;
      this.conditionRecord.pageSize = curPage; //参数pageSize是服务端接收页码参数名
      //重新渲染表格:
      this.listCustomerItem();
    },
    
    //api-查询客户信息列表-分页
    getKhxxList() {
      getKhxxList(this.condition).then(res => {
        this.khxxList = res.data.records;
        this.page.total = res.data.total; //总记录数
        this.page.pageSize = res.data.size; //每页显示条数
        this.page.currentPag = res.data.current; //当前页码
        this.page.pagCount = res.data.pages; //总页数
      });
    },
    //api-查询客户护理记录
    listNurseRecordsVo(){
      listNurseRecordsVo(this.conditionRecord).then(res=>{
        this.nurseRecordList = res.data.records;
        this.pageRecord.total = res.data.total; //总记录数
        this.pageRecord.pageSize = res.data.size; //每页显示条数
        this.pageRecord.currentPag = res.data.current; //当前页码
        this.pageRecord.pagCount = res.data.pages; //总页数
        
      })
    },
    del(id) {
      this.$confirm("确定删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          removeCustomerRecord({ id: id }).then(res => {
            if (res.flag) {
              this.$message({
                type: "success",
                message: res.message
              });
              //重载表格
              this.listNurseRecordsVo();
            }
          });
        })
        .catch(() => {});
    },
  },
  mounted() {
    this.getKhxxList();
  }
};
</script> -->

<script setup>
import { ref, computed, onMounted } from 'vue';
import { selectCustomer as getKhxxList } from "../../api/customerApi.js";
import { listNurseRecordsVo, removeCustomerRecord } from "../../api/nurseRecordApi.js";
import { getSessionStorage } from "../../utils/common.js";
import { ElMessage, ElMessageBox } from 'element-plus'

// 定义响应式数据
const page = ref({
  total: 0,
  pageSize: 10,
  currentPage: 1,
  pagCount: 0
});

const pageRecord = ref({
  total: 0,
  pageSize: 10,
  currentPage: 1,
  pagCount: 0
});

const nurseRecordList = ref([]);
const khxxList = ref([]);

// 客户查询条件
const condition = ref({
  customerName: "",
  currentPage: 1, // 使用后端字段名 currentPage
  pageSize: 10     // 使用后端字段名 pageSize
});

// 护理记录查询条件
const conditionRecord = ref({
  customerId: 0, // 使用后端字段名 customerId
  currentPage: 1, // 使用后端字段名 currentPage
  pageSize: 10    // 使用后端字段名 pageSize
});

// 计算属性 - 客户分页序号 (修复NaN问题)
const indexMethod = computed(() => {
  const current = page.value.currentPage || 1;
  const size = page.value.pageSize || 6;
  return (current - 1) * size + 1;
});

// 计算属性 - 护理记录分页序号 (修复NaN问题)
const indexMethodRecord = computed(() => {
  const current = pageRecord.value.currentPage || 1;
  const size = pageRecord.value.pageSize || 6;
  return (current - 1) * size + 1;
});

// 点击查询
const query = () => {
  nurseRecordList.value = [];
  conditionRecord.value.customerId = 0;

  // 重置到第一页
  condition.value.currentPage = 1;
  fetchCustomerData();
};

// 获取客户数据
const fetchCustomerData = () => {
  // 确保参数名匹配后端
  const params = {
    customerName: condition.value.customerName,
    currentPage: condition.value.currentPage,
    pageSize: condition.value.pageSize
  };
  
  getKhxxList(params).then(res => {
    console.log("客户数据响应:", res);
    khxxList.value = res.data.records;
    page.value.total = res.data.total;
    page.value.pageSize = res.data.size;
    page.value.currentPage = res.data.current;
    page.value.pagCount = res.data.pages;
  });
};

// 获取护理记录
const fetchNurseRecords = () => {
  // 确保参数名匹配后端
  const params = {
    customerId: conditionRecord.value.customerId,
    currentPage: conditionRecord.value.currentPage,
    pageSize: conditionRecord.value.pageSize
  };
  
  listNurseRecordsVo(params).then(res => {
    console.log("护理记录响应:", res);
    nurseRecordList.value = res.data.records;
    pageRecord.value.total = res.data.total;
    pageRecord.value.pageSize = res.data.size;
    pageRecord.value.currentPage = res.data.current;
    pageRecord.value.pagCount = res.data.pages;
  });
};

// 选中页码-客户
const handleCurrentChange = (curPage) => {
  condition.value.currentPage = curPage;
  fetchCustomerData();
  
  // 清空护理记录
  nurseRecordList.value = [];
  conditionRecord.value.customerId = 0;
};

// 选中某客户行
const handleChangeCustomer = (row) => {
  if (row) {
    conditionRecord.value.customerId = row.id;
    conditionRecord.value.currentPage = 1; // 重置到第一页
    fetchNurseRecords();
  }
};

// 选中页码-护理记录
const handleRecordChange = (curPage) => {
  conditionRecord.value.currentPage = curPage;
  fetchNurseRecords();
};

// 删除护理记录
// const del = (id) => {
//   if (!confirm("确定删除?")) return;
  
//   // 确保参数名匹配后端
//   removeCustomerRecord({ id }).then(res => {
//     if (res.flag) {
//       alert(res.message);
//       fetchNurseRecords(); // 刷新护理记录
//     }
//   });
// };
const del = (id) => {
  ElMessageBox.confirm("确定删除?", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning"
  })
    .then(() => {
      return removeCustomerRecord(id) // 返回Promise以便链式调用
    })
    .then(res => {
      if (res.flag) {
        ElMessage.success(res.message)
        // 从本地列表中移除该项
        nurseRecordList.value = nurseRecordList.value.filter(item => item.id !== id)
        // 更新总数
        pageRecord.value.total--
      } else {
        ElMessage.error(res.message || "删除失败")
      }
    })
    .catch(error => {
      if (error !== 'cancel') {
        ElMessage.error("删除操作失败: " + (error.response?.data?.message || error.message))
        console.error("删除错误详情:", error)
      }
    })
}

// 生命周期钩子
onMounted(() => {
  fetchCustomerData();
});
</script>

<style scoped>
.table-main {
  height: 500px;
  border: 1px solid #d4c5c5;
}
.table-main .table-main-header {
  height: 50px;
  background-color: #3ca2e0;
  color: #fff;
  font-size: 20px;
  font-family: "Microsoft YaHei";
  line-height: 50px;
}
</style>