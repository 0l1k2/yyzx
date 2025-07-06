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
			<el-col :span="17" v-if="roleId===2">
			  <el-button type="primary" @click="addItem" color="#337ab7" style="margin-top:4px">
			    <el-icon>
			      <Plus />
			    </el-icon>
			    <span>添加退住申请</span>
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
              <div class="table-main-header">退住登记</div>
              <!-- 表格 -->
              <el-table :data="backdownList" size="small">
                <el-table-column
                  align="center"
                  :index="indexMethodRecord"
                  type="index"
                  label="序号"
                  width="40"
                />
                <el-table-column align="center" prop="customerName" label="客户名称" width="100" />
                <el-table-column align="center" prop="checkinDate" label="入住时间" width="100" />
                <el-table-column align="center" prop="retreattime" label="退住时间" width="70" />
                <el-table-column align="center" prop="retreattype" label="退住类型" width="100" />
                <el-table-column align="center" prop="retreatreason" label="退住原因" width="100" />
                <el-table-column align="center" prop="audittime" label="审批时间" width="100" />
				<!-- <el-table-column align="center" prop="status" label="审批状态" width="100" /> -->
                <el-table-column align="center" prop="auditstatus" label="审批状态" width="100">
					<template #default="{row}">
						{{row.auditstatus===0?'已提交':row.auditstatus===1?'同意':'拒绝'}}
					</template>
				</el-table-column>
				<el-table-column align="center" prop="bedId" label="床位" width="100" />
                <el-table-column align="center" label="操作" width="100">
                  <template #default="scope">
                    <!-- <el-button v-if="scope.row.auditstatus === 1 && dialog.item.roleId === 2" :disabled="scope.row.actualreturntime" type="success" size="small" round plain @click="updateTime(scope.row.id)">登记回院时间</el-button> -->
					<el-button  v-if="scope.row.auditstatus === 0 && roleId === 2" type="danger" size="small" round plain @click="del(scope.row.id)">撤销申请</el-button>
					<el-button  v-if="scope.row.auditstatus === 0 && roleId !== 2" type="primary" size="small" round plain @click="examine(scope.row.id)">审批</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <!-- 分页插件 -->
              <div style="margin-top:15px" v-if="backdownList.length>0">
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
	      <el-button type="primary" @click="examineBackdown('itemExamineForm')">保存</el-button>
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
	    <el-form-item label="客户姓名：" prop="customerId">
			<el-select v-model="dialog.item.customerId" style="width:320px" placeholder="客户姓名：">
			  <el-option v-for="item in customerList" :label="item.customerName" :value="item.id" />
			</el-select>
	    </el-form-item>
		<el-form-item label="退住时间：" prop="retreattime">
			<el-date-picker
					style="width:320px" 
			        v-model="dialog.item.retreattime"
			        type="date"
			        placeholder="退住时间:"
					value-format="YYYY-MM-DD"
			      />
		</el-form-item>
	   <el-form-item label="退住类型：" prop="retreattype">
		  <el-select v-model="dialog.item.retreattype" style="width:320px" placeholder="退住类型：">
		    <el-option label="正常退住" value="0" />
			<el-option label="死亡退住" value="1" />
		    <el-option label="保留床位" value="2" />
		  </el-select>
	    </el-form-item>
		<el-form-item label="退住原因： " prop="retreatreason">
		  <el-input v-model="dialog.item.retreatreason" />
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
<!-- 
<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { selectCustomer } from '../../api/customerApi';
import { listBackdownRecord, addBackdownRecord, deleteBackdownRecord, auditBackdownRecord } from '../../api/backdownApi';

// 查询条件
const condition = ref({
    customerName: '',
    currentPage: 1,
    pageSize: 6
})

const conditionRecord = ref({
    currentPage: 1,
    pageSize: 6
})

// 客户信息分页
const page = ref({
    currentPage: 1,
    pageSize: 6,
    total: 0
})

// 退住记录分页
const pageRecord = ref({
    currentPage: 1,
    pageSize: 6,
    total: 0
})

// 客户信息列表
const khxxList = ref([])
// 退住记录列表
const backdownList = ref([])
// 客户下拉列表
const customerList = ref([])

