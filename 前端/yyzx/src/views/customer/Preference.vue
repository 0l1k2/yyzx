<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <div>
          <el-row :gutter="30">
            <el-col :span="7">
              <el-input
                placeholder="客户名称"
                v-model="queryParams.customerName"
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
          <!-- 表格 -->
          <el-table :data="preferenceList" style="width: 100% ;color:black;" stripe>
            <el-table-column
              align="center"
              type="index"
              :index="indexMethod"
              label="序号"
              width="60"
            />
            <el-table-column align="center" prop="customerName" label="客户名称" width="120" />
            <el-table-column align="center"  prop="customerSex" label="性别" width="60" >
            <template #default="scope">{{ scope.row.customerSex==0?'男':'女'}}</template>
            </el-table-column>
            <el-table-column align="center" prop="customerAge" label="年龄" width="120" />
            <el-table-column align="center" prop="preferences" label="喜好" width="300" />
            <el-table-column align="center" prop="attention" label="注意事项" width="200" />
            <el-table-column align="center" prop="remark" label="备注" width="200" />
           
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
            :current-page="page.currentPag"
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
        <el-form-item label="客户姓名：" prop="customerId">
        	<el-select v-model="dialog.item.customerId" style="width:320px" placeholder="客户姓名：">
        	  <el-option v-for="item in customerList" :label="item.customerName" :value="item.id" />
        	</el-select>
        </el-form-item>
         <el-form-item label="喜好：" prop="preferences">
          <el-input v-model="dialog.item.preferences" />
        </el-form-item>
        <el-form-item label="提示：" prop="attention">
          <el-input v-model="dialog.item.attention" />
        </el-form-item>
    
        <el-form-item label="备注：" prop="remark">
          <el-input v-model="dialog.item.remark" />
        </el-form-item> 
      </el-form>
      <el-divider border-style="double" style="margin:0;" />
      <template #footer>
        <span class="dialog-footer" style="padding-top: 0px">
          <el-button type="primary" @click="save()">保存</el-button>
          <el-button @click="cancel">取消</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>



<!-- <script setup>
import { ref, onMounted, nextTick, getCurrentInstance as getKhxxList } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import {
  addPreference, findPreference, updatePreference, delPreference
} from '../../api/preferenceApi.js';
import { selectCustomer } from '../../api/customerApi.js';
import { Plus } from '@element-plus/icons-vue';

// 在 setup 顶部定义 itemForm 引用
const itemForm = ref(null);

// 模态框数据
const dialog = ref({
  dialogVisible: false,
  tops: '',
  item: {
    id: 0,
    customerId: 0,
    customerName: '',
    customerSex: '',
    customerAge: '',
    preferences: '',
    attention: '',
    remark: '',
    isDeleted: 0
  }
});

// 分页属性
const page = ref({
  total: 0,
  pageSize: 6,
  currentPag: 1,
  pagCount: 0
});

// 查询参数 - 使用后端期望的字段名
const queryParams = ref({
  customerName: '',
  current: 1,  // 当前页码
  pageSize: 6   // 每页大小
});

// 数据列表
const preferenceList = ref([]);
const customerList = ref([]);

// 表单验证规则
const rules = {
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  preferences: [{ required: true, message: '请输入喜好', trigger: 'blur' }], // 关键验证规则
  attention: [{ required: true, message: '请输入提示', trigger: 'blur' }],
  remark: [{ required: true, message: '请输入备注', trigger: 'blur' }]
};

// 序号计算方法
const indexMethod = (index) => {
  return (page.value.currentPag - 1) * page.value.pageSize + index + 1;
};


// 点击修改
// 修改edit方法
const edit = (row) => {
  dialog.value.tops = '修改护理项目';
  dialog.value.dialogVisible = true;
  nextTick(() => {
    dialog.value.item = {
      ...dialog.value.item,
      id: row.id,
      customerId: row.customerId, // 用驼峰
      customerName: row.customerName,
      customerSex: row.customerSex,
      customerAge: row.customerAge,
      preferences: row.preferences,
      attention: row.attention,
      remark: row.remark
    };
  });
};

