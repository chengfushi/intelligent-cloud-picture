<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useLoginUserStore } from '@/stores/useLoginUserStore'
import { useRouter } from 'vue-router'
import { userRegisterUsingPost, userLoginUsingPost } from '@/api/userController'
import { message } from 'ant-design-vue'

const router = useRouter()
const loginUserStore = useLoginUserStore()
// Modified to require at least 3 out of 4 character types
const passwordPattern =
  /^((?=.*[a-z])(?=.*[A-Z])(?=.*\d)|(?=.*[a-z])(?=.*[A-Z])(?=.*[^A-Za-z0-9])|(?=.*[a-z])(?=.*\d)(?=.*[^A-Za-z0-9])|(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9])).*$/

const form1 = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
})

const handleLogin = async () => {
  try {
    const res = await userLoginUsingPost(form1)
    if (res.data.code === 0 && res.data.data) {
      await loginUserStore.fetchLoginUser()
      message.success('登录成功')
      router.push({
        path: '/',
        replace: true,
      })
    } else {
      message.error('登录失败: ' + (res.data.description || '未知错误'))
    }
  } catch (error) {
    message.error('网络错误，请稍后重试')
    console.error('Login error:', error)
  }
}

const form2 = reactive<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
})

const handleRegister = async () => {
  if (form2.checkPassword !== form2.userPassword) {
    message.error('二次输入的密码不一致')
    return
  }

  try {
    const res = await userRegisterUsingPost(form2)
    if (res.data.code === 0 && res.data.data) {
      message.success('注册成功')
      // Reset form after successful registration
      Object.assign(form2, {
        userAccount: '',
        userPassword: '',
        checkPassword: '',
      })
      disfiex.value = 0
    } else {
      message.error('注册失败: ' + (res.data.description || '未知错误'))
    }
  } catch (error) {
    message.error('网络错误，请稍后重试')
    console.error('Registration error:', error)
  }
}

const disfiex = ref(0)
const overlaylong = ref('overlaylong')
const overlaytitle = ref('overlaytitle')

const Signin = () => {
  overlaylong.value = 'overlaylongleft'
  overlaytitle.value = 'overlaytitleright'
  disfiex.value = 1
}

const Signup = () => {
  overlaylong.value = 'overlaylongright'
  overlaytitle.value = 'overlaytitleleft'
  disfiex.value = 0
}
</script>

