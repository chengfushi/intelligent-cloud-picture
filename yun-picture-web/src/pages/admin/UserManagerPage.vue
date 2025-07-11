<template>
  <div id="userManagePage">
    <!-- 搜索表单 -->
    <a-form layout="inline" :model="searchParams" @finish="doSearch">
      <a-form-item label="账号">
        <a-input v-model:value="searchParams.userAccount" placeholder="输入账号" allow-clear />
      </a-form-item>
      <a-form-item label="用户名">
        <a-input v-model:value="searchParams.userName" placeholder="输入用户名" allow-clear />
      </a-form-item>
      <a-form-item label="用户角色">
        <a-select
          v-model:value="searchParams.userRole"
          placeholder="选择角色"
          style="width: 120px"
          allow-clear
        >
          <a-select-option value="admin">管理员</a-select-option>
          <a-select-option value="user">普通用户</a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button :icon="h(SearchOutlined)" type="primary" html-type="submit" >搜索</a-button>
        <a-button style="margin-left: 8px" @click="resetSearch">重置</a-button>
        <a-button type="dashed" style="margin-left: 8px" @click="showAddModal">添加用户</a-button>
      </a-form-item>
    </a-form>
    <!-- 添加用户模态框 -->
    <a-modal
      v-model:visible="addModalVisible"
      title="添加新用户"
      :confirm-loading="addLoading"
      ok-text="确认"
      cancel-text="取消"
      @ok="handleAdd"
      @cancel="handleAddCancel"
    >
      <a-form :model="addUserForm" layout="vertical" ref="addFormRef">
        <a-form-item
          label="账号"
          name="userAccount"
          :rules="[{ required: true, message: '请输入账号' }]"
        >
          <a-input v-model:value="addUserForm.userAccount" placeholder="请输入账号" />
        </a-form-item>
        <a-form-item
          label="用户名"
          name="userName"
          :rules="[{ required: true, message: '请输入用户名' }]"
        >
          <a-input v-model:value="addUserForm.userName" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item label="头像URL" name="userAvatar">
          <a-input v-model:value="addUserForm.userAvatar" placeholder="请输入头像URL" />
        </a-form-item>
        <a-form-item label="个人简介" name="userProfile">
          <a-textarea
            v-model:value="addUserForm.userProfile"
            placeholder="请输入个人简介"
            :auto-size="{ minRows: 3, maxRows: 5 }"
          />
        </a-form-item>
        <a-form-item
          label="用户角色"
          name="userRole"
          :rules="[{ required: true, message: '请选择用户角色' }]"
        >
          <a-select v-model:value="addUserForm.userRole" placeholder="请选择角色">
            <a-select-option value="admin">管理员</a-select-option>
            <a-select-option value="user">普通用户</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

    <div style="margin-bottom: 16px" />
    <!-- 表格 -->
    <a-table
      :columns="columns"
      :data-source="dataList"
      :pagination="pagination"
      :loading="loading"
      @change="doTableChange"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'userAvatar'">
          <a-image v-if="record.userAvatar" :src="record.userAvatar" :width="80" />
          <a-avatar v-else :size="80" style="background-color: #1890ff">
            {{ record.userName?.charAt(0) || 'U' }}
          </a-avatar>
        </template>
        <template v-else-if="column.dataIndex === 'userRole'">
          <a-tag :color="record.userRole === 'admin' ? 'green' : 'blue'">
            {{ record.userRole === 'admin' ? '管理员' : '普通用户' }}
          </a-tag>
        </template>
        <template v-else-if="column.dataIndex === 'createTime'">
          {{ dayjs(record.createTime).format('YYYY-MM-DD HH:mm:ss') }}
        </template>
        <template v-else-if="column.key === 'action'">
          <a-space>
            <a-button type="dashed" size="small" @click="showEditModal(record)">编辑</a-button>
            <a-button danger size="small" @click="showDeleteConfirm(record.id)">删除</a-button>
          </a-space>
        </template>
      </template>
    </a-table>

    <!-- 编辑用户模态框 -->
    <a-modal
      v-model:visible="editModalVisible"
      :title="editModalTitle"
      :confirm-loading="editLoading"
      ok-text="保存"
      cancel-text="取消"
      @ok="handleEdit"
      @cancel="handleCancel"
    >
      <a-form :model="updateUserForm" layout="vertical" ref="editFormRef">
        <a-form-item
          label="用户名"
          name="userName"
          :rules="[{ required: true, message: '请输入用户名' }]"
        >
          <a-input v-model:value="updateUserForm.userName" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item label="头像URL" name="userAvatar">
          <a-input v-model:value="updateUserForm.userAvatar" placeholder="请输入头像URL" />
        </a-form-item>
        <a-form-item label="个人简介" name="userProfile">
          <a-textarea
            v-model:value="updateUserForm.userProfile"
            placeholder="请输入个人简介"
            :auto-size="{ minRows: 3, maxRows: 5 }"
          />
        </a-form-item>
        <a-form-item
          label="用户角色"
          name="userRole"
          :rules="[{ required: true, message: '请选择用户角色' }]"
        >
          <a-select v-model:value="updateUserForm.userRole" placeholder="请选择角色">
            <a-select-option value="admin">管理员</a-select-option>
            <a-select-option value="user">普通用户</a-select-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from 'vue'
import {
  deleteUserUsingPost,
  getUserVoListPageUsingPost,
  updateUserUsingPost,
  addUserUsingPost,
} from '@/api/userController'
import { message, Modal } from 'ant-design-vue'
import dayjs from 'dayjs'
import type { FormInstance } from 'ant-design-vue'
import { h } from 'vue';
import { SearchOutlined } from '@ant-design/icons-vue';

