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
                                size="large">
                                <template #append>
                                    <el-button type="info" @click="query" style="color:black">查询</el-button>
                                </template>
                            </el-input>
                        </el-col>
            <el-col :span="17" v-if="roleId===2">
                <el-button type="primary" @click="addItem" color="#337ab7" style="margin-top:4px">
                    <el-icon>
                        <Plus />
                    </el-icon>
                    <span>添加外出申请</span>
                </el-button>
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
                                <el-table-column align="center" prop="customerName" label="姓名"/>
                                <el-table-column align="center" prop="customerAge" label="年龄"/>
                                <el-table-column align="center" prop="customerSex" label="性别">
                                    <template #default="scope">{{ scope.row.customerSex==0?'男':'女'}}</template>
                                </el-table-column>
                                <el-table-column align="center" prop="bedNo" label="床号"/>
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
                            <div class="table-main-header">外出登记</div>
                            <!-- 表格 -->
                            <el-table :data="outwardList" size="small">
                                <el-table-column
                                    align="center"
                                    :index="indexMethodRecord"
                                    type="index"
                                    label="序号"
                                    width="40"
                                />
                                <el-table-column align="center" prop="customerName" label="客户名称" width="100" />
                                <el-table-column align="center" prop="outgoingreason" label="外出事由" width="100" />
                                <el-table-column align="center" prop="outgoingtime" label="外出时间" width="70" />
                                <el-table-column align="center" prop="expectedreturntime" label="预计回院时间" width="100" />
                                <el-table-column align="center" prop="actualreturntime" label="实际回院时间" width="100" />
                                <el-table-column align="center" prop="escorted" label="陪同人" width="100" />
                <el-table-column align="center" prop="relation" label="与老人关系" width="100" />
                <el-table-column align="center" prop="escortedtel" label="陪同人电话" width="100" />
                <!-- <el-table-column align="center" prop="status" label="审批状态" width="100" /> -->
                                <el-table-column align="center" prop="auditstatus" label="审批状态" width="100">
                    <template #default="{row}">
                        {{row.auditstatus===0?'已提交':row.auditstatus===1?'同意':'拒绝'}}
                    </template>
                </el-table-column>
                                <el-table-column align="center" label="操作" width="100">
                                    <template #default="scope">
                                        <el-button v-if="scope.row.auditstatus === 1 && roleId === 2" :disabled="scope.row.actualreturntime" type="success" size="small" round plain @click="updateTime(scope.row.id)">登记回院时间</el-button>
                    <el-button  v-if="scope.row.auditstatus === 0 && roleId === 2" type="danger" size="small" round plain @click="del(scope.row.id)">撤销申请</el-button>
                    <el-button  v-if="scope.row.auditstatus === 0 && roleId !== 2" type="primary" size="small" round plain @click="examine(scope.row.id)">审批</el-button>
                                    </template>
                                </el-table-column>
                            </el-table>
                            <!-- 分页插件 -->
                            <div style="margin-top:15px" v-if="outwardList.length>0">
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
    <!-- 修改/添加对话框 -->
    <el-dialog
        v-model="dialog.dialogTimeVisible"
        :title="dialog.tops"
        width="40%"
        align-center
        draggable
        :before-close="handleTimeClose"
    >
        <el-divider border-style="double" style="margin:0;" />
        <el-form
            label-position="right"
            label-width="auto"
            style="max-width:380px;margin:20px auto"
            class="demo-form-inline"
            ref="itemTimeForm"
            :model="dialog.item"
            :rules="rules"
        >
     
            <el-form-item label="实际回院时间： " prop="actualreturntime">
            <el-date-picker
                    style="width:320px" 
                            v-model="dialog.item.actualreturntime"
                            type="date"
                            placeholder="实际回院时间:"
                    value-format="YYYY-MM-DD"
                        />
            </el-form-item>
        
        </el-form>
        <el-divider border-style="double" style="margin:0;" />
        <template #footer>
            <span class="dialog-footer" style="padding-top: 0px">
                <el-button type="primary" @click="updateBackTime('itemTimeForm')">保存</el-button>
                <el-button @click="cancelTime">取消</el-button>
            </span>
        </template>
    </el-dialog>
    <!-- 修改/添加对话框 -->
    <el-dialog
        v-model="dialog.dialogExamineVisible"
        :title="dialog.tops"
        width="40%"
        align-center
        draggable
        :before-close="handleExamineClose"
    >
        <el-divider border-style="double" style="margin:0;" />
        <el-form
            label-position="right"
            label-width="auto"
            style="max-width:380px;margin:20px auto"
            class="demo-form-inline"
            ref="itemExamineForm"
            :model="dialog.item"
            :rules="rules"
        >
     
            <el-form-item label="审批：" prop="auditstatus">
            <el-radio-group v-model="dialog.item.auditstatus" class="ml-4">
                        <el-radio label="1">同意</el-radio>
                        <el-radio label="2">拒绝</el-radio>
            </el-radio-group>
            </el-form-item>
        
        </el-form>
        <el-divider border-style="double" style="margin:0;" />
        <template #footer>
            <span class="dialog-footer" style="padding-top: 0px">
                <el-button type="primary" @click="examineOutward('itemExamineForm')">保存</el-button>
                <el-button @click="cancelExamine">取消</el-button>
            </span>
        </template>
    </el-dialog>
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
     <!-- <el-form-item label="创建时间：" prop="createTime">
            <el-input v-model="dialog.item.createTime" />
        </el-form-item> -->
            <el-form-item label="客户姓名：" prop="customerId">
            <el-select v-model="dialog.item.customerId" style="width:320px" placeholder="客户姓名：">
                <el-option v-for="item in customerList" :label="item.customerName" :value="item.id" />
            </el-select>
            </el-form-item>
        <el-form-item label="外出事由：" prop="outgoingreason">
            <el-input v-model="dialog.item.outgoingreason" />
        </el-form-item>
         <!-- <el-form-item label="性别：" prop="sex">
            <el-select v-model="dialog.item.sex" style="width:320px" placeholder="性别：">
                <el-option label="男" value="1" />
                <el-option label="女" value="2" />
            </el-select>
            </el-form-item> -->
            <el-form-item label="外出时间：" prop="outgoingtime">
            <el-date-picker
                    style="width:320px" 
                            v-model="dialog.item.outgoingtime"
                            type="date"
                            placeholder="外出时间:"
                    value-format="YYYY-MM-DD"
                        />
            </el-form-item>
        
            <el-form-item label="预计回院时间： " prop="expectedreturntime">
            <el-date-picker
                    style="width:320px" 
                            v-model="dialog.item.expectedreturntime"
                            type="date"
                            placeholder="外出时间:"
                    value-format="YYYY-MM-DD"
                        />
            </el-form-item>
        
        <el-form-item label="陪同人： " prop="escorted">
            <el-input v-model="dialog.item.escorted" />
        </el-form-item>
        
        
        <el-form-item label="陪同人电话： " prop="escortedtel">
            <el-input v-model="dialog.item.escortedtel" />
        </el-form-item>
    
            <!-- <el-form-item label="状态：" prop="isDeleted">
                <el-select v-model="dialog.item.isDeleted" style="width:320px" placeholder="状态">
                    <el-option
                        v-for="item in dialog.statusArr"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value"
                    />
                </el-select>
            </el-form-item> -->
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

