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
              <el-button type="primary" @click="this.$router.go(-1)" round>返回</el-button>
            </el-col>
          </el-row>
        </div>
      </el-header>
      <el-divider style="margin:0"></el-divider>
      <el-main>
        <el-row :gutter="20">
          <el-col :span="11">
            <div class="table-main">
              <div class="table-main-header">护理项目</div>
            
              <el-table
                :data="nurseItemList"
                highlight-current-row
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
                <el-table-column align="center" prop="serialNumber" label="编号" width="80" />
                <el-table-column align="center" prop="nursingName" label="名称" width="100" />
                <el-table-column align="center" prop="servicePrice" label="价格" width="80" />
                <el-table-column align="center" prop="executionCycle" label="执行周次" width="80" />
                <el-table-column align="center" prop="executionTimes" label="执行次数" width="80" />
              </el-table>
             
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
          <!-- <el-col :span="13">
            <div class="table-main">
              <div class="table-main-header">护理项目({{levelName}})</div>
              
              <el-table
                :data="isInLevelItemList"
                style="width: 100% ;color:black;"
                size="small"
                stripe
              >
                <el-table-column align="center" type="index" label="序号" width="60" />
                <el-table-column align="center" prop="serialNumber" label="编号" width="80" />
                <el-table-column align="center" prop="nursingName" label="名称" width="100" />
                <el-table-column align="center" prop="servicePrice" label="价格" width="80" />
                <el-table-column align="center" prop="executionCycle" label="执行周次" width="80" />
                <el-table-column align="center" prop="executionTimes" label="执行次数" width="80" />
                <el-table-column align="center" fixed="right" label="操作" width="100">
                  <template #default="scope">
                    <el-button
                      type="danger"
                      icon="CloseBold"
                      size="small"
                      @click="del(scope.row.id)"
                    ></el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-col> -->
           <el-col :span="13">
      <div class="table-main">
        <div class="table-main-header">护理项目({{levelName}})</div>
        <el-table
          :data="isInLevelItemList"
          style="width: 100% ;color:black;"
          size="small"
          stripe
        >
          <el-table-column align="center" type="index" :index="indexMethod" label="序号" width="60" />
          <el-table-column align="center" prop="serialNumber" label="编号" width="80" />
          <el-table-column align="center" prop="nursingName" label="名称" width="100" />
          <el-table-column align="center" prop="servicePrice" label="价格" width="80" />
          <el-table-column align="center" prop="executionCycle" label="执行周次" width="80" />
          <el-table-column align="center" prop="executionTimes" label="执行次数" width="80" />
          <el-table-column align="center" fixed="right" label="操作" width="100">
            <template #default="scope">
              <el-button
                type="danger"
                icon="CloseBold"
                size="small"
                @click="del(scope.row.id)"
              ></el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<!-- <script setup>
import { ref, computed, onMounted } from 'vue';
import { selectCustomer as getKhxxList } from "../../api/customerApi.js";
import { listNurseRecordsVo, removeCustomerRecord } from "../../api/nurseRecordApi.js";
import { getSessionStorage } from "../../utils/common.js";

// 定义响应式数据
const page = ref({
  total: 0,
  pageSize: 6,
  currentPage: 1,
  pagCount: 0
});

const pageRecord = ref({
  total: 0,
  pageSize: 6,
  currentPage: 1,
  pagCount: 0
});

const nurseRecordList = ref([]);
const khxxList = ref([]);

// 客户查询条件
const condition = ref({
  customerName: "",
  currentPage: 1, // 使用后端字段名 currentPage
  pageSize: 6     // 使用后端字段名 pageSize
});

