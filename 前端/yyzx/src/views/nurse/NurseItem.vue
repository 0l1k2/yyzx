<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <div>
          <el-row :gutter="30">
            <el-col :span="7">
              <el-input
                placeholder="名称"
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
            <el-col :span="17">
              <el-button type="primary" @click="addItem" color="#337ab7" style="margin-top:4px">
                <el-icon>
                  <Plus />
                </el-icon>
                <span>添加</span>
              </el-button>
            </el-col>
          </el-row>
        </div>
      </el-header>
      <el-divider style="margin:0"></el-divider>
      <el-main>
        <div>
          <el-row class="mb-4">
            <el-button
              :class="[{activeBtn: btnFlag}]"
              style="border: 1px white solid;margin-left: 0px"
              @click="enable"
            >启用</el-button>
            <el-button
              :class="[{activeBtn: !btnFlag}]"
              style="border: 1px white solid;margin-left: 0px"
              @click="disable"
            >停用</el-button>
          </el-row>
        </div>
        <div>
          <!-- 表格 -->
          <el-table :data="nurseItemList" style="width: 100% ;color:black;" stripe>
            <el-table-column
              align="center"
              type="index"
              :index="indexMethod"
              label="序号"
              width="60"
            />
            <el-table-column align="center" prop="serialNumber" label="编号" width="120" />
            <el-table-column align="center" prop="nursingName" label="名称" width="120" />
            <el-table-column align="center" prop="servicePrice" label="价格" width="120" />
            <el-table-column align="center" prop="executionCycle" label="执行周次" width="120" />
            <el-table-column align="center" prop="executionTimes" label="执行次数" width="120" />
            <el-table-column align="center" prop="message" label="描述" width="120" />
            <el-table-column align="center" prop="status" label="状态" width="120">
              <template #default="scope">{{ scope.row.status==1?'启用':'停用'}}</template>
            </el-table-column>
            <el-table-column align="center" fixed="right" label="操作" width="220">
              <template #default="scope">
                <el-button type="primary" icon="Edit" link @click="edit(scope.row)">修改</el-button>
                <el-button type="danger" icon="Delete" link @click="del(scope.row.id)">删除</el-button>
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
          <el-pagination
            :page-size="page.pageSize"
            background
            :current-page="page.currentPage"
            layout=" prev, pager, next"
            :total="page.total"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-main>
    </el-container>
    <!-- 修改/添加对话框 -->
    <el-dialog
      v-model="dialog.dialogVisible"
      :title="dialog.tops"
      width="40%"
      align-center
      draggable
      :before-close="handleClose"
    >
      <el-divider border-style="double" style="margin:0;" />
      <el-form
        label-position="right"
        label-width="auto"
        style="max-width:380px;margin:20px auto"
        class="demo-form-inline"
        ref="itemForm"
        :model="dialog.item"
        :rules="rules"
      >
        <el-form-item label="编号：" prop="serialNumber">
          <el-input v-model="dialog.item.serialNumber" />
        </el-form-item>
        <el-form-item label="名称：" prop="nursingName">
          <el-input v-model="dialog.item.nursingName" />
        </el-form-item>
        <el-form-item label="价格：" prop="servicePrice">
          <el-input v-model="dialog.item.servicePrice" />
        </el-form-item>

        <el-form-item label="执行周期：" prop="executionCycle">
          <el-select v-model="dialog.item.executionCycle" style="width:320px" placeholder="执行周期：">
            <el-option label="每天" value="每天" />
            <el-option label="每周" value="每周" />
            <el-option label="每月" value="每月" />
          </el-select>
        </el-form-item>

        <el-form-item label="执行次数：" prop="executionTimes">
          <el-input v-model="dialog.item.executionTimes" />
        </el-form-item>

        <el-form-item label="描述： " prop="message">
          <el-input v-model="dialog.item.message" />
        </el-form-item>

        <el-form-item label="状态：" prop="status">
          <el-select v-model="dialog.item.status" style="width:320px" placeholder="状态">
            <el-option
              v-for="item in dialog.statusArr"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <el-divider border-style="double" style="margin:0;" />
      <template #footer>
        <span class="dialog-footer" style="padding-top: 0px">
          <el-button type="primary" @click="save('itemForm')">保存</el-button>
          <el-button @click="cancel">取消</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<!-- <script>
