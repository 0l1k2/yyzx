<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <div>
          <el-row :gutter="30">
            <el-col :span="7">
              <el-input
                placeholder="护理项目名称"
                v-model="queryParams.itemName"
                @clear="query"
                clearable
                size="large"
              >
                <template #append>
                  <el-button type="info" @click="query" style="color:black">查询</el-button>
                </template>
              </el-input>
            </el-col>
            <el-col :span="7" style="margin-top:5px">
              <el-button type="primary" color="#1d83ec" @click="this.$router.go(-1)" round>返回</el-button>
            </el-col>
          </el-row>
        </div>
      </el-header>
      <el-divider style="margin:0"></el-divider>
      <el-main>
        <div class="table-main">
          <div class="table-main-header">护理项目</div>
          <!-- 表格 -->
          <el-table
            :data="nurseItemList"
            style="width: 100% ;color:black;"
            size="small"
            @row-click="changeRow"
            stripe
          >
            <el-table-column
              align="center"
              type="index"
              :index="indexMethod"
              label="序号"
              width="60"
            />
            <el-table-column align="center" prop="serialNumber" label="编号" width="180" />
            <el-table-column align="center" prop="nursingName" label="名称" width="180" />
            <el-table-column align="center" prop="servicePrice" label="价格" width="180" />
            <el-table-column align="center" prop="executionCycle" label="执行周次" width="180" />
            <el-table-column align="center" prop="executionTimes" label="执行次数" width="180" />
            <el-table-column align="center" prop="message" label="备注" width="180" />
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
        <el-divider></el-divider>
        <div class="table-main">
          <div class="table-main-header">已选护理项目</div>
          <!-- 表格 -->
          <el-table :data="changeNurseItemList" style="width: 100% ;color:black;" size="small" stripe>
            <el-table-column align="center" type="index" label="序号" width="150" />
            <el-table-column align="center" prop="serialNumber" label="编号" width="150" />
            <el-table-column align="center" prop="nursingName" label="名称" width="150" />
            <el-table-column align="center" prop="buyTime" label="服务购买日期" width="150"/>
                <el-table-column align="center" prop="nurseNumber" label="购买数量" width="150">
                  <template #default="scope">
                    <el-input v-model="scope.row.nurseNumber" placeholder="购买数量"></el-input>
                  </template>
                </el-table-column>
                <el-table-column align="center" prop="maturityTime" label="服务到期日期" width="150">
                  <template #default="scope">
                    <el-date-picker
                      style="width:130px"
                      format="YYYY/MM/DD"
                      value-format="YYYY-MM-DD"
                      v-model="scope.row.maturityTime"
                      type="date"
                      placeholder="服务到期日期"
                    ></el-date-picker>
                  </template>
                </el-table-column>

            <el-table-column align="center"  label="操作" width="150">
              <template #default="scope">
                <el-button type="danger" icon="CloseBold" size="small" @click="del(scope.$index)"></el-button>
              </template>
            </el-table-column>
          </el-table>
          
        </div>
        <div class="demo-drawer__footer" style="margin:0 auto;width:200px">
              <el-button @click="clearArr"  type="warning">清空</el-button>
              <el-button @click="isOk" type="success">保存</el-button>
            </div>
      </el-main>
    </el-container>
  </div>
</template>