<style scoped>
body,
html {
  margin: 0;
  padding: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
}
.center {
  height: 88vh;
  background-image: url('https://gd-hbimg.huaban.com/9165dc3c0f8279ae5402d1f47212847bff68b49f3cb547-QEli8R');
  background-size: 100% 100%;
  background-repeat: no-repeat;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

h1 {
  font-size: 30px;
  color: black;
}

.logon {
  background-color: #fff;
  border-radius: 10px;
  box-shadow:
    0 14px 28px rgba(0, 0, 0, 0.25),
    0 10px 10px rgba(0, 0, 0, 0.22);
  /* position: relative;
  overflow: hidden; */
  width: 768px;
  max-width: 100%;
  min-height: 480px;
  margin-top: 20px;
  display: flex;
  background: -webkit-linear-gradient(right, #4284db, #29eac4);
}

.overlaylong {
  border-radius: 10px 0 0 10px;
  width: 50%;
  height: 100%;
  background-color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlaylongleft {
  border-radius: 0px 10px 10px 0px;
  width: 50%;
  height: 100%;
  background-color: #fff;
  transform: translateX(100%);
  transition: transform 0.6s ease-in-out;
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlaylongright {
  border-radius: 10px 0 0 10px;
  width: 50%;
  height: 100%;
  background-color: #fff;
  transform: translateX(0%);
  transition: transform 0.6s ease-in-out;
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlaytitle {
  border-radius: 0px 10px 10px 0px;
  width: 50%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0);
  display: flex;
  align-items: center;
  justify-content: center;
}

.overlaytitleH2 {
  font-size: 30px;
  color: #fff;
  margin-top: 20px;
}

.overlaytitleP {
  font-size: 15px;
  color: #fff;
  margin-top: 20px;
}

.overlaytitleleft {
  border-radius: 0px 10px 10px 0px;
  width: 50%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0);
  display: flex;
  align-items: center;
  justify-content: center;
  transform: translateX(0%);
  transition: transform 0.6s ease-in-out;
}

.overlaytitleright {
  border-radius: 0px 10px 10px 0px;
  width: 50%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0);
  display: flex;
  align-items: center;
  justify-content: center;
  transform: translateX(-100%);
  transition: transform 0.6s ease-in-out;
}

.overlaytitle-Signin {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.overlaytitle-Signup {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.buttongohs {
  width: 180px;
  height: 40px;
  border-radius: 50px;
  border: 1px solid #fff;
  color: #fff;
  font-size: 15px;
  text-align: center;
  line-height: 40px;
  margin-top: 40px;
}

.overlaylongH2 {
  font-size: 25px;
  color: black;
  /* width: 250px; */
}

.overlaylong-Signin {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

.overlaylong-Signup {
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}

h3 {
  font-size: 10px;
  margin-top: 10px;
  cursor: pointer;
}
.inupbutton {
  background-color: #29eac4;
  border: none;
  width: 180px;
  height: 40px;
  border-radius: 50px;
  font-size: 15px;
  color: #fff;
  text-align: center;
  line-height: 30px;
  margin-top: 30px;
}
</style>

<template>
  <div class="center">
    <h1>Sign in/ Sign up</h1>
    <div class="logon">
      <div :class="overlaylong">
        <div class="overlaylong-Signin" v-if="disfiex == 0">
          <h2 class="overlaylongH2">Sign in</h2>
          <a-form
            style="max-width: 700px; margin: 0 auto"
            label-align="left"
            :label-col="{ span: 4 }"
            :wrapper-col="{ span: 20 }"
            :model="form1"
            @finish="handleLogin"
          >
            <a-form-item
              name="userAccount"
              label="账号"
              :rules="[
                { required: true, message: '请输入账号' },
                { min: 4, message: '账号不少于 4 位' },
                { max: 20, message: '账号不超过 20 位' },
              ]"
            >
              <a-input
                style="width: 250px"
                v-model:value="form1.userAccount"
                placeholder="请输入账号"
              />
            </a-form-item>
            <a-form-item
              name="userPassword"
              label="密码"
              :rules="[
                { required: true, message: '请输入密码' },
                { min: 8, message: '密码不少于 8 位' },
                { max: 20, message: '密码不超过 20 位' },
                {
                  pattern: passwordPattern,
                  message: '密码必须包含大小写字母、数字和特殊符号',
                },
              ]"
            >
              <a-input-password
                style="width: 250px"
                v-model:value="form1.userPassword"
                placeholder="请输入密码"
              />
              <h3 @click="message.info('请联系管理员')">Forgot your password?</h3>
            </a-form-item>
            <a-form-item :wrapper-col="{ offset: 4, span: 20 }">
              <a-button type="primary" html-type="submit" class="inupbutton">登录</a-button>
            </a-form-item>
          </a-form>
        </div>
        <div class="overlaylong-Signup" v-if="disfiex == 1">
          <h2 class="overlaylongH2">Registered Account</h2>
          <a-form
            style="max-width: 700px; margin: 0 auto"
            label-align="left"
            :label-col="{ span: 7 }"
            :wrapper-col="{ span: 20 }"
            :model="form2"
            @finish="handleRegister"
          >
            <a-form-item
              name="userAccount"
              label="账号"
              :rules="[
                { required: true, message: '请输入账号' },
                { min: 4, message: '账号不少于 4 位' },
                { max: 20, message: '账号不超过 20 位' },
              ]"
            >
              <a-input
                style="width: 200px"
                v-model:value="form2.userAccount"
                placeholder="请输入账号"
              />
            </a-form-item>
            <a-form-item
              name="userPassword"
              label="密码"
              :rules="[
                { required: true, message: '请输入密码' },
                { min: 8, message: '密码不少于 8 位' },
                { max: 20, message: '密码不超过 20 位' },
                {
                  pattern: passwordPattern,
                  message: '密码必须包含大小写字母、数字和特殊符号',
                },
              ]"
            >
              <a-input-password
                style="width: 200px"
                v-model:value="form2.userPassword"
                placeholder="请输入密码"
              />
            </a-form-item>
            <a-form-item
              name="checkPassword"
              label="确认密码"
              :rules="[
                { required: true, message: '请再次输入密码' },
                { min: 8, message: '密码不少于 8 位' },
                { max: 20, message: '密码不超过 20 位' },
                {
                  pattern: passwordPattern,
                  message: '密码必须包含大小写字母、数字和特殊符号',
                },
              ]"
            >
              <a-input-password
                style="width: 200px"
                v-model:value="form2.checkPassword"
                placeholder="请输入确认密码"
              />
            </a-form-item>
            <a-form-item :wrapper-col="{ offset: 4, span: 20 }">
              <a-button type="primary" html-type="submit" class="inupbutton">注册</a-button>
            </a-form-item>
          </a-form>
        </div>
      </div>
      <div :class="overlaytitle">
        <div class="overlaytitle-Signin" v-if="disfiex == 0">
          <h2 class="overlaytitleH2">Hello,Friend!</h2>
          <p class="overlaytitleP">Enter your personal details and start journey with us</p>
          <div class="buttongohs" @click="Signin">Sign up</div>
        </div>
        <div class="overlaytitle-Signup" v-if="disfiex == 1">
          <h2 class="overlaytitleH2">Welcome Back!</h2>
          <p class="overlaytitleP">
            To keep connected with us please login with your personal info
          </p>
          <div class="buttongohs" @click="Signup">Sign in</div>
        </div>
      </div>
    </div>
  </div>
</template>