// 角色ID（1:管理员 2:普通用户） - 需要根据实际登录信息设置
const roleId = ref(2) // 假设默认是普通用户

// 当前选中的客户
const currentCustomer = ref(null)

// 对话框状态
const dialog = ref({
    dialogVisible: false,
    dialogExamineVisible: false,
    tops: '',
    item: {
        id: null,
        customerId: '',
        retreattime: '',
        retreattype: '',
        retreatreason: '',
        auditstatus: '1', // 默认为同意
        auditopinion: ''
    }
})

// 表单引用
const itemForm = ref(null)
const itemExamineForm = ref(null)

// 表单验证规则
const rules = {
    customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
    retreattime: [{ required: true, message: '请选择退住时间', trigger: 'change' }],
    retreattype: [{ required: true, message: '请选择退住类型', trigger: 'change' }],
    retreatreason: [{ required: true, message: '请输入退住原因', trigger: 'blur' }],
    auditstatus: [{ required: true, message: '请选择审批状态', trigger: 'change' }],
    auditopinion: [{ 
        required: true, 
        message: '请输入审批意见', 
        trigger: 'blur',
        validator: (rule, value, callback) => {
            if (dialog.value.item.auditstatus === '2' && !value) {
                callback(new Error('拒绝时必须填写审批意见'))
            } else {
                callback()
            }
        }
    }]
}

// 查询客户信息
const query = async () => {
    try {
        const params = {
            customerName: condition.value.customerName,
            currentPage: condition.value.currentPage,
            pageSize: condition.value.pageSize
        }
        
        const res = await selectCustomer(params)
        if (res.flag && res.data) {
            khxxList.value = res.data.records || []
            page.value.total = res.data.total || 0
            
            // 更新客户下拉列表
            customerList.value = khxxList.value.map(item => ({ 
                id: item.id, 
                customerName: item.customerName
            }))
        } else {
            ElMessage.error(res.message || '查询客户信息失败')
        }
    } catch (error) {
        console.error('查询客户信息失败', error)
        ElMessage.error('查询客户信息失败')
    }
}

// 客户信息分页切换
const handleCurrentChange = (val) => {
    condition.value.currentPage = val
    query()
}

// 退住记录分页切换
const handleRecordChange = (val) => {
    conditionRecord.value.currentPage = val
    if (currentCustomer.value && currentCustomer.value.id) {
        loadBackdownRecords(currentCustomer.value.id)
    }
}

// 加载退住记录
// const loadBackdownRecords = async (customerId) => {
//     try {
//         const params = {
//             currentPage: conditionRecord.value.currentPage,
//             pageSize: conditionRecord.value.pageSize,
//             customerId: customerId
//         }
        
//         const res = await listBackdownRecord(params)
//         if (res.flag && res.data) {
//             backdownList.value = res.data.records || []
//             pageRecord.value.total = res.data.total || 0
            
//             // 确保数据完整性：添加缺失字段
//             backdownList.value = backdownList.value.map(record => ({
//                 ...record,
//                 customerName: record.customerName || currentCustomer.value?.customerName || '',
//                 bedNo: record.bedNo || currentCustomer.value?.bedNo || '',
//                 retreattype: record.retreattype || '',
//                 retreatreason: record.retreatreason || '',
//                 audittime: record.audittime || ''
//             }))
//         } else {
//             ElMessage.error(res.message || '查询退住记录失败')
//         }
//     } catch (error) {
//         console.error('查询退住记录失败', error)
//         ElMessage.error('查询退住记录失败')
//     }
// }
// 加载退住记录（移除 async）
const loadBackdownRecords = (customerId) => {
    if (!customerId) return;
    
    const params = {
        currentPage: conditionRecord.value.currentPage,
        pageSize: conditionRecord.value.pageSize,
        customerId: customerId
    };
    
    listBackdownRecord(params)
        .then(res => {
            if (res.flag && res.data) {
                backdownList.value = res.data.records || [];
                pageRecord.value.total = res.data.total || 0;
                
                // 确保数据完整性（保持原有逻辑）
                backdownList.value = backdownList.value.map(record => ({
                    ...record,
                    customerName: record.customerName || currentCustomer.value?.customerName || '',
                    bedNo: record.bedNo || currentCustomer.value?.bedNo || '',
                    retreattype: record.retreattype || '',
                    retreatreason: record.retreatreason || '',
                    audittime: record.audittime || ''
                }));
            } else {
                ElMessage.error(res.message || '查询退住记录失败');
            }
        })
        .catch(error => {
            console.error('查询退住记录失败', error);
            ElMessage.error('查询退住记录失败');
        });
}