<!-- 
<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import {
    selectCustomer
} from '../../api/customerApi';
import { listOutwardRecord, addOutwardRecord, updateOutwardRecord, deleteOutwardRecord, auditOutwardRecord } from '../../api/outwardApi';

// 查询条件
const condition = ref({
    customerName: '',
    currentPage: 1,
    pageSize: 10
})

// 客户信息分页
const page = ref({
    currentPage: 1,
    pageSize: 10,
    total: 0
})

// 外出记录分页
const pageRecord = ref({
    currentPage: 1,
    pageSize: 10,
    total: 0
})

// 客户信息列表
const khxxList = ref([])
// 外出记录列表
const outwardList = ref([])
// 客户下拉列表
const customerList = ref([])

// 角色ID（1:管理员 2:普通用户）
const roleId = ref(2)

// 对话框状态
const dialog = ref({
    dialogVisible: false,
    dialogTimeVisible: false,
    dialogExamineVisible: false,
    tops: '',
    item: {
        id: null,
        customerId: null,
        outgoingreason: '',
        outgoingtime: '',
        expectedreturntime: '',
        escorted: '',
        escortedtel: '',
        actualreturntime: '',
        auditstatus: 0
    }
})

// 表单校验规则
const rules = ref({
    customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
    outgoingreason: [{ required: true, message: '请输入外出事由', trigger: 'blur' }],
    outgoingtime: [{ required: true, message: '请选择外出时间', trigger: 'change' }],
    expectedreturntime: [{ required: true, message: '请选择预计回院时间', trigger: 'change' }],
    escorted: [{ required: true, message: '请输入陪同人', trigger: 'blur' }],
    escortedtel: [{ 
        required: true, 
        message: '请输入陪同人电话', 
        trigger: 'blur', 
        pattern: /^1[3-9]\d{9}$/, 
        message: '请输入正确的手机号码' 
    }],
    actualreturntime: [{ required: true, message: '请选择实际回院时间', trigger: 'change' }],
    auditstatus: [{ required: true, message: '请选择审批状态', trigger: 'change' }]
})