<!-- <script>
import { findNurseItem } from "@/api/nurseItem.js";
import { isIncludesItemCustomer ,addItemToCustomer} from "@/api/customerNurseItem.js";
export default {
  data() {
    return {
      customerId: this.$route.query.customerId,

      //分页属性封装
      page: {
        total: 0,
        pageSize: 6,
        currentPag: 1,
        pagCount: 0
      },
      queryParams: {
        status: "1", //查询默认状态1 -启用
        itemName: "",
        pageSize: "1" //默认第一页
      },
      changeNurseItemList: [], //已选择护理项目
      nurseItemList: [] //护理项目列表
    };
  },
  mounted() {
    this.getNurseItemList();
  },
  computed: {
    indexMethod() {
      return this.page.currentPag * this.page.pageSize - this.page.pageSize + 1;
    }
  },
  methods: {
    //点击查询
    query() {
      this.queryParams.pageSize = "1"; //回到第一页
      this.getNurseItemList();
    },
    //选中页码
    handleCurrentChange(curPage) {
      this.page.currentPag = curPage;
      this.queryParams.pageSize = curPage; //参数pageSize是服务端接收页码参数名
      //重新渲染表格
      this.getNurseItemList();
    },
    //鼠标点击某行触发：添加护理项目到已选护理项目列表  api-判断用户是否已经配置了某个指定项目
    changeRow(row) {
        //api-判断用户是否已经配置了某个指定项目
        isIncludesItemCustomer({customerId:this.customerId,itemId:row.id}).then(res=>{
            if(!res.flag){
                this.$message.error("当前用户已存在该项目,可前往【服务关注】进行续费")
                return
            }
            //构建已选护理项目列表
            let obj=row;
            obj.buyTime=new Date().getFullYear()+"-"+new Date().getMonth()+"-"+new Date().getDate();
            obj.maturityTime = new Date().getFullYear()+"-"+(new Date().getMonth()+3)+"-"+new Date().getDate(); //默认三个月到期
            obj.nurseNumber=1
            //判断是否已经添加过
            if(JSON.stringify(this.changeNurseItemList).includes(JSON.stringify(obj))===true){
                this.$message.warning("所选项目已存在列表中")
                return
            }
            this.changeNurseItemList.push(obj)    
         })
        
    },
    //移除已选护理项目列表项
    del(index){
        this.changeNurseItemList.splice(index,1)
    },
    //清空已选护理项目列表项
    clearArr(){
        this.changeNurseItemList=[]
    },
    //api-查询护理项目(分页)
    getNurseItemList() {
      findNurseItem(this.queryParams).then(res => {
        this.nurseItemList = res.data.records;
        this.page.total = res.data.total; //总记录数
        this.page.pageSize = res.data.size; //每页显示条数
        this.page.currentPag = res.data.current; //当前页码
        this.page.pagCount = res.data.pages; //总页数
      });
    },
    //api-点击确认 批量生成客户护理项目信息
    isOk() {
      let customerItemList=this.changeNurseItemList
      let paramsArr=[];//提交的参数
      if(customerItemList.length==0){
          this.$message.error("请选择需要【购买的】护理项目")
          return
      }
      //构建提交的参数列表
      for(let i=0;i<customerItemList.length;i++){
        let obj={} 
        obj.itemId=customerItemList[i].id
        obj.buyTime=customerItemList[i].buyTime
        obj.custormerId=this.customerId
        obj.nurseNumber=customerItemList[i].nurseNumber
        obj.isDeleted=0
        obj.maturityTime=customerItemList[i].maturityTime
        paramsArr.push(obj)
      }
      this.$confirm("确定添护理项目数据无误?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "error",
        icon:'WarningFilled'
      })
        .then(() => {
          addItemToCustomer(paramsArr).then(res=>{
              if (res.flag) {
                this.$message.success(res.message);
                //路由会上一级
                this.$router.go(-1)
            } else {
                this.$message.error(res.message);
            }
          })
         
        })
        .catch(() => {});
      // console.log(paramsArr);

    },
  }
};
</script> -->

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { findNurseItem } from "../../api/nurseItemApi.js";
import { isIncludeItemCustormer, addItemToCustomer } from "../../api/customerNurseItemApi.js";

const route = useRoute();
const router = useRouter();

// 客户ID处理
const customerId = ref(route.query.customerId || '');
const queryParams = ref({
  status: "1",
  itemName: "",
  currentPage: 1,
  pageSize: 6
});
const page = ref({
  total: 0,
  pageSize: 6,
  currentPag: 1,
  pagCount: 0
});
const nurseItemList = ref([]);
const changeNurseItemList = ref([]);

// 已选项目分页配置
const selectedPage = ref({
  currentPage: 1,
  pageSize: 10,  // 每页默认条数
  total: 0
});

// 分页后的数据（计算属性）
const paginatedChangeList = computed(() => {
  const start = (selectedPage.value.currentPage - 1) * selectedPage.value.pageSize;
  const end = start + selectedPage.value.pageSize;
  return changeNurseItemList.value.slice(start, end);
});

// 监听已选列表变化，更新分页总数
watch(changeNurseItemList, (newList) => {
  selectedPage.value.total = newList.length;
});

// 分页切换事件
const handleSelectedPageChange = (curPage) => {
  selectedPage.value.currentPage = curPage;
};

// 日期格式化
const formatDate = (date) => {
  const d = new Date(date);
  return [
    d.getFullYear(),
    String(d.getMonth() + 1).padStart(2, '0'),
    String(d.getDate()).padStart(2, '0')
  ].join('-');
};