import {
  addNurseItem,
  findNurseItem,
  updateNurseItem,
  delNurseItem
} from "../../api/nurseItemApi.js";
export default {
  computed: {
    indexMethod() {
      return this.page.currentPag * this.page.pageSize - this.page.pageSize + 1;
    }
  },
  data() {
    return {
      //模态框数据
      dialog: {
        dialogVisible: false, //模态框状态
        tops: "", //模态框标题,
        item: {
          id: "",
          serialNumber: "",
          nursingName: "",
          servicePrice: "",
          message: "",
          executionCycle: "",
          executionTimes: "",
          status: "" //状态 1：启用；2：停用
        },
        statusArr: [
          {
            value: 1,
            label: "启用"
          },
          {
            value: 2,
            label: "停用"
          }
        ]
      },
      //校验规则
      rules: {
        serialNumber: [
          { required: true, message: "请输入编号", trigger: "blur" }
        ],
        nursingName: [
          { required: true, message: "请输入名称", trigger: "blur" }
        ],
        servicePrice: [
          { required: true, message: "请输入价格", trigger: "blur" }
        ],
        status: [{ required: true, message: "请选择状态", trigger: "change" }]
      },
      //分页属性封装
      page: {
        total: 0,
        pageSize: 6,
        currentPag: 1,
        pagCount: 0
      },
      btnFlag: true,
      queryParams: {
        status: "1", //查询默认状态1 -启用
        itemName: "",
        pageSize: "1" //默认第一页
      },
      nurseItemList: []
    };
  },
  mounted() {
    this.getNurseItemList();
  },
  methods: {
    //点击查询
    query() {
      this.queryParams.pageSize = "1"; //回到第一页
      this.getNurseItemList();
    },
    //点击启用
    enable() {
      this.btnFlag = true;
      this.queryParams.status = "1"; //1-启用
      this.queryParams.pageSize = "1"; //回到第一页
      this.getNurseItemList();
    },
    //点击停用
    disable() {
      this.btnFlag = false;
      this.queryParams.pageSize = "1"; //2-停用
      this.queryParams.status = "2"; //0-生效床位信息
      this.getNurseItemList();
    },
    //选中页码
    handleCurrentChange(curPage) {
      this.page.currentPag = curPage;
      this.queryParams.pageSize = curPage; //参数pageSize是服务端接收页码参数名
      //重新渲染表格
      this.getNurseItemList();
    },
    //点击修改
    edit(row) {
      this.dialog.tops = "修改护理项目";
      this.dialog.dialogVisible = true;
      //初始化模态框数据
      this.$nextTick(() => {
        this.dialog.item.id = row.id;
        this.dialog.item.serialNumber = row.serialNumber;
        this.dialog.item.nursingName = row.nursingName;
        this.dialog.item.servicePrice = row.servicePrice;
        this.dialog.item.message = row.message;
        this.dialog.item.executionCycle = row.executionCycle;
        this.dialog.item.executionTimes = row.executionTimes;
        this.dialog.item.status = row.status;
      });
    },
    //点击添加按钮
    addItem() {
      this.dialog.tops = "添加护理项目";
      this.dialog.dialogVisible = true;
    },
    handleClose() {
      this.dialog.dialogVisible = false;
      this.resetForm("itemForm"); //重置表单
    },
    cancel() {
      this.handleClose();
    },
    //重置表单
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    //api-保存(新增/编辑)
    save(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          //通过id判断是添加还是编辑
          if (this.dialog.item.id == null || this.dialog.item.id == "") {
            addNurseItem(this.dialog.item).then(res => {
              if (res.flag) {
                this.$message.success(res.message);
                //刷新数据表格(回到最初查询状态)
                this.queryParams.status = "1";
                this.queryParams.pageSize = "1"; //回到第一页
                this.queryParams.itemName = "";
                this.getNurseItemList();
                this.handleClose(); //关闭模态框
              } else {
                this.$message.error(res.message);
              }
            });
          } else {
            updateNurseItem(this.dialog.item).then(res => {
              if (res.flag) {
                this.$message.success(res.message);
                //刷新数据表格
                this.getNurseItemList();
                this.handleClose(); //关闭模态框
              } else {
                this.$message.error(res.message);
              }
            });
          }
        } else {
          return false;
        }
      });
    },
    //api-删除
    del(id) {
      this.$confirm("此操作删除记录, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          delNurseItem({ id: id }).then(res => {
            if (res.flag) {
              this.$message.success(res.message);
              //重载表格
              this.getNurseItemList();
            } else {
              this.$message.error(res.message);
            }
          });
        })
        .catch(() => {});
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
    }
  }
};
</script> -->
<!-- <script setup>
import { ref, nextTick, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { addNurseItem, findNurseItem, updateNurseItem, delNurseItem } from "../../api/nurseItemApi.js";

// 获取路由实例
const router = useRouter();

// ========== 1. 查询参数（严格匹配后端字段名） ==========
// 查询参数：与后端字段完全一致
// 分页状态（与后端响应严格同步）
const page = ref({
  total: 0,         // 总记录数
  currentPage: 1,   // 当前页码（双向绑定，自动更新）
  pageSize: 6,      // 每页条数（优先取后端返回值）
  pages: 0          // 总页数
});

// 查询参数（与后端接口字段严格对齐）
const queryParams = ref({
  itemName: '',
  status: 1,
  currentPage: 1,   // 与 page.currentPage 联动
  pageSize: 6       // 与 page.pageSize 联动
});

// 分页切换时，同步查询参数 + 触发请求
const handleCurrentChange = (curPage) => {
  queryParams.value.currentPage = curPage; // 同步查询参数
  getNurseItemList();                     // 重新请求数据
};

// ========== 2. 响应式数据 ==========
const nurseItemList = ref([]); // 表格数据


const dialog = ref({          // 模态框数据
  dialogVisible: false,
  tops: "添加护理项目",
  item: {
    id: "",
    serialNumber: "",
    nursingName: "",
    servicePrice: "",
    message: "",
    status: 1,
    executionCycle: "",
    executionTimes: "",
    is_deleted: 0 // 后端字段：删除标记
  },
  statusArr: [
    { value: 1, label: "启用" },
    { value: 2, label: "停用" }
  ]
});

const rules = ref({           // 表单校验规则
  serialNumber: [{ required: true, message: "请输入编号", trigger: "blur" }],
  nursingName: [{ required: true, message: "请输入名称", trigger: "blur" }],
  servicePrice: [{ required: true, message: "请输入价格", trigger: "blur" }],
  status: [{ required: true, message: "请选择状态", trigger: "change" }]
});

const btnFlag = ref(true);    // 启用/停用标记
const itemForm = ref(null);   // 表单引用


// ========== 3. 表格序号计算 ==========
// 序号计算：基于「当前页码」和「每页条数」动态计算
const indexMethod = (index) => {
  // 公式：(当前页码-1)×每页条数 + 行索引(从0开始) + 1
  return (page.value.currentPage - 1) * page.value.pageSize + index + 1;
};


// ========== 4. 查询功能（重置页码+重新拉取） ==========
const query = () => {
  queryParams.value.currentPage = 1; // 回到第1页
  getNurseItemList();
};

// 切换启用/停用（同时重置页码）
const enable = () => {
  btnFlag.value = true;
  queryParams.value.status = 1;
  queryParams.value.currentPage = 1;
  getNurseItemList();
};
const disable = () => {
  btnFlag.value = false;
  queryParams.value.status = 2;
  queryParams.value.currentPage = 1;
  getNurseItemList();
};


// ========== 6. 表单操作（新增/编辑/删除） ==========
const edit = (row) => {
  dialog.value.tops = "修改护理项目";
  dialog.value.dialogVisible = true;
  nextTick(() => {
    dialog.value.item = { ...row }; // 复制行数据（包含is_deleted）
    if (itemForm.value) itemForm.value.clearValidate();
  });
};

const addItem = () => {
  dialog.value.tops = "添加护理项目";
  dialog.value.dialogVisible = true;
  dialog.value.item = {
    id: "",
    serialNumber: "",
    nursingName: "",
    servicePrice: "",
    message: "",
    status: 1,
    executionCycle: "",
    executionTimes: "",
    is_deleted: 0
  };
  nextTick(() => {
    if (itemForm.value) itemForm.value.clearValidate();
  });
};

const handleClose = () => {
  dialog.value.dialogVisible = false;
  resetForm();
};

const cancel = () => {
  handleClose();
};

const resetForm = () => {
  if (itemForm.value) {
    itemForm.value.resetFields();
    dialog.value.item = {
      id: "",
      serialNumber: "",
      nursingName: "",
      servicePrice: "",
      message: "",
      status: 1,
      executionCycle: "",
      executionTimes: "",
      is_deleted: 0
    };
  }
};

const save = () => {
  if (!itemForm.value) return;
  itemForm.value.validate((valid) => {
    if (valid) {
      const api = dialog.value.item.id ? updateNurseItem : addNurseItem;
      api(dialog.value.item)
        .then(res => {
          if (res.flag) {
            ElMessage.success(res.message || "操作成功");
            query(); // 保存后重置到第1页
            handleClose();
          } else {
            ElMessage.error(res.message || "操作失败");
          }
        })
        .catch(err => {
          console.error("保存失败:", err);
          ElMessage.error("网络请求失败");
        });
    }
  });
};

const del = (id) => {
  ElMessageBox.confirm("此操作将删除记录，是否继续?", "提示", {
    type: "warning"
  }).then(() => {
    delNurseItem(id)
      .then(res => {
        if (res.flag) {
          ElMessage.success(res.message || "删除成功");
          query(); // 删除后重置到第1页
        } else {
          ElMessage.error(res.message || "删除失败");
        }
      })
      .catch(err => {
        console.error("删除失败:", err);
        ElMessage.error("网络请求失败");
      });
  }).catch(() => {});
};


// ========== 7. 核心：获取数据（严格映射后端分页字段） ==========
const getNurseItemList = () => {
  findNurseItem(queryParams.value)
    .then(res => {
      if (res.flag) {
        const data = res.data || {};
        nurseItemList.value = data.records || []; 
        
        // 关键：用后端返回的「真实分页参数」更新前端状态
        page.value.total = data.total || 0;
        page.value.currentPage = data.currentPage || 1; // 后端返回的当前页码
        page.value.pageSize = data.pageSize || 6;       // 后端返回的每页条数（可能动态变化）
        page.value.pages = data.pages || 0;             // 总页数
      }
    });
};


// ========== 8. 页面初始化 ==========
onMounted(() => {
  getNurseItemList(); // 初始加载数据
});
</script> -->
<script setup>
import { ref, nextTick, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from 'element-plus';
import { addNurseItem, findNurseItem, updateNurseItem, delNurseItem } from "../../api/nurseItemApi.js";

const router = useRouter();

// 分页状态（保持简单）
const page = ref({
  total: 0,
  currentPage: 1,   // 前端始终从1开始计数
  pageSize: 6
});

// 查询参数（保持原有字段）
const queryParams = ref({
  itemName: '',
  status: 1,
  currentPage: 1,
  pageSize: 6
});

const nurseItemList = ref([]);
const btnFlag = ref(true);

// 对话框数据（保持不变）
const dialog = ref({
  dialogVisible: false,
  tops: "添加护理项目",
  item: {
    id: "",
    serialNumber: "",
    nursingName: "",
    servicePrice: "",
    message: "",
    status: 1,
    executionCycle: "",
    executionTimes: "",
    is_deleted: 0
  },
  statusArr: [
    { value: 1, label: "启用" },
    { value: 2, label: "停用" }
  ]
});

// 表单校验规则（保持不变）
const rules = ref({
  serialNumber: [{ required: true, message: "请输入编号", trigger: "blur" }],
  nursingName: [{ required: true, message: "请输入名称", trigger: "blur" }],
  servicePrice: [{ required: true, message: "请输入价格", trigger: "blur" }],
  status: [{ required: true, message: "请选择状态", trigger: "change" }]
});

const itemForm = ref(null);

// ========== 核心方法 ==========
const getNurseItemList = () => {
  // 直接使用queryParams发送请求（不再转换页码）
  findNurseItem(queryParams.value)
    .then(res => {
      if (res.flag && res.data) {
        nurseItemList.value = res.data.records || [];
        
        // 更新分页信息（假设后端返回total和pageSize）
        page.value.total = res.data.total || 0;
        page.value.pageSize = res.data.pageSize || 6;
        
        // 重要：保持前后端页码一致
        page.value.currentPage = queryParams.value.currentPage;
      }
    })
    .catch(error => {
      console.error("获取数据失败:", error);
      ElMessage.error("获取数据失败");
    });
};

// ========== 分页控制 ==========
const handleCurrentChange = (newPage) => {
  queryParams.value.currentPage = newPage;
  page.value.currentPage = newPage; // 同步更新
  getNurseItemList();
};

// ========== 序号计算 ==========
const indexMethod = (index) => {
  // 简单计算：(当前页-1)*每页条数 + 当前行索引 + 1
  return (page.value.currentPage - 1) * page.value.pageSize + index + 1;
};

// ========== 查询和状态切换 ==========
const query = () => {
  queryParams.value.currentPage = 1;
  page.value.currentPage = 1;
  getNurseItemList();
};

const enable = () => {
  btnFlag.value = true;
  queryParams.value.status = 1;
  query();
};

const disable = () => {
  btnFlag.value = false;
  queryParams.value.status = 2;
  query();
};

// ========== 表单操作（保持不变） ==========
const addItem = () => {
  dialog.value.tops = "添加护理项目";
  dialog.value.dialogVisible = true;
  dialog.value.item = {
    id: "",
    serialNumber: "",
    nursingName: "",
    servicePrice: "",
    message: "",
    status: 1,
    executionCycle: "",
    executionTimes: "",
    is_deleted: 0
  };
  nextTick(() => itemForm.value?.clearValidate());
};

const edit = (row) => {
  dialog.value.tops = "修改护理项目";
  dialog.value.dialogVisible = true;
  dialog.value.item = { ...row };
  nextTick(() => itemForm.value?.clearValidate());
};

const save = () => {
  itemForm.value?.validate((valid) => {
    if (valid) {
      const api = dialog.value.item.id ? updateNurseItem : addNurseItem;
      api(dialog.value.item)
        .then(res => {
          if (res.flag) {
            ElMessage.success(res.message || "操作成功");
            query();
            dialog.value.dialogVisible = false;
          } else {
            ElMessage.error(res.message || "操作失败");
          }
        })
        .catch(error => {
          console.error("操作失败:", error);
          ElMessage.error("网络请求失败");
        });
    }
  });
};

const del = (id) => {
  ElMessageBox.confirm("确认删除此记录吗？", "提示", { type: "warning" })
    .then(() => delNurseItem(id))
    .then(res => {
      if (res.flag) {
        ElMessage.success("删除成功");
        query();
      } else {
        ElMessage.error(res.message || "删除失败");
      }
    })
    .catch(() => {});
};

// 初始化加载
onMounted(getNurseItemList);
</script>

<style scoped >
.activeBtn {
  color: #1890ff;
  background: rgb(232, 244, 255);
}
</style>