// 查询客户信息
const query = () => {
    const params = {
        customerName: condition.value.customerName,
        currentPage: page.value.currentPage,
        pageSize: page.value.pageSize
    }
    
    selectCustomer(params).then(res => {
        if (res.flag && res.data) {
            khxxList.value = res.data.records || []
            page.value.total = res.data.total || 0
            customerList.value = khxxList.value.map(item => ({ id: item.id, customerName: item.customerName }))
        } else {
            ElMessage.error(res.message || '查询失败')
        }
    }).catch(error => {
        console.error('查询失败', error)
        ElMessage.error('查询失败')
    })
}

// 重置查询条件
const resetQuery = () => {
    condition.value.customerName = ''; // 重置客户姓名输入
    page.value.currentPage = 1;        // 重置到第一页
    query();                          // 执行查询
}

// 客户信息分页切换
const handleCurrentChange = (val) => {
    page.value.currentPage = val
    query()
}

// 外出记录分页切换
const handleRecordChange = (val) => {
    pageRecord.value.currentPage = val
    loadOutwardRecords()
}

// 加载外出记录
const loadOutwardRecords = (customerId = null) => {
    const params = {
        currentPage: pageRecord.value.currentPage,
        pageSize: pageRecord.value.pageSize,
        customerId: customerId
    }
    
    listOutwardRecord(params).then(res => {
        if (res.flag && res.data) {
            outwardList.value = res.data.records || []
            pageRecord.value.total = res.data.total || 0
        } else {
            ElMessage.error(res.message || '查询失败')
        }
    }).catch(error => {
        console.error('查询失败', error)
        ElMessage.error('查询失败')
    })
}

// 表格序号计算
const indexMethod = (index) => (page.value.currentPage - 1) * page.value.pageSize + index + 1
const indexMethodRecord = (index) => (pageRecord.value.currentPage - 1) * pageRecord.value.pageSize + index + 1

// 选择客户并查询其外出记录
const handleChangeCustomer = (row) => {
    if (row && row.id) {
        pageRecord.value.currentPage = 1
        loadOutwardRecords(row.id)
    }
}

// 添加外出申请
const addItem = () => {
    dialog.value.tops = '添加外出申请'
    dialog.value.dialogVisible = true
    dialog.value.item = {
        auditstatus: 0,
        outgoingtime: new Date().toISOString().split('T')[0],
        expectedreturntime: new Date(new Date().setDate(new Date().getDate() + 1)).toISOString().split('T')[0],
        customerId: null
    }
}

// 保存外出申请
const save = async (formName) => {
    try {
        const res = await addOutwardRecord(dialog.value.item)
        if (res.flag) {
            ElMessage.success('保存成功')
            dialog.value.dialogVisible = false
            loadOutwardRecords()
        } else {
            ElMessage.error(res.message || '保存失败')
        }
    } catch (error) {
        console.error('保存失败', error)
        ElMessage.error('保存失败')
    }
}