// 表格序号计算
const indexMethod = computed(() => (index) => 
  (queryParams.value.currentPage - 1) * queryParams.value.pageSize + index + 1
);

// 获取护理项目列表
const getNurseItemList = () => {
  if (!customerId.value) {
    ElMessage.error("客户ID缺失，无法加载数据");
    return;
  }
  const safeParams = {
    ...queryParams.value,
    currentPage: Number(queryParams.value.currentPage) || 1,
    pageSize: Number(queryParams.value.pageSize) || 6
  };
  findNurseItem(safeParams)
    .then(res => {
      nurseItemList.value = res.data?.records || [];
      page.value.total = res.data?.total || 0;
      page.value.pageSize = res.data?.size || 6;
      page.value.currentPag = res.data?.current || 1;
      page.value.pagCount = res.data?.pages || 0;
    })
    .catch(err => {
      console.error("加载护理项目失败:", err);
      ElMessage.error("系统异常，请稍后重试");
    });
};

// 查询按钮
const query = () => {
  queryParams.value.currentPage = 1;
  getNurseItemList();
};

// 分页切换（主列表）
const handleCurrentChange = (curPage) => {
  queryParams.value.currentPage = curPage;
  getNurseItemList();
};

// 选择护理项目（修正itemId为数字ID）
const changeRow = (row) => {
  if (!customerId.value) {
    ElMessage.error("客户信息未就绪，请稍后再试");
    return;
  }
  const itemId = row.id; // 使用后端数字ID
  if (!itemId) {
    ElMessage.error("项目ID缺失，无法添加");
    return;
  }
  isIncludeItemCustormer({  
    customerId: customerId.value,  
    itemId: itemId  
  })  
  .then(res => {  
    if (!res.flag) {  
      ElMessage.error(res.message || "该项目已存在");  
      return;  
    }  
    const now = new Date();  
    const maturityDate = new Date(now);  
    maturityDate.setMonth(now.getMonth() + 3);  
    const newItem = {  
      ...row,  
      buyTime: formatDate(now),  
      maturityTime: formatDate(maturityDate),  
      nurseNumber: 1  
    };  
    const exists = changeNurseItemList.value.some(  
      item => item.id === newItem.id  
    );  
    if (exists) {  
      ElMessage.warning("该项目已在已选列表中");  
      return;  
    }  
    changeNurseItemList.value.push(newItem);  
  })  
  .catch(err => {  
    console.error("添加项目失败:", err);  
    ElMessage.error("操作异常，请稍后重试");  
  });  
};

// 删除已选项目
const del = (index) => {
  changeNurseItemList.value.splice(index, 1);
};

// 清空已选项目
const clearArr = () => {
  changeNurseItemList.value = [];
};

// 保存提交（确保itemId为数字ID）
const isOk = () => {
  if (!customerId.value) return ElMessage.error("客户ID缺失");
  if (changeNurseItemList.value.length === 0) return ElMessage.error("请选择护理项目");
  const invalidItems = changeNurseItemList.value.filter(item => {
    return !item.id || !item.nurseNumber || !item.maturityTime;
  });
  if (invalidItems.length > 0) {
    return ElMessage.error("请完善所有项目的必填信息");
  }
  const paramsArr = changeNurseItemList.value.map(item => ({
    itemId: item.id,        // 传递数字ID
    customerId: customerId.value,
    nurseNumber: item.nurseNumber,
    buyTime: item.buyTime,
    maturityTime: item.maturityTime,
    isDeleted: 0,
    levelId: 0
  }));
  ElMessageBox.confirm(
    "确认提交所选护理项目？",
    "确认提示",
    { type: "warning" }
  )
  .then(() => {
    addItemToCustomer(paramsArr)
      .then(res => {
        if (res.flag) {
          ElMessage.success(res.message);
          router.go(-1);
        } else {
          ElMessage.error(res.message);
        }
      })
      .catch(err => {
        console.error("提交失败:", err);
        ElMessage.error("系统异常，请稍后重试");
      });
  })
  .catch(() => {});
};

// 页面初始化
onMounted(() => {
  const cid = route.query.customerId;  
  if (!cid || cid === 'undefined') {  
    ElMessage.error("客户ID缺失，即将返回上一页");  
    setTimeout(() => router.go(-1), 1500);  
    return;  
  }  
  customerId.value = Number(cid);  
  if (isNaN(customerId.value)) {  
    ElMessage.error("客户ID格式错误");  
    setTimeout(() => router.go(-1), 1500);  
    return;  
  }  
  getNurseItemList();  
});
</script>


