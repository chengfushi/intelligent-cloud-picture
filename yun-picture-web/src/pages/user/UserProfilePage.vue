<template>
  <div class="userProfilePage">
    <a-card :bordered="false" class="profile-card">
      <div class="profile-header">
        <div
          class="custom-avatar"
          :style="{ width: '120px', height: '120px', borderRadius: '50%' }"
        >
          <template v-if="userData.userAvatar">
            <a-image :src="userData.userAvatar" :width="120" style="border-radius: 50%" />
          </template>
          <template v-else>
            <div
              style="
                width: 100%;
                height: 100%;
                background-color: #1890ff;
                border-radius: 50%;
                display: flex;
                justify-content: center;
                align-items: center;
                color: white;
                font-size: 36px;
              "
            >
              {{ userData.userName?.charAt(0) || 'U' }}
            </div>
          </template>
        </div>
        <div class="profile-info">
          <h2>{{ userData.userName }}</h2>
          <p class="account-info">@{{ userData.userAccount }}</p>
          <a-tag :color="userData.userRole === 'admin' ? 'red' : 'blue'">
            {{ userData.userRole === 'admin' ? '管理员' : '普通用户' }}
          </a-tag>
        </div>
      </div>

      <a-divider />

      <div class="profile-details">
        <a-descriptions :column="1" bordered>
          <a-descriptions-item label="账号ID">{{ userData.id }}</a-descriptions-item>
          <a-descriptions-item label="个人简介">
            {{ userData.userProfile || '暂无简介' }}
          </a-descriptions-item>
          <a-descriptions-item label="创建时间">
            {{ formatDate(userData.createTime) }}
          </a-descriptions-item>
          <a-descriptions-item label="最后更新时间">
            {{ formatDate(userData.updateTime) }}
          </a-descriptions-item>
        </a-descriptions>
      </div>

      <div class="profile-actions">
        <a-button type="primary" @click="showEditModal">编辑资料</a-button>
        <a-button v-if="userData.userRole === 'admin'" style="margin-left: 10px" @click="goManage">
          管理后台
        </a-button>
      </div>
    </a-card>
    <a-modal
      v-model:visible="editModalVisible"
      title="编辑资料"
      ok-text="保存"
      cancel-text="取消"
      @ok="handleEdit"
      @cancel="handleCancel"
    >
      <a-form :model="updateUserForm" layout="vertical">
        <a-form-item label="用户名">
          <a-input v-model:value="updateUserForm.userName" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item label="头像URL">
          <a-input v-model:value="updateUserForm.userAvatar" placeholder="请输入头像URL" />
        </a-form-item>
        <!--        上传头像-->
        <a-upload
          list-type="picture-card"
          :show-upload-list="false"
          :custom-request="handleUpload"
          :before-upload="beforeUpload"
        >
          <img v-if="picture?.url" :src="picture?.url" alt="avatar" />
          <div v-else>
            <loading-outlined v-if="loading"></loading-outlined>
            <plus-outlined v-else></plus-outlined>
            <div class="ant-upload-text">点击或拖拽上传图片</div>
          </div>
        </a-upload>

        <a-form-item label="个人简介">
          <a-textarea
            v-model:value="updateUserForm.userProfile"
            placeholder="请输入个人简介"
            :auto-size="{ minRows: 3, maxRows: 5 }"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { message, type UploadProps } from 'ant-design-vue'
import { getLoginUserUsingGet } from '@/api/userController'
import router from '@/router'
import { updateUserSelfUsingPost } from '@/api/userController'
import { LoadingOutlined, PlusOutlined } from '@ant-design/icons-vue'
import { uploadPictureUsingPost } from '@/api/pictureController'

const editModalVisible = ref(false)
const userData = ref({
  id: '',
  userAccount: '',
  userName: '',
  userAvatar: null,
  userProfile: null,
  userRole: '',
  editTime: '',
  createTime: '',
  updateTime: '',
})