// 撤销申请
const del = async (id) => {
    try {
        await ElMessageBox.confirm('确定撤销？此操作不可恢复', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        })
        const res = await deleteOutwardRecord(id)
        if (res.flag) {
            ElMessage.success('撤销成功')
            loadOutwardRecords()
        } else {
            ElMessage.error(res.message || '撤销失败')
        }
    } catch (error) {
        if (error !== 'cancel') ElMessage.error('撤销失败')
    }
}

// 审批外出申请
const examine = (id) => {
    dialog.value.tops = '审批外出申请'
    dialog.value.dialogExamineVisible = true
    dialog.value.item = { id, auditstatus: 1 }
}

// 审批保存
const examineOutward = async (formName) => {
    try {
        const res = await auditOutwardRecord(dialog.value.item)
        if (res.flag) {
            ElMessage.success('审批完成')
            dialog.value.dialogExamineVisible = false
            loadOutwardRecords()
        } else {
            ElMessage.error(res.message || '审批失败')
        }
    } catch (error) {
        console.error('审批失败', error)
        ElMessage.error('审批失败')
    }
}

// 登记回院时间
const updateTime = (id) => {
    dialog.value.tops = '登记回院时间'
    dialog.value.dialogTimeVisible = true
    dialog.value.item = { 
        id, 
        actualreturntime: new Date().toISOString().split('T')[0] 
    }
}

// 保存回院时间
const updateBackTime = async (formName) => {
    try {
        const res = await updateOutwardRecord(dialog.value.item)
        if (res.flag) {
            ElMessage.success('更新成功')
            dialog.value.dialogTimeVisible = false
            loadOutwardRecords()
        } else {
            ElMessage.error(res.message || '更新失败')
        }
    } catch (error) {
        console.error('更新失败', error)
        ElMessage.error('更新失败')
    }
}

// 关闭对话框
const handleClose = () => dialog.value.dialogVisible = false
const handleTimeClose = () => dialog.value.dialogTimeVisible = false
const handleExamineClose = () => dialog.value.dialogExamineVisible = false
const cancel = () => dialog.value.dialogVisible = false
const cancelTime = () => dialog.value.dialogTimeVisible = false
const cancelExamine = () => dialog.value.dialogExamineVisible = false

// 组件挂载时加载数据
onMounted(() => {
    query()
    loadOutwardRecords()
})
</script> -->
<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { selectCustomer } from '../../api/customerApi';
import { listOutwardRecord, addOutwardRecord, updateOutwardRecord, deleteOutwardRecord, auditOutwardRecord } from '../../api/outwardApi';

// 查询条件（客户列表）
const condition = ref({
  customerName: '',
  currentPage: 1,
  pageSize: 6
});

// 客户信息分页
const page = ref({
  currentPage: 1,
  pageSize: 6,
  total: 0
});

// 外出记录分页
const pageRecord = ref({
  currentPage: 1,
  pageSize: 6,
  total: 0
});


// 角色ID
const roleId = ref(2);

// 对话框状态
const dialog = ref({
  dialogVisible: false,
  dialogTimeVisible: false,
  dialogExamineVisible: false,
  tops: '',
  item: {
    id: null,
    customerId: null,
    outgoingreason: '',
    outgoingtime: '',
    expectedreturntime: '',
    escorted: '',
    escortedtel: '',
    actualreturntime: '',
    auditstatus: 0
  }
});

// 表单校验规则
const rules = ref({
  customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
  outgoingreason: [{ required: true, message: '请输入外出事由', trigger: 'blur' }],
  outgoingtime: [{ required: true, message: '请选择外出时间', trigger: 'change' }],
  expectedreturntime: [{ required: true, message: '请选择预计回院时间', trigger: 'change' }],
  escorted: [{ required: true, message: '请输入陪同人', trigger: 'blur' }],
  escortedtel: [{ 
    required: true, 
    message: '请输入陪同人电话', 
    trigger: 'blur', 
    pattern: /^1[3-9]\d{9}$/, 
    message: '请输入正确的手机号码' 
  }],
  actualreturntime: [{ required: true, message: '请选择实际回院时间', trigger: 'change' }],
  auditstatus: [{ required: true, message: '请选择审批状态', trigger: 'change' }]
});
const khxxList = ref([]);
const outwardList = ref([]);
const customerList = ref([]);