// 客户表格序号计算
const indexMethod = (index) => {
    return (page.value.currentPage - 1) * page.value.pageSize + index + 1
}

// 退住记录表格序号计算
const indexMethodRecord = (index) => {
    return (pageRecord.value.currentPage - 1) * pageRecord.value.pageSize + index + 1
}

// 选择客户并查询其退住记录
const handleChangeCustomer = (row) => {
    if (row && row.id) {
        currentCustomer.value = row
        conditionRecord.value.currentPage = 1
        loadBackdownRecords(row.id)
    }
}

// 添加退住申请
const addItem = () => {
    dialog.value.tops = '添加退住申请'
    dialog.value.dialogVisible = true
    dialog.value.item = {
        id: null,
        customerId: '',
        retreattime: '',
        retreattype: '',
        retreatreason: '',
        auditstatus: '1',
        auditopinion: ''
    }
}

// 保存退住申请
// const save = () => {
//   // 表单验证
//   itemForm.value.validate()
//     .then(() => {
//       // 添加当前客户ID（如果从下拉选择）
//       if (!dialog.value.item.customerId && currentCustomer.value) {
//         dialog.value.item.customerId = currentCustomer.value.id
//       }
      
//       // 调用API保存退住申请
//       return addBackdownRecord(dialog.value.item)
//     })
//     .then(res => {
//       if (res.flag) {
//         ElMessage.success('退住申请保存成功')
//         dialog.value.dialogVisible = false
//         // 重新加载退住记录
//         if (currentCustomer.value?.id) {
//           loadBackdownRecords(currentCustomer.value.id)
//         }
//       } else {
//         ElMessage.error(res.message || '保存失败')
//       }
//       return res // 传递结果给后续处理
//     })
//     .catch(error => {
//       // 单独处理验证错误（Element UI已处理界面提示）
//       if (error?.name === 'ValidateError' || error?.fields) {
//         return // 不显示额外错误
//       }
      
//       // 其他错误处理
//       console.error('保存退住申请失败', error)
//       ElMessage.error(typeof error === 'string' ? error : '保存失败')
//     })
// }// 保存退住申请（移除 async）
const save = () => {
    itemForm.value.validate((valid) => {
        if (!valid) return;
        
        // 添加当前客户ID（如果从下拉选择）
        if (!dialog.value.item.customerId && currentCustomer.value) {
            dialog.value.item.customerId = currentCustomer.value.id;
        }
        
        addBackdownRecord(dialog.value.item)
            .then(res => {
                if (res.flag) {
                    ElMessage.success('退住申请保存成功');
                    dialog.value.dialogVisible = false;
                    if (currentCustomer.value?.id) {
                        loadBackdownRecords(currentCustomer.value.id);
                    }
                } else {
                    ElMessage.error(res.message || '保存失败');
                }
            })
            .catch(error => {
                console.error('保存退住申请失败', error);
                ElMessage.error('保存失败');
            });
    });
}