const updateUserForm = reactive<API.UserUpdateRequest>({
  id: '',
  userName: '',
  userAvatar: '',
  userProfile: '',
})

const formatDate = (dateString: string) => {
  if (!dateString) return '未知时间'
  return new Date(dateString).toLocaleString()
}

const fetchUserProfile = async () => {
  try {
    // Assuming the API returns data in the format you provided
    const res = await getLoginUserUsingGet()
    if (res.data.code === 0 && res.data.data) {
      userData.value = res.data.data
    } else {
      message.error('获取用户信息失败: ' + (res.data.message || '未知错误'))
    }
  } catch (error) {
    message.error('网络错误，请稍后重试')
    console.error('Fetch profile error:', error)
  }
}

const tempPicture = ref<string | null>(null) // 存储上传成功的临时图片URL
const showEditModal = () => {
  // Fill the form with current user data
  updateUserForm.id = userData.value.id
  updateUserForm.userName = userData.value.userName || ''
  updateUserForm.userAvatar = tempPicture.value || userData.value.userAvatar || ''
  updateUserForm.userProfile = userData.value.userProfile || ''
  editModalVisible.value = true
}

const handleEdit = async () => {
  try {
    const res = await updateUserSelfUsingPost(updateUserForm)
    if (res.data.code === 0) {
      message.success('资料更新成功')
      editModalVisible.value = false
      fetchUserProfile() // Refresh user data
    } else {
      message.error('更新失败: ' + (res.data.message || '未知错误'))
    }
  } catch (error) {
    message.error('网络错误，请稍后重试')
    console.error('Update profile error:', error)
  }
}

const handleCancel = () => {
  editModalVisible.value = false
}

const goManage = () => {
  router.push('/admin/user')
}
interface Props {
  picture?: API.PictureVO
  spaceId?: number
  onSuccess?: (newPicture: API.PictureVO) => void
}

const props = defineProps<Props>()

/**
 * 上传图片
 * @param file
 */
const handleUpload = async ({ file }: any) => {
  loading.value = true
  try {
    const params: API.PictureUploadRequest = props.picture ? { id: props.picture.id } : {}
    params.spaceId = props.spaceId
    const res = await uploadPictureUsingPost(params, {}, file)
    if (res.data.code === 0 && res.data.data) {
      const { url } = res.data.data // 提取URL
      message.success('头像上传成功')

      // 1. 更新表单中的头像URL
      updateUserForm.userAvatar = url

      // 2. 更新临时图片预览（用于编辑模态框）
      tempPicture.value = url
    } else {
      message.error('头像上传失败，' + res.data.message)
    }
  } catch (error) {
    console.error('头像上传失败', error)
    message.error('头像上传失败，' + error.message)
  }
  loading.value = false
}

const loading = ref<boolean>(false)

/**
 * 上传前的校验
 * @param file
 */
const beforeUpload = (file: UploadProps['fileList'][number]) => {
  // 校验图片格式
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
  if (!isJpgOrPng) {
    message.error('不支持上传该格式的图片，推荐 jpg 或 png')
  }
  // 校验图片大小
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isLt2M) {
    message.error('不能上传超过 2M 的图片')
  }
  return isJpgOrPng && isLt2M
}

onMounted(() => {
  fetchUserProfile()
})
</script>

<style scoped>
.userProfilePage {
  padding: 24px;
  max-width: 800px;
  margin: 0 auto;
}

.profile-card {
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.profile-header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}

.user-avatar {
  margin-right: 24px;
  background-color: #1890ff;
  color: white;
  font-size: 36px;
}

.profile-info h2 {
  margin-bottom: 8px;
  font-size: 24px;
}

.account-info {
  color: #666;
  margin-bottom: 12px;
}

.profile-details {
  margin: 24px 0;
}

.profile-actions {
  margin-top: 24px;
  text-align: center;
}

@media (max-width: 768px) {
  .profile-header {
    flex-direction: column;
    text-align: center;
  }

  .user-avatar {
    margin-right: 0;
    margin-bottom: 16px;
  }
}
</style>