// 查询客户信息
const query = () => {
  // 确保页码合法（≥1）
  condition.value.currentPage = Math.max(1, condition.value.currentPage);
  
  const params = {
    customerName: condition.value.customerName,
    currentPage: condition.value.currentPage, // 直接传递前端页码（假设后端1-based）
    pageSize: condition.value.pageSize
  };
  
  selectCustomer(params).then(res => {
    if (res.flag && res.data) {
      khxxList.value = res.data.records || []; // 客户数据赋值
      // 同步分页信息（从后端响应提取）
      page.value = {
        total: res.data.total || 0,
        currentPage: condition.value.currentPage,
        pageSize: res.data.pageSize || condition.value.pageSize
      };
      // 构建客户下拉列表（用于添加外出申请）
      customerList.value = khxxList.value.map(item => ({ 
        id: item.id, 
        customerName: item.customerName 
      }));
    } else {
      ElMessage.error(res.message || '查询失败');
    }
  }).catch(error => {
    console.error('客户查询失败:', error);
    ElMessage.error('查询失败');
  });
};
// 重置查询条件
const resetQuery = () => {
  condition.value.customerName = '';
  condition.value.currentPage = 1;
  query();
};

// 客户信息分页切换
const handleCurrentChange = (val) => {
  condition.value.currentPage = Math.max(1, val);
  query();
};

// 外出记录分页切换
const handleRecordChange = (val) => {
  pageRecord.value.currentPage = val;
  loadOutwardRecords();
};

// 加载外出记录（关键修改：计算正确的偏移量）
// const loadOutwardRecords = (customerId = null) => {
//   // 计算正确的偏移量：(当前页码 - 1) * 每页大小
//   const offset = (pageRecord.value.currentPage - 1) * pageRecord.value.pageSize;
  
//   const params = {
//     currentPage: pageRecord.value.currentPage - 1,
//     pageSize: pageRecord.value.pageSize,
//     customerId: customerId
//   };
  
//   console.log('外出记录请求参数:', params);
//   listOutwardRecord(params).then(res => {
//     console.log('外出记录响应:', res);
//     if (res.flag && res.data) {
//       outwardList.value = res.data.records || [];
//       pageRecord.value.total = res.data.total || 0;
//     } else {
//       ElMessage.error(res.message || '查询外出记录失败');
//     }
//   }).catch(error => {
//     console.error('查询外出记录失败:', error);
//     ElMessage.error('查询失败');
//   });
// };
// 加载外出记录
const loadOutwardRecords = (customerId = null) => {
  // 确保页码合法（≥1）
  pageRecord.value.currentPage = Math.max(1, pageRecord.value.currentPage);
  
  const params = {
    currentPage: pageRecord.value.currentPage, // 直接传递前端页码
    pageSize: pageRecord.value.pageSize,
    customerId: customerId
  };
  
  console.log('外出记录请求参数:', params); // 调试日志
  listOutwardRecord(params).then(res => {
    console.log('外出记录响应:', res); // 调试日志
    
    if (res.flag && res.data) {
      // 映射后端字段到前端期望格式
      outwardList.value = (res.data.records || []).map(item => ({
        id: item.id,
        customerName: item.customerName, // 确保后端返回该字段，否则需通过customerId关联
        outgoingreason: item.outgoingreason,
        outgoingtime: item.outgoingtime,
        expectedreturntime: item.expectedreturntime,
        actualreturntime: item.actualreturntime,
        escorted: item.escorted,
        relation: item.relation,
        escortedtel: item.escortedtel,
        auditstatus: item.auditstatus // 审批状态：0-已提交，1-同意，2-拒绝
      }));
      
      pageRecord.value = {
        total: res.data.total || 0,
        currentPage: pageRecord.value.currentPage,
        pageSize: res.data.pageSize || pageRecord.value.pageSize
      };
    } else {
      outwardList.value = []; // 清空列表
      ElMessage.error(res.message || '查询外出记录失败');
    }
  }).catch(error => {
    console.error('查询外出记录失败:', error);
    outwardList.value = []; // 清空列表
    ElMessage.error('查询失败');
  });
};