// 点击添加按钮
const addItem = () => {
  dialog.value.tops = '添加膳食管理';
  dialog.value.dialogVisible = true;
  dialog.value.item = {
    id: '',
    customerId: '',
    customerName: '',
    customerSex: '',
    customerAge: '',
    preferences: '',
    attention: '',
    remark: '',
    isDeleted: 0
  };
};

// 关闭对话框
const handleClose = () => {
  dialog.value.dialogVisible = false;
  resetForm('itemForm');
};

// 取消操作
const cancel = () => {
  handleClose();
};

// 重置表单
// const resetForm = (formName) => {
//   const itemForm = ref(null)
//   if (form) {
//     form.resetFields();
//   }
//   dialog.value.item = {
//     id: 0,
//     customerId: 0,
//     customerName: '',
//     customerSex: '',
//     customerAge: '',
//     preferences: '',
//     attention: '',
//     remark: '',
//     isDeleted: 0
//   };
// };
// 重置表单
const resetForm = (formName) => {
  if (itemForm.value) {
    itemForm.value.resetFields();
  }
  dialog.value.item = {
    id: 0,
    customerId: 0,
    customerName: '',
    customerSex: '',
    customerAge: '',
    preferences: '',
    attention: '',
    remark: '',
    isDeleted: 0
  };
};

// 保存(新增/编辑)
// const save = (formName) => {
//   const form = getCurrentInstance()?.refs[formName];
//   if (!form){
//      console.log('表单ref未找到');
//      return;
//   } 
  
//   form.validate((valid) => {
//     console.log('表单校验结果:', valid, dialog.value.item);
//     if (!valid) return;
    
//     const apiCall = dialog.value.item.id 
//       ? updatePreference(dialog.value.item)
//       : addPreference(dialog.value.item);
    
//     apiCall
//       .then(res => {
//         if (res.flag) {
//           ElMessage.success(res.message);
//           queryParams.value.current = 1;
//           queryParams.value.customerName = '';
//           getPreferenceList();
//           getKhxxList();
//           handleClose();
//         } else {
//           ElMessage.error(res.message);
//         }
//       })
//       .catch(error => {
//         console.error('保存失败', error);
//         ElMessage.error('保存失败');
//       });
//   });
// };