// 撤销申请
// const del = (id) => {
//   ElMessageBox.confirm(
//     '确定要撤销此退住申请吗? 此操作不可恢复',
//     '提示',
//     {
//       confirmButtonText: '确定',
//       cancelButtonText: '取消',
//       type: 'warning',
//     }
//   )
//   .then(() => deleteBackdownRecord(id))
//   .then(res => {
//     if (res.flag) {
//       ElMessage.success('退住申请已撤销')
//       if (currentCustomer.value?.id) {
//         loadBackdownRecords(currentCustomer.value.id)
//       }
//     } else {
//       ElMessage.error(res.message || '撤销失败')
//     }
//   })
//   .catch(error => {
//     // 用户取消操作不报错
//     if (error === 'cancel' || error?.includes('取消')) return
//     console.error('撤销退住申请失败', error)
//     ElMessage.error('撤销失败')
//   })
// }
// 撤销申请（移除 async）
const del = (id) => {
    ElMessageBox.confirm(
        '确定要撤销此退住申请吗? 此操作不可恢复',
        '提示',
        {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning',
        }
    )
    .then(() => deleteBackdownRecord(id))
    .then(res => {
        if (res.flag) {
            ElMessage.success('退住申请已撤销');
            if (currentCustomer.value?.id) {
                loadBackdownRecords(currentCustomer.value.id);
            }
        } else {
            ElMessage.error(res.message || '撤销失败');
        }
    })
    .catch(error => {
        if (error !== 'cancel') {
            console.error('撤销退住申请失败', error);
            ElMessage.error('撤销失败');
        }
    });
}

// 审批保存
// const examineBackdown = () => {
//   itemExamineForm.value.validate()
//     .then(() => {
//       // 构造审批参数
//       const params = {
//         id: dialog.value.item.id,
//         auditstatus: dialog.value.item.auditstatus,
//         auditopinion: dialog.value.item.auditstatus === '2' 
//           ? dialog.value.item.auditopinion 
//           : ''
//       }
      
//       // 调用API审批
//       return auditBackdownRecord(params)
//     })
//     .then(res => {
//       if (res.flag) {
//         ElMessage.success('审批完成')
//         dialog.value.dialogExamineVisible = false
//         if (currentCustomer.value?.id) {
//           loadBackdownRecords(currentCustomer.value.id)
//         }
//       } else {
//         ElMessage.error(res.message || '审批失败')
//       }
//     })
//     .catch(error => {
//       // 验证错误不额外处理
//       if (error?.name === 'ValidateError' || error?.fields) return
//       console.error('审批退住申请失败', error)
//       ElMessage.error('审批失败')
//     })
// }
// 审批保存（移除 async）
const examineBackdown = () => {
    itemExamineForm.value.validate((valid) => {
        if (!valid) return;
        
        const auditData = {
            id: dialog.value.item.id,
            auditstatus: dialog.value.item.auditstatus,
            auditopinion: dialog.value.item.auditstatus === '2' 
                ? dialog.value.item.auditopinion 
                : ''
        };
        
        auditBackdownRecord(auditData)
            .then(res => {
                if (res.flag) {
                    ElMessage.success('审批完成');
                    dialog.value.dialogExamineVisible = false;
                    if (currentCustomer.value?.id) {
                        loadBackdownRecords(currentCustomer.value.id);
                    }
                } else {
                    ElMessage.error(res.message || '审批失败');
                }
            })
            .catch(error => {
                console.error('审批退住申请失败', error);
                ElMessage.error('审批失败');
            });
    });
}

// 审批退住申请（非异步方法保持不变）
const examine = (id) => {
  const record = backdownList.value.find(item => item.id === id)
  if (!record) return

  dialog.value.tops = '审批退住申请'
  dialog.value.dialogExamineVisible = true
  dialog.value.item = { 
    id: record.id,
    auditstatus: String(record.auditstatus || '1'),
    auditopinion: record.auditopinion || ''
  }
}

// 关闭添加/编辑对话框
const handleClose = () => {
    dialog.value.dialogVisible = false
}

// 关闭审批对话框
const handleExamineClose = () => {
    dialog.value.dialogExamineVisible = false
}

// 取消添加/编辑
const cancel = () => {
    dialog.value.dialogVisible = false
}

// 取消审批
const cancelExamine = () => {
    dialog.value.dialogExamineVisible = false
}

// 组件挂载时加载数据
onMounted(() => {
    query()
})

