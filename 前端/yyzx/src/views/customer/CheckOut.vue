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


<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import {
    selectCustomer
} from '../../api/customerApi';
import { listBackdownRecord, addBackdownRecord, deleteBackdownRecord, auditBackdownRecord } from '../../api/backdownApi';

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

// 退住记录分页
const pageRecord = ref({
    currentPage: 1,
    pageSize: 10,
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

// 对话框状态
const dialog = ref({
    dialogVisible: false,
    dialogExamineVisible: false,
    tops: '',
    item: {
        customerId: '',
        retreattime: '',
        retreattype: '',
        retreatreason: '',
        auditstatus: 1,
        auditopinion: ''
    }
})

// 表单引用
const itemForm = ref(null)
const itemExamineForm = ref(null)

// 表单验证规则
const rules = ref({
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
            if (dialog.value.item.auditstatus === 2 && !value) {
                callback(new Error('拒绝时必须填写审批意见'))
            } else {
                callback()
            }
        }
    }]
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
            
            // 同步更新客户下拉列表
            customerList.value = khxxList.value.map(item => ({ 
                id: item.id, 
                customerName: item.customerName
            }))
        } else {
            ElMessage.error(res.message || '查询客户信息失败')
        }
    }).catch(error => {
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
    loadBackdownRecords()
}

// 加载退住记录
const loadBackdownRecords = (customerId = null) => {
    const params = {
        currentPage: pageRecord.value.currentPage,
        pageSize: pageRecord.value.pageSize,
        userId: roleId.value, // 必须传递用户ID
        customerId: customerId // 可选：按客户ID筛选
    }
    console.log('加载退住记录，参数:', params);
    if (customerId) {
        params.customerId = customerId
    }
    
    // listBackdownRecord(params).then(res => {
    //       if (res.flag && res.data) {
    //     // 去重处理（按id字段）
    //     const uniqueRecords = res.data.records.reduce((arr, record) => {
    //         if (!arr.some(item => item.id === record.id)) {
    //             arr.push(record);
    //         }
    //         return arr;
    //     }, []);
        
    //     backdownList.value = uniqueRecords;
    //     pageRecord.value.total = uniqueRecords.length;
    // } else {
    //     if (res.flag && res.data) {
    //         backdownList.value = res.data.records || []
    //         pageRecord.value.total = res.data.total || 0
    //     } else {
    //         ElMessage.error(res.message || '查询退住记录失败')
    //     }
    // }).catch(error => {
    //     console.error('查询退住记录失败', error)
    //     ElMessage.error('查询退住记录失败')
    // });
  listBackdownRecord(params).then(res => {
    console.log('退住记录API响应数据:', res); // 打印完整响应数据用于调试
    
    if (res.flag && res.data) {
        // 1. 数据去重处理（按id字段）
        const uniqueRecords = res.data.records.reduce((arr, record) => {
            if (!arr.some(item => item.id === record.id)) {
                arr.push(record);
                return arr;
            }
            return arr;
        }, []);
        
        // 2. 赋值去重后的数据
        backdownList.value = uniqueRecords;
        pageRecord.value.total = uniqueRecords.length;
        
        // 3. 显示数据加载结果提示
        if (uniqueRecords.length > 0) {
            console.log(`成功加载 ${uniqueRecords.length} 条退住记录`);
        } else {
            ElMessage.info('暂无退住记录数据');
        }
    } else {
        // 处理API返回失败的情况
        const errorMessage = res.message || '查询退住记录失败，请检查网络或稍后再试';
        ElMessage.error(errorMessage);
        backdownList.value = []; // 清空数据避免显示旧数据
        pageRecord.value.total = 0;
    }
}).catch(error => {
    console.error('退住记录API请求异常:', error); // 打印异常详情
    
    // 处理网络异常或接口错误
    ElMessage.error('加载退住记录失败，请检查网络连接');
    backdownList.value = [];
    pageRecord.value.total = 0;
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
        pageRecord.value.currentPage = 1
        loadBackdownRecords(row.id)
    }
}

// 添加退住申请
const addItem = () => {
    dialog.value.tops = '添加退住申请'
    dialog.value.dialogVisible = true
    dialog.value.item = {
        customerId: '',
        retreattime: '',
        retreattype: '',
        retreatreason: '',
        auditstatus: 0,
        auditopinion: ''
    }
}

// 保存退住申请
const save = async () => {
    try {
        // 表单验证
        await itemForm.value.validate()
        
        // 调用API保存退住申请
        const res = await addBackdownRecord(dialog.value.item)
        
        if (res.flag) {
            ElMessage.success('退住申请保存成功')
            dialog.value.dialogVisible = false
            loadBackdownRecords()
        } else {
            ElMessage.error(res.message || '保存失败')
        }
    } catch (error) {
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
        
        const res = await deleteBackdownRecord(id)
        
        if (res.flag) {
            ElMessage.success('退住申请已撤销')
            loadBackdownRecords()
        } else {
            ElMessage.error(res.message || '撤销失败')
        }
    } catch (error) {
        if (error !== 'cancel') {
            console.error('撤销退住申请失败', error)
            ElMessage.error('撤销失败')
        }
    }
}

// 审批退住申请
const examine = (row) => {
    dialog.value.tops = '审批退住申请'
    dialog.value.dialogExamineVisible = true
    dialog.value.item = { 
        id: row.id,
        customerId: row.customerId,
        retreattime: row.retreattime,
        retreattype: row.retreattype,
        retreatreason: row.retreatreason,
        auditstatus: 1,
        auditopinion: ''
    }
}

// 审批保存
const examineBackdown = async () => {
    try {
        // 表单验证
        await itemExamineForm.value.validate()
        
        // 调用API审批退住申请
        const res = await auditBackdownRecord(dialog.value.item)
        
        if (res.flag) {
            ElMessage.success('审批完成')
            dialog.value.dialogExamineVisible = false
            loadBackdownRecords()
        } else {
            ElMessage.error(res.message || '审批失败')
        }
    } catch (error) {
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
}

// 组件挂载时加载数据
onMounted(() => {
    query()
    loadBackdownRecords();
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