// // 删除
// const del = (id) => {
//   ElMessageBox.confirm(
//     '此操作删除记录, 是否继续?',
//     '提示',
//     {
//       confirmButtonText: '确定',
//       cancelButtonText: '取消',
//       type: 'warning'
//     }
//   )
//   .then(() => {
//     delPreference({ id })
//       .then(res => {
//         if (res.flag) {
//           ElMessage.success(res.message);
//           getPreferenceList();
//         } else {
//           ElMessage.error(res.message);
//         }
//       })
//       .catch(error => {
//         console.error('删除失败', error);
//         ElMessage.error('删除失败');
//       });
//   })
//   .catch(() => {});
// };
// 保存(新增/编辑)
const save = () => {
  if (!itemForm.value) {
    ElMessage.error('表单加载失败，请刷新页面');
    return;
  }
  
  itemForm.value.validate((valid) => {
    if (!valid) {
      const errors = itemForm.value.errors;
      const errorMsg = errors.map(err => err.message).join('，');
      ElMessage.error(`表单验证失败：${errorMsg}`);
      
      const firstError = errors[0];
      if (firstError) {
        nextTick(() => {
          const fieldEl = document.querySelector(`[prop="${firstError.field}"] input`);
          fieldEl?.focus();
        });
      }
      return;
    }
    
    const { id, ...itemData } = dialog.value.item;
    const apiPromise = id 
      ? updatePreference({ id, ...itemData })
      : addPreference(itemData);
    
    apiPromise
      .then(res => {
        if (res.flag) {
          ElMessage.success(res.message);
          queryParams.value.current = 1;
          getPreferenceList()
            .then(() => getKhxxList())
            .then(() => handleClose());
        } else {
          ElMessage.error(res.message || '保存失败，请稍后重试');
        }
      })
      .catch(error => {
        console.error('保存异常', error);
        ElMessage.error('系统错误，请联系管理员');
      });
  });
};
const del = (id) => {
  ElMessageBox.confirm(
    '此操作将永久删除记录，无法恢复，是否继续?',
    '警告',
    {
      confirmButtonText: '确认删除',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
  .then(() => {
    return delPreference( id );
  })
  .then(res => {
    if (res.flag) {
      ElMessage.success(res.message);
      queryParams.value.current = 1;
      return getPreferenceList();
    } else {
      ElMessage.error(res.message);
      throw new Error(res.message);
    }
  })
  .catch(error => {
    if (error !== 'cancel') {
      console.error('删除操作失败', error);
      ElMessage.error('删除记录失败，请稍后重试');
    }
  });
};

// 查询护理项目(分页)
// const getPreferenceList = () => {
//   findPreference(queryParams.value)
//     .then(res => {
//       console.log('API响应:', res); // 添加日志
      
//       // 处理不同的响应结构
//       let data = res.data;
      
//       // 情况1：直接返回记录数组
//       if (Array.isArray(data)) {
//         preferenceList.value = data;
//         page.value.total = data.length;
//         return;
//       }
      
//       // 情况2：返回分页结构
//       if (data && data.records) {
//         preferenceList.value = data.records || [];
//         page.value = {
//           total: data.total || 0,
//           pageSize: data.size || data.Size || queryParams.value.pageSize,
//           currentPag: data.current || 1,
//           pagCount: data.pages || 0
//         };
//       } 
//       // 情况3：返回其他结构
//       else if (data && data.data) {
//         preferenceList.value = data.data.records || [];
//         page.value = {
//           total: data.data.total || 0,
//           pageSize: data.data.size || data.data.Size || queryParams.value.pageSize,
//           currentPag: data.data.current || 1,
//           pagCount: data.data.pages || 0
//         };
//       }
//       // 无法识别的结构
//       else {
//         console.error('无法识别的响应结构:', data);
//         preferenceList.value = [];
//         page.value.total = 0;
//         ElMessage.warning('未获取到有效数据');
//       }
//     })
//     .catch(error => {
//       console.error('获取喜好列表失败', error);
//       ElMessage.error('获取数据失败');
//       preferenceList.value = [];
//       page.value.total = 0;
//     });
// };
// 查询护理项目(分页)
const getPreferenceList = () => {
  // 关键：添加 return，让方法返回 Promise
  return findPreference(queryParams.value)
    .then(res => {
      console.log('完整API响应:', res);
      if (res.flag && res.data) {
        preferenceList.value = res.data.records || [];
        page.value = {
          total: res.data.total || 0,
          pageSize: res.data.size || page.value.pageSize,
          currentPag: res.data.current || page.value.currentPag,
          pagCount: res.data.pages || 0
        };
      } else if (!res.flag) {
        ElMessage.error(res.message || '请求失败');
      } else {
        preferenceList.value = [];
        page.value.total = 0;
        ElMessage.warning('未获取到数据');
      }
    })
    .catch(error => {
      console.error('获取喜好列表失败', error);
      ElMessage.error('获取数据失败，请稍后重试');
      preferenceList.value = [];
      page.value.total = 0;
    });
};

// 查询客户信息列表-分页
// const getKhxxList = () => {
//   selectCustomer(condition.value)
//     .then(res => {
//       // 处理不同的响应结构
//       if (Array.isArray(res.data)) {
//         customerList.value = res.data;
//       } else if (res.data && res.data.records) {
//         customerList.value = res.data.records;
//       } else if (res.data && res.data.data) {
//         customerList.value = res.data.data.records;
//       } else {
//         customerList.value = [];
//       }
//     })
//     .catch(error => {
//       console.error('获取客户列表失败', error);
//       ElMessage.error('获取客户信息失败');
//       customerList.value = [];
//     });
// };

// 查询条件-客户
const condition = ref({
  customerName: '',
  currentPage: 1,  // ✅ 对应后端 currentPage
  pageSize: 6
});

// 处理页码变化
const handleCurrentChange = (current) => {
  queryParams.value.current = current; // 喜好列表的当前页
  condition.value.currentPage = current; // 客户列表的当前页 ✅
  getPreferenceList();
  getKhxxList();
};

// 点击查询
const query = () => {
  queryParams.value.current = 1; 
  condition.value.currentPage = 1; // ✅
  getPreferenceList();
  getKhxxList();
};
// 组件挂载时加载数据
onMounted(() => {
  condition.value.offset = 0;
  getKhxxList();
  getPreferenceList();

});
// 修改后的 getKhxxList 方法
const getKhxxList = () => {
  selectCustomer(condition.value)
    .then(res => {
      console.log('客户列表数据:', res.data);
      if (res.data && res.data.records) {
        // 转换后端字段为前端需要的格式
        customerList.value = res.data.records.map(item => ({
          id: item.id,                     // 后端的 id 映射到前端的 id
          customerName: item.customerName, // 客户姓名
          // 其他需要的字段
        }));
      } else {
        customerList.value = [];
      }
    })
    .catch(error => {
      console.error('获取客户列表失败', error);
      customerList.value = [];
    });
};
</script> -->
<script setup>
import { ref, onMounted, nextTick } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { addPreference, findPreference, updatePreference, delPreference } from '../../api/preferenceApi.js';
import { selectCustomer } from '../../api/customerApi.js';
import { Plus } from '@element-plus/icons-vue';

// 表单引用（与模板的 ref="itemForm" 对应）
const itemForm = ref(null);

// 模态框数据（仅保留必要字段）
const dialog = ref({
  dialogVisible: false,
  tops: '',
  item: {
    id: 0,
    customerId: 0, // 关联客户的 ID
    preferences: '',
    attention: '',
    remark: '',
    isDeleted: 0
  }
});

// 喜好列表分页参数（与后端接口匹配）
const page = ref({
  total: 0,
  pageSize: 6,
  currentPag: 1,
  pagCount: 0
});

// 喜好列表查询参数（后端接口字段）
const queryParams = ref({
  customerName: '', // 搜索关键词
  current: 1,       // 当前页码
  pageSize: 6       // 每页条数
});

// 数据列表
const preferenceList = ref([]); // 喜好列表
const customerList = ref([]);   // 客户列表（用于下拉框）

// 客户列表查询条件（与后端 KhxxDto 匹配）
const condition = ref({
  customerName: '',   // 搜索关键词（同步 queryParams）
  currentPage: 1,     // 当前页码
  pageSize: 6         // 每页条数
});

// 表单验证规则
const rules = {
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  preferences: [{ required: true, message: '请输入喜好', trigger: 'blur' }],
  attention: [{ required: true, message: '请输入提示', trigger: 'blur' }],
  remark: [{ required: true, message: '请输入备注', trigger: 'blur' }]
};

// 表格序号计算
const indexMethod = (index) => {
  return (page.value.currentPag - 1) * page.value.pageSize + index + 1;
};

// 编辑功能
const edit = (row) => {
  dialog.value.tops = '修改护理项目';
  dialog.value.dialogVisible = true;
  nextTick(() => {
    dialog.value.item = {
      ...dialog.value.item,
      id: row.id,
      customerId: row.customerId,
      preferences: row.preferences,
      attention: row.attention,
      remark: row.remark
    };
  });
};

// 添加功能
const addItem = () => {
  dialog.value.tops = '添加膳食管理';
  dialog.value.dialogVisible = true;
  dialog.value.item = {
    id: 0,
    customerId: 0,
    preferences: '',
    attention: '',
    remark: '',
    isDeleted: 0
  };
};

// 关闭对话框（含表单重置）
const handleClose = () => {
  dialog.value.dialogVisible = false;
  resetForm();
};

// 取消操作
const cancel = () => {
  handleClose();
};

// 重置表单
const resetForm = () => {
  if (itemForm.value) {
    itemForm.value.resetFields();
  }
  dialog.value.item = {
    id: 0,
    customerId: 0,
    preferences: '',
    attention: '',
    remark: '',
    isDeleted: 0
  };
};

// 保存功能（新增/编辑）
const save = () => {
  if (!itemForm.value) {
    ElMessage.error('表单加载失败，请刷新页面');
    return;
  }

  itemForm.value.validate((valid) => {
    if (!valid) {
      const errorMsg = itemForm.value.errors.map(err => err.message).join('，');
      ElMessage.error(`表单验证失败：${errorMsg}`);
      // 自动聚焦到第一个错误字段
      const firstError = itemForm.value.errors[0];
      if (firstError) {
        nextTick(() => {
          const inputEl = document.querySelector(`[prop="${firstError.field}"] input`);
          inputEl?.focus();
        });
      }
      return;
    }

    const { id, ...itemData } = dialog.value.item;
    const apiPromise = id 
      ? updatePreference({ id, ...itemData })
      : addPreference(itemData);

    apiPromise
      .then(res => {
        if (res.flag) {
          ElMessage.success(res.message);
          queryParams.value.current = 1; // 重置页码
          // 先刷新喜好列表，再刷新客户列表，最后关闭弹窗
          getPreferenceList()
            .then(() => getKhxxList())
            .then(() => handleClose());
        } else {
          ElMessage.error(res.message || '保存失败，请稍后重试');
        }
      })
      .catch(error => {
        console.error('保存异常:', error);
        ElMessage.error('系统错误，请联系管理员');
      });
  });
};

// 删除功能
const del = (id) => {
  ElMessageBox.confirm(
    '此操作将永久删除记录，无法恢复，是否继续?',
    '警告',
    {
      confirmButtonText: '确认删除',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
  .then(() => delPreference(id))
  .then(res => {
    if (res.flag) {
      ElMessage.success(res.message);
      queryParams.value.current = 1; // 重置页码
      return getPreferenceList();
    } else {
      ElMessage.error(res.message);
      throw new Error(res.message);
    }
  })
  .catch(error => {
    if (error !== 'cancel') {
      console.error('删除失败:', error);
      ElMessage.error('删除记录失败，请稍后重试');
    }
  });
};

// 查询喜好列表（分页）
const getPreferenceList = () => {
  return findPreference(queryParams.value)
    .then(res => {
      console.log('喜好列表响应:', res);
      if (res.flag && res.data) {
        preferenceList.value = res.data.records || [];
        page.value = {
          total: res.data.total || 0,
          pageSize: res.data.size || page.value.pageSize,
          currentPag: res.data.current || page.value.currentPag,
          pagCount: res.data.pages || 0
        };
      } else if (!res.flag) {
        ElMessage.error(res.message || '请求失败');
      } else {
        preferenceList.value = [];
        page.value.total = 0;
        ElMessage.warning('未获取到数据');
      }
    })
    .catch(error => {
      console.error('获取喜好列表失败:', error);
      ElMessage.error('获取数据失败，请稍后重试');
      preferenceList.value = [];
      page.value.total = 0;
    });
};

// 查询客户列表（分页，供下拉框使用）
const getKhxxList = () => {
  // 同步搜索关键词：将喜好列表的搜索词同步到客户查询
  condition.value.customerName = queryParams.value.customerName;
  
  return selectCustomer(condition.value)
    .then(res => {
      console.log('客户列表响应:', res);
      if (res.flag && res.data && res.data.records) {
        // 转换为下拉框需要的格式：{ id, customerName }
        customerList.value = res.data.records.map(item => ({
          id: item.id,
          customerName: item.customerName
        }));
      } else {
        customerList.value = [];
        ElMessage.warning('未获取到客户数据');
      }
    })
    .catch(error => {
      console.error('获取客户列表失败:', error);
      ElMessage.error('获取客户信息失败');
      customerList.value = [];
    });
};

// 分页切换事件
const handleCurrentChange = (current) => {
  queryParams.value.current = current; // 更新喜好列表页码
  condition.value.currentPage = current; // 更新客户列表页码
  getPreferenceList();
  getKhxxList();
};

// 查询按钮事件（重置页码）
const query = () => {
  queryParams.value.current = 1; // 喜好列表回到第一页
  condition.value.currentPage = 1; // 客户列表回到第一页
  getPreferenceList();
  getKhxxList();
};

// 组件挂载时初始化数据
onMounted(() => {
  getKhxxList();   // 先加载客户列表（供下拉框）
  getPreferenceList(); // 再加载喜好列表
});
</script>


<style scoped >
.activeBtn {
  color: #1890ff;
  background: rgb(232, 244, 255);
}
</style>