{/* <script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { selectCustomer } from '../../api/customerApi';

// 查询条件
const condition = ref({
    customerName: '',
    currentPage: 1,
    pageSize: 6
})

const conditionRecord = ref({
    currentPage: 1,
    pageSize: 6
})

// 客户信息分页
const page = ref({
    currentPage: 1,
    pageSize: 6,
    total: 0
})

// 退住记录分页
const pageRecord = ref({
    currentPage: 1,
    pageSize: 6,
    total: 0
})

// 客户信息列表
const khxxList = ref([])
// 退住记录列表
const backdownList = ref([])
// 客户下拉列表
const customerList = ref([])

// 角色ID（1:管理员 2:普通用户） - 需要根据实际登录信息设置
const roleId = ref(2) // 假设默认是普通用户

// 当前选中的客户
const currentCustomer = ref(null)

// 对话框状态
const dialog = ref({
    dialogVisible: false,
    dialogExamineVisible: false,
    tops: '',
    item: {
        id: null,
        customerId: '',
        retreattime: '',
        retreattype: '',
        retreatreason: '',
        auditstatus: '1', // 默认为同意
        auditopinion: ''
    }
})

// 表单引用
const itemForm = ref(null)
const itemExamineForm = ref(null)

// 表单验证规则
const rules = {
    customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
    retreattime: [{ required: true, message: '请选择退住时间', trigger: 'change' }],
    retreattype: [{ required: true, message: '请选择退住类型', trigger: 'change' }],
    retreatreason: [{ required: true, message: '请输入退住原因', trigger: 'blur' }],
    auditstatus: [{ required: true, message: '请选择审批状态', trigger: 'change' }],
    auditopinion: [{ 
        required: true, 
        message: '请输入审批意见', 
        trigger: 'blur',
        validator: (rule, value, callback) => {
            if (dialog.value.item.auditstatus === '2' && !value) {
                callback(new Error('拒绝时必须填写审批意见'))
            } else {
                callback()
            }
        }
    }]
}

// 查询客户信息
const query = async () => {
    try {
        const params = {
            customerName: condition.value.customerName,
            currentPage: condition.value.currentPage,
            pageSize: condition.value.pageSize
        }
        
        const res = await selectCustomer(params)
        if (res.flag && res.data) {
            khxxList.value = res.data.records || []
            page.value.total = res.data.total || 0
            
            // 更新客户下拉列表
            customerList.value = khxxList.value.map(item => ({ 
                id: item.id, 
                customerName: item.customerName
            }))
        } else {
            ElMessage.error(res.message || '查询客户信息失败')
        }
    } catch (error) {
        console.error('查询客户信息失败', error)
        ElMessage.error('查询客户信息失败')
    }
}

// 客户信息分页切换
const handleCurrentChange = (val) => {
    condition.value.currentPage = val
    query()
}

// 退住记录分页切换
const handleRecordChange = (val) => {
    conditionRecord.value.currentPage = val
    if (currentCustomer.value && currentCustomer.value.id) {
        loadBackdownRecords(currentCustomer.value.id)
    }
}

// 加载退住记录
const loadBackdownRecords = async (customerId) => {
    try {
        const params = {
            currentPage: conditionRecord.value.currentPage,
            pageSize: conditionRecord.value.pageSize,
            customerId: customerId
        }
        
        // 调用后端接口获取退住记录
        const res = await http.post(`/backdown/listBackdownVo`, params)
        if (res.flag && res.data) {
            backdownList.value = res.data.records || []
            pageRecord.value.total = res.data.total || 0
            
            // 确保数据完整性：添加缺失字段
            backdownList.value = backdownList.value.map(record => ({
                ...record,
                customerName: record.customerName || currentCustomer.value?.customerName || '',
                bedNo: record.bedNo || currentCustomer.value?.bedNo || '',
                retreattype: record.retreattype || '',
                retreatreason: record.retreatreason || '',
                audittime: record.audittime || ''
            }))
        } else {
            ElMessage.error(res.message || '查询退住记录失败')
        }
    } catch (error) {
        console.error('查询退住记录失败', error)
        ElMessage.error('查询退住记录失败')
    }
}

// 客户表格序号计算
const indexMethod = (index) => {
    return (page.value.currentPage - 1) * page.value.pageSize + index + 1
}

// 退住记录表格序号计算
const indexMethodRecord = (index) => {
    return (pageRecord.value.currentPage - 1) * pageRecord.value.pageSize + index + 1
}

// 选择客户并查询其退住记录
const handleChangeCustomer = (row) => {
    if (row && row.id) {
        currentCustomer.value = row
        conditionRecord.value.currentPage = 1
        loadBackdownRecords(row.id)
    }
}

// 添加退住申请
const addItem = () => {
    dialog.value.tops = '添加退住申请'
    dialog.value.dialogVisible = true
    dialog.value.item = {
        id: null,
        customerId: '',
        retreattime: '',
        retreattype: '',
        retreatreason: '',
        auditstatus: '1',
        auditopinion: ''
    }
}

// 保存退住申请
const save = async () => {
    try {
        // 表单验证
        await itemForm.value.validate()
        
        // 添加当前客户ID（如果从下拉选择）
        if (!dialog.value.item.customerId && currentCustomer.value) {
            dialog.value.item.customerId = currentCustomer.value.id
        }
        
        // 调用API保存退住申请
        const res = await http.post(`/backdown/addBackdown`, dialog.value.item)
        
        if (res.flag) {
            ElMessage.success('退住申请保存成功')
            dialog.value.dialogVisible = false
            // 重新加载退住记录
            if (currentCustomer.value && currentCustomer.value.id) {
                loadBackdownRecords(currentCustomer.value.id)
            }
        } else {
            ElMessage.error(res.message || '保存失败')
        }
    } catch (error) {
        // 验证失败不显示错误消息（Element UI会处理）
        if (error !== 'validate') {
            console.error('保存退住申请失败', error)
            ElMessage.error('保存失败')
        }
    }
}

// 撤销申请
const del = async (id) => {
    try {
        await ElMessageBox.confirm(
            '确定要撤销此退住申请吗? 此操作不可恢复',
            '提示',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
            }
        )
        
        // 调用后端接口删除退住申请
        const res = await http.get(`/backdown/delBackdown?id=${id}`)
        
        if (res.flag) {
            ElMessage.success('退住申请已撤销')
            // 重新加载退住记录
            if (currentCustomer.value && currentCustomer.value.id) {
                loadBackdownRecords(currentCustomer.value.id)
            }
        } else {
            ElMessage.error(res.message || '撤销失败')
        }
    } catch (error) {
        // 用户取消操作不显示错误
        if (error !== 'cancel') {
            console.error('撤销退住申请失败', error)
            ElMessage.error('撤销失败')
        }
    }
}

// 审批退住申请
const examine = (id) => {
    // 找到要审批的记录
    const record = backdownList.value.find(item => item.id === id)
    if (record) {
        dialog.value.tops = '审批退住申请'
        dialog.value.dialogExamineVisible = true
        dialog.value.item = { 
            id: record.id,
            auditstatus: String(record.auditstatus || '1'), // 确保是字符串
            auditopinion: record.auditopinion || ''
        }
    }
}

// 审批保存
const examineBackdown = async () => {
    try {
        // 表单验证
        await itemExamineForm.value.validate()
        
        // 准备审批数据
        const auditData = {
            id: dialog.value.item.id,
            auditstatus: dialog.value.item.auditstatus,
            auditopinion: dialog.value.item.auditstatus === '2' 
                ? dialog.value.item.auditopinion 
                : ''
        }
        
        // 调用API审批退住申请
        const res = await http.post(`/backdown/examineBackdown`, auditData)
        
        if (res.flag) {
            ElMessage.success('审批完成')
            dialog.value.dialogExamineVisible = false
            // 重新加载退住记录
            if (currentCustomer.value && currentCustomer.value.id) {
                loadBackdownRecords(currentCustomer.value.id)
            }
        } else {
            ElMessage.error(res.message || '审批失败')
        }
    } catch (error) {
        // 验证失败不显示错误消息
        if (error !== 'validate') {
            console.error('审批退住申请失败', error)
            ElMessage.error('审批失败')
        }
    }
}

// 关闭添加/编辑对话框
const handleClose = () => {
    dialog.value.dialogVisible = false
}

// 关闭审批对话框
const handleExamineClose = () => {
    dialog.value.dialogExamineVisible = false
}

// 取消添加/编辑
const cancel = () => {
    dialog.value.dialogVisible = false
}

// 取消审批
const cancelExamine = () => {
    dialog.value.dialogExamineVisible = false
} */}