<!-- <script setup>
import { ref, onMounted, computed, watchEffect } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { findNurseItem } from "../../api/nurseItemApi.js";
import { isIncludeItemCustormer, addItemToCustomer } from "../../api/customerNurseItemApi.js";

const route = useRoute();
const router = useRouter();

// 客户ID（使用明确的初始化状态）
const customerId = ref(null); // 初始化为null，明确未就绪状态

// 页面加载状态（新增）
const pageLoading = ref(true);
const isCustomerIdValid = ref(false); 

// 分页参数
const queryParams = ref({
  status: "1",
  itemName: "",
  currentPage: 1,
  pageSize: 6
});

const page = ref({
  total: 0,
  pageSize: 6,
  currentPag: 1,
  pagCount: 0
});

// 护理项目列表
const nurseItemList = ref([]);
// 已选护理项目
const changeNurseItemList = ref([]);

// 日期格式化
const formatDate = (date) => {
  const d = new Date(date);
  return [
    d.getFullYear(),
    String(d.getMonth() + 1).padStart(2, '0'),
    String(d.getDate()).padStart(2, '0')
  ].join('-');
};

// 表格序号计算
const indexMethod = computed(() => (index) => 
  (queryParams.value.currentPage - 1) * queryParams.value.pageSize + index + 1
);

// 获取护理项目列表（添加客户ID校验）
const getNurseItemList = () => {
  if (!customerId.value) {
    console.error("客户ID未初始化，阻止数据请求");
    return;
  }
  
  const safeParams = {
    ...queryParams.value,
    currentPage: Number(queryParams.value.currentPage) || 1,
    pageSize: Number(queryParams.value.pageSize) || 6
  };
  
  findNurseItem(safeParams)
    .then(res => {
      nurseItemList.value = res.data?.records || [];
      page.value.total = res.data?.total || 0;
      page.value.pageSize = res.data?.size || 6;
      page.value.currentPag = res.data?.current || 1;
      page.value.pagCount = res.data?.pages || 0;
    })
    .catch(err => {
      console.error("加载护理项目失败:", err);
      ElMessage.error("系统异常，请稍后重试");
    })
    .finally(() => {
      pageLoading.value = false;
    });
};

// 查询按钮
const query = () => {
  queryParams.value.currentPage = 1;
  getNurseItemList();
};

// 分页切换
const handleCurrentChange = (curPage) => {
  queryParams.value.currentPage = curPage;
  getNurseItemList();
};

// 选择护理项目（添加严格的客户ID校验）
const changeRow = (row) => {
  if (!customerId.value) {
    ElMessage.error("客户信息未就绪，请稍后再试");
    return;
  }
  
  // 改为传递项目ID（数字类型，后端期望的Integer）
  const itemId = row.id;  // 关键修改：用row.id（数据库ID，数字）而非row.serialNumber（编号，字符串）
  
  if (!itemId) {
    ElMessage.error("项目ID缺失，无法添加");
    return;
  }
  
  isIncludeItemCustormer({  
    customerId: customerId.value,  
    itemId: itemId  
  })  
  .then(res => {  
    if (!res.flag) {  
      ElMessage.error(res.message || "该项目已存在");  
      return;  
    }  

    const now = new Date();  
    const maturityDate = new Date(now);  
    maturityDate.setMonth(now.getMonth() + 3);  

    const newItem = {  
      ...row,  
      buyTime: formatDate(now),  
      maturityTime: formatDate(maturityDate),  
      nurseNumber: 1  
    };  

    const exists = changeNurseItemList.value.some(  
      item => item.id === newItem.id  // 同时修改匹配条件为id
    );  
    if (exists) {  
      ElMessage.warning("该项目已在已选列表中");  
      return;  
    }  

    changeNurseItemList.value.push(newItem);  
  })  
  .catch(err => {  
    console.error("添加项目失败:", err);  
    ElMessage.error("操作异常，请稍后重试");  
  });  
};

// 删除已选项目
const del = (index) => {
  changeNurseItemList.value.splice(index, 1);
};

// 清空已选项目
const clearArr = () => {
  changeNurseItemList.value = [];
};

// 保存提交（添加客户ID校验）
// const isOk = () => {
//   if (!customerId.value) {
//     return ElMessage.error("客户信息未就绪，无法提交");
//   }
  