// 在原有响应式变量后添加
const addModalVisible = ref(false)
const addLoading = ref(false)
const addFormRef = ref<FormInstance>()

const loading = ref(false)
const editLoading = ref(false)
const editModalVisible = ref(false)
const editModalTitle = ref('编辑用户')
const editFormRef = ref<FormInstance>()

const columns = [
  {
    title: 'ID',
    dataIndex: 'id',
    width: 200,
  },
  {
    title: '账号',
    dataIndex: 'userAccount',
    width: 200,
  },
  {
    title: '用户名',
    dataIndex: 'userName',
    width: 150,
  },
  {
    title: '头像',
    dataIndex: 'userAvatar',
    width: 120,
  },
  {
    title: '简介',
    dataIndex: 'userProfile',
    ellipsis: true,
  },
  {
    title: '用户角色',
    dataIndex: 'userRole',
    width: 120,
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 180,
  },
  {
    title: '操作',
    key: 'action',
    width: 150,
  },
]

const dataList = ref<API.UserVO[]>([])
const total = ref(0)

const searchParams = reactive<API.UserQueryRequest>({
  current: 1,
  pageSize: 10,
  sortField: 'createTime',
  sortOrder: 'descend',
})

const updateUserForm = reactive<API.UserUpdateRequest>({
  id: '',
  userName: '',
  userAvatar: '',
  userProfile: '',
  userRole: '',
})

const addUserForm = reactive<API.UserAddRequest>({
  userName: '',
  userAccount: '',
  userAvatar: '',
  userProfile: '',
  userRole: '',
})

// 显示添加用户模态框
const showAddModal = () => {
  addModalVisible.value = true
}

// 处理添加用户
const handleAdd = async () => {
  try {
    // 验证表单
    await addFormRef.value?.validate()
    addLoading.value = true

    // 调用API
    const res = await addUserUsingPost(addUserForm)

    if (res.data.code === 0) {
      message.success('添加用户成功')
      addModalVisible.value = false
      // 重置表单
      addFormRef.value?.resetFields()
      // 刷新列表
      fetchData()
    } else {
      message.error('添加失败: ' + (res.data.message || '未知错误'))
    }
  } catch (error) {
    console.error('Add user error:', error)
    if (error instanceof Error) {
      message.error('添加失败: ' + error.message)
    }
  } finally {
    addLoading.value = false
  }
}

// 取消添加
const handleAddCancel = () => {
  addModalVisible.value = false
  addFormRef.value?.resetFields()
}

const fetchData = async () => {
  try {
    loading.value = true
    const res = await getUserVoListPageUsingPost({
      ...searchParams,
    })
    if (res.data.code === 0 && res.data.data) {
      dataList.value = res.data.data.records ?? []
      total.value = res.data.data.total ?? 0
    } else {
      message.error('获取数据失败: ' + (res.data.message || '未知错误'))
    }
  } catch (error) {
    message.error('网络错误，请稍后重试')
    console.error('Fetch data error:', error)
  } finally {
    loading.value = false
  }
}

const showEditModal = (record: API.UserVO) => {
  editModalTitle.value = `编辑用户 - ${record.userName}`
  Object.assign(updateUserForm, {
    id: record.id,
    userName: record.userName,
    userAvatar: record.userAvatar,
    userProfile: record.userProfile,
    userRole: record.userRole,
  })
  editModalVisible.value = true
}

const handleEdit = async () => {
  try {
    await editFormRef.value?.validate()
    editLoading.value = true
    const res = await updateUserUsingPost(updateUserForm)
    if (res.data.code === 0) {
      message.success('更新成功')
      editModalVisible.value = false
      fetchData()
    } else {
      message.error('更新失败: ' + (res.data.message || '未知错误'))
    }
  } catch (error) {
    console.error('Update error:', error)
  } finally {
    editLoading.value = false
  }
}

const handleCancel = () => {
  editModalVisible.value = false
  editFormRef.value?.resetFields()
}

const showDeleteConfirm = (id: string) => {
  Modal.confirm({
    title: '确认删除',
    content: '确定要删除该用户吗？此操作不可撤销！',
    okText: '确认',
    okType: 'danger',
    cancelText: '取消',
    onOk: () => doDelete(id),
  })
}

const doDelete = async (id: string) => {
  try {
    loading.value = true
    const res = await deleteUserUsingPost({ id })
    if (res.data.code === 0) {
      message.success('删除成功')
      fetchData()
    } else {
      message.error('删除失败: ' + (res.data.message || '未知错误'))
    }
  } catch (error) {
    message.error('网络错误，请稍后重试')
    console.error('Delete error:', error)
  } finally {
    loading.value = false
  }
}

const doSearch = () => {
  searchParams.current = 1
  fetchData()
}

const resetSearch = () => {
  Object.assign(searchParams, {
    userAccount: undefined,
    userName: undefined,
    userRole: undefined,
    current: 1,
  })
  fetchData()
}

const doTableChange = (page: any) => {
  searchParams.current = page.current
  searchParams.pageSize = page.pageSize
  fetchData()
}

const pagination = computed(() => ({
  current: searchParams.current,
  pageSize: searchParams.pageSize,
  total: total.value,
  showSizeChanger: true,
  showQuickJumper: true,
  showTotal: (total) => `共 ${total} 条`,
}))

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
#userManagePage {
  padding: 24px;
}

.ant-form-item {
  margin-bottom: 16px;
}
</style>