// 组件挂载时加载数据
// onMounted(() => {
//     query()
// })
</script> -->

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { selectCustomer } from '../../api/customerApi';
import { listBackdownRecord, addBackdownRecord, deleteBackdownRecord, auditBackdownRecord } from '../../api/backdownApi';

// 查询条件
const condition = ref({
    customerName: '',
    currentPage: 1,
    pageSize: 6
})

// 退住记录分页
const pageRecord = ref({
    currentPage: 1,
    pageSize: 6,
    total: 0
})

// 客户信息分页
const page = ref({
    currentPage: 1,
    pageSize: 6,
    total: 0
})

// 客户信息列表
const khxxList = ref([])
// 退住记录列表
const backdownList = ref([])
// 客户下拉列表
const customerList = ref([])

// 角色ID（1:管理员 2:普通用户）
const roleId = ref(2)

// 当前选中的客户
const currentCustomer = ref(null)

// 对话框状态
const dialog = ref({
    dialogVisible: false,
    dialogExamineVisible: false,
    tops: '',
    item: {
        id: null,
        customerId: '',
        retreattime: '',
        retreattype: '',
        retreatreason: '',
        auditstatus: '1',
        auditopinion: ''
    }
})

// 表单引用
const itemForm = ref(null)
const itemExamineForm = ref(null)