//   if (changeNurseItemList.value.length === 0) {
//     return ElMessage.error("请选择护理项目");
//   }
  
//   const invalidItems = changeNurseItemList.value.filter(item => {
//     return !item.serialNumber || 
//            !item.nurseNumber || 
//            !item.maturityTime;
//   });
  
//   if (invalidItems.length > 0) {
//     return ElMessage.error("请完善所有项目的必填信息");
//   }
  
//   const params = changeNurseItemList.value.map(item => ({
//     itemId: item.serialNumber,
//     customerId: customerId.value,
//     nurseNumber: item.nurseNumber,
//     buyTime: item.buyTime,
//     maturityTime: item.maturityTime,
//     isDeleted: 0,
//     levelId: 0
//   }));
  
//   ElMessageBox.confirm(
//     "确认提交所选护理项目？",
//     "确认提示",
//     { type: "warning" }
//   )
//   .then(() => {
//     addItemToCustomer(params)
//       .then(res => {
//         if (res.flag) {
//           ElMessage.success(res.message);
//           router.go(-1);
//         } else {
//           ElMessage.error(res.message);
//         }
//       })
//       .catch(err => {
//         console.error("提交失败:", err);
//         ElMessage.error("系统异常，请稍后重试");
//       });
//   })
//   .catch(() => {});
// };
const isOk = () => {
  if (!customerId.value) return ElMessage.error("客户ID缺失");
  if (changeNurseItemList.value.length === 0) return ElMessage.error("请选择护理项目");
  
  // 验证必填字段
  const invalidItems = changeNurseItemList.value.filter(item => {
    return !item.id || // 校验项目ID（数字）存在
           !item.nurseNumber || 
           !item.maturityTime;
  });
  
  if (invalidItems.length > 0) {
    return ElMessage.error("请完善所有项目的必填信息");
  }
  
  // 构建提交参数：itemId改用item.id（数字类型，匹配后端Integer）
  const params = changeNurseItemList.value.map(item => ({
    itemId: item.id, // 关键修改：从item.serialNumber改为item.id
    customerId: customerId.value,
    nurseNumber: item.nurseNumber,
    buyTime: item.buyTime,
    maturityTime: item.maturityTime,
    isDeleted: 0,
    levelId: 0
  }));
  
  ElMessageBox.confirm(
    "确认提交所选护理项目？",
    "确认提示",
    { type: "warning" }
  )
  .then(() => {
    addItemToCustomer(params)
      .then(res => {
        if (res.flag) {
          ElMessage.success(res.message);
          router.go(-1);
        } else {
          ElMessage.error(res.message);
        }
      })
      .catch(err => {
        console.error("提交失败:", err);
        ElMessage.error("系统异常，请稍后重试");
      });
  })
  .catch(() => {});
};

// 页面初始化（使用更严格的路由参数处理）
onMounted(() => {

  // 临时硬编码客户ID用于测试
  customerId.value = 123; // 使用一个有效的客户ID

  // 获取路由参数
  const cid = route.query.customerId;
  
  // 严格校验参数有效性
  if (
    typeof cid === 'undefined' ||
    cid === null ||
    cid === 'undefined' ||
    cid === '' ||
    cid.trim() === ''
  ) {
    console.error("路由中未包含有效客户ID:", cid);
    ElMessage.error("客户ID缺失，即将返回上一页");
    setTimeout(() => router.go(-1), 1500);
    return;
  }
  
  // 尝试转换为数字（如果后端需要数字类型）
  const parsedCid = parseInt(cid, 10);
  
  // 校验转换结果
  if (isNaN(parsedCid) || parsedCid <= 0) {
    console.error("无效的客户ID格式:", cid);
    ElMessage.error("客户ID格式错误，必须为正整数");
    setTimeout(() => router.go(-1), 1500);
    return;
  }
  
  // 赋值并加载数据
  isCustomerIdValid.value = true;
  customerId.value = parsedCid;
  getNurseItemList();
});

// 新增：监听customerId变化，确保状态一致性
watchEffect(() => {
  console.log("客户ID状态:", customerId.value);
});
</script> -->

<style scoped>
.table-main {
  height: 300px;
  border: 1px solid #d4c5c5;
  width: 100%;
}
.table-main .table-main-header {
  height: 30px;
  background-color: #1d83ec;
  color: #fff;
  font-size: 18px;
  font-family: "Microsoft YaHei";
  line-height: 30px;
}
</style>