// 表格序号计算
const indexMethod = (index) => (page.value.currentPage - 1) * page.value.pageSize + index + 1;
const indexMethodRecord = (index) => (pageRecord.value.currentPage - 1) * pageRecord.value.pageSize + index + 1;

// 选择客户并查询其外出记录
const handleChangeCustomer = (row) => {
  if (row && row.id) {
    pageRecord.value.currentPage = 1;
    loadOutwardRecords(row.id);
  }
};

// 添加外出申请
const addItem = () => {
  dialog.value.tops = '添加外出申请';
  dialog.value.dialogVisible = true;
  dialog.value.item = {
    auditstatus: 0,
    outgoingtime: new Date().toISOString().split('T')[0],
    expectedreturntime: new Date(new Date().setDate(new Date().getDate() + 1)).toISOString().split('T')[0],
    customerId: null
  };
};

// 保存外出申请
const save = (formName) => {
  addOutwardRecord(dialog.value.item)
    .then(res => {
      if (res.flag) {
        ElMessage.success('保存成功');
        dialog.value.dialogVisible = false;
        loadOutwardRecords();
      } else {
        ElMessage.error(res.message || '保存失败');
      }
    })
    .catch(error => {
      console.error('保存失败:', error);
      ElMessage.error('保存失败');
    });
};

// 撤销申请
const del = (id) => {
  ElMessageBox.confirm('确定撤销？此操作不可恢复', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  .then(() => {
    deleteOutwardRecord(id)
      .then(res => {
        if (res.flag) {
          ElMessage.success('撤销成功');
          loadOutwardRecords();
        } else {
          ElMessage.error(res.message || '撤销失败');
        }
      })
      .catch(error => {
        console.error('撤销失败:', error);
        ElMessage.error('撤销失败');
      });
  })
  .catch(error => {
    if (error !== 'cancel') ElMessage.error('撤销失败');
  });
};

// 审批外出申请
const examine = (id) => {
  dialog.value.tops = '审批外出申请';
  dialog.value.dialogExamineVisible = true;
  dialog.value.item = { id, auditstatus: 1 };
};

// 审批保存
const examineOutward = () => {
  auditOutwardRecord(dialog.value.item)
    .then(res => {
      if (res.flag) {
        ElMessage.success('审批完成');
        dialog.value.dialogExamineVisible = false;
        loadOutwardRecords();
      } else {
        ElMessage.error(res.message || '审批失败');
      }
    })
    .catch(error => {
      console.error('审批失败:', error);
      ElMessage.error('审批失败');
    });
};

// 登记回院时间
const updateTime = (id) => {
  dialog.value.tops = '登记回院时间';
  dialog.value.dialogTimeVisible = true;
  dialog.value.item = { 
    id, 
    actualreturntime: new Date().toISOString().split('T')[0] 
  };
};

// 保存回院时间
const updateBackTime = (formName) => {
  updateOutwardRecord(dialog.value.item)
    .then(res => {
      if (res.flag) {
        ElMessage.success('更新成功');
        dialog.value.dialogTimeVisible = false;
        loadOutwardRecords();
      } else {
        ElMessage.error(res.message || '更新失败');
      }
    })
    .catch(error => {
      console.error('更新失败:', error);
      ElMessage.error('更新失败');
    });
};

// 关闭对话框
const handleClose = () => dialog.value.dialogVisible = false;
const handleTimeClose = () => dialog.value.dialogTimeVisible = false;
const handleExamineClose = () => dialog.value.dialogExamineVisible = false;
const cancel = () => dialog.value.dialogVisible = false;
const cancelTime = () => dialog.value.dialogTimeVisible = false;
const cancelExamine = () => dialog.value.dialogExamineVisible = false;

// 组件挂载时加载数据
onMounted(() => {
  query();
  loadOutwardRecords();
});
</script>



<style scoped>

.table-main {
  height: 600px;
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