// 表单验证规则
const rules = {
    customerId: [{ required: true, message: '请选择客户', trigger: 'change' }],
    retreattime: [{ required: true, message: '请选择退住时间', trigger: 'change' }],
    retreattype: [{ required: true, message: '请选择退住类型', trigger: 'change' }],
    retreatreason: [{ required: true, message: '请输入退住原因', trigger: 'blur' }],
    auditstatus: [{ required: true, message: '请选择审批状态', trigger: 'change' }],
    auditopinion: [{ 
        required: true, 
        message: '请输入审批意见', 
        trigger: 'blur',
        validator: (rule, value, callback) => {
            if (dialog.value.item.auditstatus === '2' && !value) {
                callback(new Error('拒绝时必须填写审批意见'))
            } else {
                callback()
            }
        }
    }]
}

// 查询客户信息
const query = () => {
    const params = {
        customerName: condition.value.customerName,
        currentPage: page.value.currentPage,
        pageSize: page.value.pageSize
    }
    
    selectCustomer(params)
        .then(res => {
            if (res.flag && res.data) {
                khxxList.value = res.data.records || []
                page.value.total = res.data.total || 0
                
                // 更新客户下拉列表
                customerList.value = khxxList.value.map(item => ({ 
                    id: item.id, 
                    customerName: item.customerName
                }))
            } else {
                ElMessage.error(res.message || '查询客户信息失败')
            }
        })
        .catch(error => {
            console.error('查询客户信息失败', error)
            ElMessage.error('查询客户信息失败')
        })
}

// 客户信息分页切换
const handleCurrentChange = (val) => {
    page.value.currentPage = val
    query()
}

// 退住记录分页切换
const handleRecordChange = (val) => {
    pageRecord.value.currentPage = val
    if (currentCustomer.value && currentCustomer.value.id) {
        loadBackdownRecords(currentCustomer.value.id)
    }
}

// 加载退住记录
const loadBackdownRecords = (customerId) => {
    if (!customerId) return
    
    const params = {
        currentPage: pageRecord.value.currentPage,
        pageSize: pageRecord.value.pageSize,
        customerId: customerId
    }
    
    listBackdownRecord(params)
        .then(res => {
            if (res.flag && res.data) {
                backdownList.value = res.data.records || []
                pageRecord.value.total = res.data.total || 0
                
                // 确保数据完整性
                backdownList.value = backdownList.value.map(record => ({
                    ...record,
                    customerName: record.customerName || currentCustomer.value?.customerName || '',
                    bedNo: record.bedNo || currentCustomer.value?.bedNo || '',
                    retreattype: record.retreattype || '',
                    retreatreason: record.retreatreason || '',
                    audittime: record.audittime || ''
                }))
            } else {
                ElMessage.error(res.message || '查询退住记录失败')
            }
        })
        .catch(error => {
            console.error('查询退住记录失败', error)
            ElMessage.error('查询退住记录失败')
        })
}


// 客户表格序号计算
const indexMethod = (index) => {
    return (page.value.currentPage - 1) * page.value.pageSize + index + 1
}