// 护理记录查询条件
const conditionRecord = ref({
  customerId: 0, // 使用后端字段名 customerId
  currentPage: 1, // 使用后端字段名 currentPage
  pageSize: 6    // 使用后端字段名 pageSize
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
const del = (id) => {
  if (!confirm("确定删除?")) return;
  
  // 确保参数名匹配后端
  removeCustomerRecord({ id }).then(res => {
    if (res.flag) {
      alert(res.message);
      fetchNurseRecords(); // 刷新护理记录
    }
  });
};

// 生命周期钩子
onMounted(() => {
  fetchCustomerData();
});
</script> -->
<!-- <script setup>
import { ref, watch, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { findNurseItem } from "../../api/nurseItemApi.js";
import { addItemToLevel, listNurseItemByLevel, removeNurseLevel as removeNurseLevelItem } from "../../api/nurseLevelApi.js";
import { ElMessage } from 'element-plus';

// 路由实例
const router = useRouter();
const route = useRoute();

// 响应式数据（保持原有字段定义）
const levelId = ref(route.query.levelId || '');
const levelName = ref(route.query.levelName || '');
const page = ref({
  total: 0,
  pageSize: 6,  // 每页条数（与后端统一）
  currentPag: 1, // 保持原有字段名，对应后端currentPage
  pagCount: 0
});
const queryParams = ref({
  status: "1",
  itemName: "",
  pageSize: page.value.pageSize, // 同步分页参数
  currentPage: page.value.currentPag // 同步分页参数
});
const isInLevelItemList = ref([]);
const nurseItemList = ref([]);

// 监听路由变化（级联更新数据）
watch(() => route.query, (newQuery) => {
  levelId.value = newQuery.levelId || '';
  levelName.value = newQuery.levelName || '';
  if (levelId.value) {
    getNurseItemList();
    listNurseItemByLevel();
  }
}, { deep: true });

// 表格序号计算（保持原有逻辑）
const indexMethod = (index) => {
  return page.value.currentPag * page.value.pageSize - page.value.pageSize + index + 1;
};

// 查询方法（重置分页）
const query = () => {
  queryParams.value.currentPage = 1; // 重置到第一页
  getNurseItemList();
};

// 分页切换（同步参数）
const handleCurrentChange = (curPage) => {
  page.value.currentPag = curPage; // 更新当前页
  queryParams.value.currentPage = curPage; // 同步查询参数
  getNurseItemList();
};


// 添加项目后刷新右侧列表（保持原有逻辑）
const changeRow = (row) => {
  addItemToLevel({ levelId: levelId.value, itemId: row.id })
    .then(res => {
      if (res.flag) {
        listNurseItemByLevel(); // 刷新右侧表格
        ElMessage.success(res.message);
      }
    });
};

// 获取护理项目列表（带分页）
const getNurseItemList = () => {
  findNurseItem(queryParams.value)
    .then(res => {
      // 同步后端返回的分页数据
      nurseItemList.value = res.data.records || [];
      page.value.total = res.data.total || 0;
      page.value.pageSize = res.data.size || 6;
      page.value.currentPag = res.data.current || 1;
      page.value.pagCount = res.data.pages || 0;
      // 确保查询参数与后端一致（处理后端可能的参数覆盖）
      queryParams.value.pageSize = page.value.pageSize;
      queryParams.value.currentPage = page.value.currentPag;
    })
    .catch(error => {
      console.error('获取护理项目列表失败', error);
      ElMessage.error('获取数据失败，请稍后再试');
    });
};


// 删除操作
const del = (id) => {
  const confirm = window.confirm("此操作删除记录, 是否继续?");
  if (confirm) {
    removeNurseLevelItem({ levelId: levelId.value, itemId: id })
      .then(res => {
        if (res.flag) {
          ElMessage.success(res.message);
          listNurseItemByLevel(); // 刷新已选列表
        } else {
          ElMessage.error(res.message);
        }
      })
      .catch(error => {
        console.error('删除级别内护理项目失败', error);
        ElMessage.error('删除失败，请稍后再试');
      });
  }
};

// 返回上一页
const goBack = () => {
  router.go(-1);
};

// 初始化加载
onMounted(() => {
  if (levelId.value) {
    getNurseItemList();
    listNurseItemByLevel();
  }
});
</script> -->

<script setup>
import { ref, watch, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { findNurseItem } from "../../api/nurseItemApi.js";
import { addItemToLevel, listNurseItemByLevel, removeNurseLevel as removeNurseLevelItem } from "../../api/nurseLevelApi.js";
import { ElMessage, ElMessageBox } from 'element-plus';

const router = useRouter();
const route = useRoute();

// 确保 levelId 初始化为数字类型
const levelId = ref(route.query.levelId ? parseInt(route.query.levelId) : null);
const levelName = ref(route.query.levelName || '');
const page = ref({
  total: 0,
  pageSize: 6,
  currentPag: 1,
  pagCount: 0
});
const queryParams = ref({
  status: "1",
  itemName: "",
  pageSize: page.value.pageSize,
  currentPage: page.value.currentPag
});
const isInLevelItemList = ref([]);
const nurseItemList = ref([]);

// 获取护理项目列表
const getNurseItemList = () => {
  findNurseItem(queryParams.value)
    .then(res => {
      nurseItemList.value = res.data.records || [];
      page.value.total = res.data.total || 0;
      page.value.pageSize = res.data.size || 6;
      page.value.currentPag = res.data.current || 1;
      page.value.pagCount = res.data.pages || 0;
      
      queryParams.value.pageSize = page.value.pageSize;
      queryParams.value.currentPage = page.value.currentPag;
    })
    .catch(error => {
      console.error('获取护理项目失败:', error);
      ElMessage.error('获取数据失败');
    });
};

// 加载护理级别项目 - 确保传递数字类型的levelId
const loadLevelItems = () => {
  if (!levelId.value || isNaN(levelId.value)) {
    console.error('无效的levelId:', levelId.value);
    return;
  }
  
  // 确保传递数字类型的levelId
  listNurseItemByLevel(parseInt(levelId.value))
    .then(res => {
      isInLevelItemList.value = res.data || [];
    })
    .catch(error => {
      console.error('加载护理级别项目失败:', error);
      ElMessage.error('加载数据失败');
    });
};

// 监听路由变化 - 确保正确处理levelId
watch(() => route.query, (newQuery) => {
  const newLevelId = newQuery.levelId ? parseInt(newQuery.levelId) : null;
  
  if (isNaN(newLevelId)) {
    ElMessage.error('无效的级别ID');
    router.go(-1);
    return;
  }
  
  levelId.value = newLevelId;
  levelName.value = newQuery.levelName || '';
  
  if (levelId.value) {
    getNurseItemList();
    loadLevelItems();
  }
}, { immediate: true, deep: true });

// 表格序号计算
const indexMethod = (index) => {
  return (page.value.currentPag - 1) * page.value.pageSize + index + 1;
};

// 查询方法
const query = () => {
  queryParams.value.currentPage = 1;
  getNurseItemList();
};

// 分页切换
const handleCurrentChange = (curPage) => {
  page.value.currentPag = curPage;
  queryParams.value.currentPage = curPage;
  getNurseItemList();
};

// 添加项目 - 确保传递数字类型的levelId
const changeRow = (row) => {
  if (!levelId.value || isNaN(levelId.value)) {
    ElMessage.error('缺少级别ID或级别ID无效');
    return;
  }
  
  addItemToLevel({ 
    levelId: parseInt(levelId.value), 
    itemId: row.id 
  })
    .then(res => {
      if (res.flag) {
        loadLevelItems();
        ElMessage.success(res.message);
      } else {
        ElMessage.error(res.message);
      }
    })
    .catch(error => {
      console.error('添加失败:', error);
      ElMessage.error('操作失败');
    });
};

// 删除操作 - 确保传递数字类型的Id
// 删除操作 (AddItemToLevel.vue)
const del = (itemId) => {
  // 1. 参数验证
  if (!levelId.value || isNaN(levelId.value)) {
    ElMessage.error('无效的护理级别ID');
    return;
  }

  const numericItemId = Number(itemId);
  if (isNaN(numericItemId)) {
    ElMessage.error('无效的项目ID');
    return;
  }

  // 2. 用户确认
  ElMessageBox.confirm('确定要从当前护理级别移除该项目吗？', '提示', {
    type: 'warning'
  })
  .then(() => {
    // 3. 调用API - 确保传递正确的参数对象
    return removeNurseLevelItem({
      levelId: parseInt(levelId.value),
      itemId: numericItemId
    });
  })
  .then((res) => {
    // 4. 处理响应
    if (res?.flag) {
      ElMessage.success(res.message || '移除成功');
      return loadLevelItems(); // 刷新列表
    } else {
      ElMessage.error(res?.message || '移除失败');
    }
  })
  .catch((error) => {
    // 用户取消操作不报错
    if (error !== 'cancel') {
      console.error('移除操作失败:', error);
      ElMessage.error('移除操作失败');
    }
  });
};

// 返回上一页
const goBack = () => {
  router.go(-1);
};

// 初始化加载
onMounted(() => {
  if (levelId.value && !isNaN(levelId.value)) {
    getNurseItemList();
    loadLevelItems();
  }
});
</script>

<style scoped>
/* 样式保持不变 */
</style>

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