// 退住记录表格序号计算
const indexMethodRecord = (index) => {
    return (pageRecord.value.currentPage - 1) * pageRecord.value.pageSize + index + 1
}

// 选择客户并查询其退住记录
const handleChangeCustomer = (row) => {
    if (row && row.id) {
        currentCustomer.value = row
        pageRecord.value.currentPage = 1
        loadBackdownRecords(row.id)
    }
}

// 添加退住申请
const addItem = () => {
    dialog.value.tops = '添加退住申请'
    dialog.value.dialogVisible = true
    dialog.value.item = {
        id: null,
        customerId: '',
        retreattime: '',
        retreattype: '',
        retreatreason: '',
        auditstatus: '1',
        auditopinion: ''
    }
}

// 保存退住申请

const save = () => {
  itemForm.value.validate((valid) => {
    if (!valid) return;
    
    if (!dialog.value.item.customerId && currentCustomer.value) {
      dialog.value.item.customerId = currentCustomer.value.id;
    }
    
    addBackdownRecord(dialog.value.item)
      .then(res => {
        if (res.flag) {
          ElMessage.success('保存成功');
          dialog.value.dialogVisible = false;
          if (currentCustomer.value?.id) {
            pageRecord.value.currentPage = 1; // 重置到第1页
            loadBackdownRecords(currentCustomer.value.id);
          }
        } else {
          ElMessage.error(res.message || '保存失败');
        }
      })
      .catch(error => {
        console.error('保存失败:', error);
        ElMessage.error('保存失败');
      });
  });
};

// 撤销申请（添加分页重置）
const del = (id) => {
  ElMessageBox.confirm('确定撤销？', '提示', { type: 'warning' })
    .then(() => deleteBackdownRecord(id))
    .then(res => {
      if (res.flag) {
        ElMessage.success('撤销成功');
        if (currentCustomer.value?.id) {
          pageRecord.value.currentPage = 1; // 重置到第1页
          loadBackdownRecords(currentCustomer.value.id);
        }
      } else {
        ElMessage.error(res.message || '撤销失败');
      }
    })
    .catch(error => {
      if (error !== 'cancel') ElMessage.error('撤销失败');
    });
};

// 审批保存（添加分页重置）
const examineBackdown = () => {
  itemExamineForm.value.validate((valid) => {
    if (!valid) return;
    
    const auditData = {
      id: dialog.value.item.id,
      auditstatus: dialog.value.item.auditstatus,
      auditopinion: dialog.value.item.auditstatus === '2' ? dialog.value.item.auditopinion : ''
    };
    
    auditBackdownRecord(auditData)
      .then(res => {
        if (res.flag) {
          ElMessage.success('审批完成');
          dialog.value.dialogExamineVisible = false;
          if (currentCustomer.value?.id) {
            pageRecord.value.currentPage = 1; // 重置到第1页
            loadBackdownRecords(currentCustomer.value.id);
          }
        } else {
          ElMessage.error(res.message || '审批失败');
        }
      })
      .catch(error => {
        console.error('审批失败:', error);
        ElMessage.error('审批失败');
      });
  });
};

// 审批退住申请
const examine = (id) => {
    const record = backdownList.value.find(item => item.id === id)
    if (!record) return

    dialog.value.tops = '审批退住申请'
    dialog.value.dialogExamineVisible = true
    dialog.value.item = { 
        id: record.id,
        auditstatus: String(record.auditstatus || '1'),
        auditopinion: record.auditopinion || ''
    }
}
// 在 <script setup> 中添加方法定义
const djhandleClose = () => {
  // 处理对话框关闭逻辑
  dialogVisible.value = false;
};
// 关闭添加/编辑对话框
const handleClose = () => {
    dialog.value.dialogVisible = false
}

// 关闭审批对话框
const handleExamineClose = () => {
    dialog.value.dialogExamineVisible = false
}

// 取消添加/编辑
const cancel = () => {
    dialog.value.dialogVisible = false
}

// 取消审批
const cancelExamine = () => {
    dialog.value.dialogExamineVisible = false
}

// 组件挂载时加载数据
onMounted(() => {
    query()
})
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