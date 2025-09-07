<template>
  <div class="login-page">


    <!-- 登录表单 -->
    <div class="login-container">
      <h2 class="form-title">欢迎登录<br> 数字病理远程会诊系统</h2>
      <el-form ref="loginFormRef" :model="loginForm" :rules="rules" class="login-form" auto-complete="on" label-position="left">
        <p style="color: #888888; /* 修改字体颜色为灰色 */
                  font-size: 14px; /* 调整字体大小 */
                  font-weight: normal; /* 控制字体粗细 */
                  margin: 0 22px;
                  ">用户名:</p>
        <el-form-item prop="username" style="margin:15px 20px; margin-top: 10px;" >
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            name="username"
            type="text"
            tabindex="1"
            auto-complete="on"
          />
        </el-form-item>
        <div v-if="isRegister">
          <p style="color: #888888; /* 修改字体颜色为灰色 */
                  font-size: 14px; /* 调整字体大小 */
                  font-weight: normal; /* 控制字体粗细 */
                  margin: 0 22px;
                  ">姓名:</p>
          <el-form-item prop="realName" style="margin:15px 20px; margin-top: 10px;" >
            <el-input
                v-model="loginForm.realName"
                placeholder="请输入姓名"
                name="username"
                type="text"
                tabindex="1"
                auto-complete="on"
            />
          </el-form-item>
        </div>
        <p style="color: #888888; /* 修改字体颜色为灰色 */
                  font-size: 14px; /* 调整字体大小 */
                  font-weight: normal; /* 控制字体粗细 */
                  margin: 22px 22px 10px;
                  ">密码:</p>
        <el-form-item  prop="password" style="position: relative;margin:30px 20px; margin-top: 10px;" >
          <el-input
            ref="passwordInputRef"
            :key="passwordType"
            v-model="loginForm.password"
            :type="passwordType"
            placeholder="请输入密码"
            name="password"
            tabindex="2"
            auto-complete="on"
            @keyup.enter="handleLogin"
          />
          <span class="show-pwd" @click="showPwd" style="position: absolute;top:0px;width: 20px;">
            <view-icon v-if="passwordType === 'password'" />
            <view-off-icon v-else />
          </span>
        </el-form-item>
        <div v-if="isRegister">
          <p style="color: #888888; /* 修改字体颜色为灰色 */
                  font-size: 14px; /* 调整字体大小 */
                  font-weight: normal; /* 控制字体粗细 */
                  margin: 22px 22px 10px;
                  ">确认密码:</p>
          <el-form-item  prop="passwordConfirm" style="position: relative;margin:30px 20px; margin-top: 10px;" >
            <el-input
                ref="passwordInputRef"
                :key="passwordType"
                v-model="loginForm.passwordConfirm"
                :type="passwordType"
                placeholder="请确认密码"
                name="passwordConfirm"
                tabindex="2"
                auto-complete="on"
            />
            <span class="show-pwd" @click="showPwd" style="position: absolute; top:0px; width: 20px;">
            <view-icon v-if="passwordType === 'password'" />
            <view-off-icon v-else />
          </span>
          </el-form-item>
        </div>
        <el-button v-if="!isRegister" :loading="loading" type="primary" style="width: calc(100% - 40px); margin-left: 20px; margin-bottom: 20px;" @click.prevent="handleLogin">登录</el-button>
        <el-button v-else :loading="loading" type="warning" style="width: calc(100% - 40px); margin-left: 20px; margin-bottom: 20px;" @click.prevent="handleRegister">注册</el-button>
        <el-button v-if="!isRegister" @click="isRegister = true; clearForm()" type="primary" text>注册</el-button>
        <el-button v-else @click="isRegister = false; clearForm()" type="primary" text>返回登录</el-button>
        <el-button style="margin-left: 0px;" @click="router.push(`/`)" type="info" text>主页</el-button>
        <!-- float: right;margin-left: 0px;margin-right: 230px; -->
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { useStore } from 'vuex'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { View as ViewIcon, Hide as ViewOffIcon } from '@element-plus/icons-vue'
import "../../api/user.js"
import {register} from "@/api/user.js";

onMounted(() => {
  redirect.value = route.query && route.query.redirect
})

const loginForm = reactive({
  username: '',
  realName: '',
  password: '',
  passwordConfirm: '',
})

const clearForm = () => {
  loginForm.username = ''
  loginForm.password = ''
  loginForm.passwordConfirm = ''
}

const rules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  realName: [
    { validator: checkRegister, trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  passwordConfirm: [
    { validator: checkEqual, trigger: 'blur' },
    { validator: checkRegister, trigger: 'blur' }
  ]
})

function checkEqual(rule, value, callback) {
  if (value !== loginForm.password) {
    callback(new Error("两次输入密码不同，请重新输入"));
  } else {
    callback()
  }
}

function checkRegister(rule, value, callback) {
  if (!value) {
    if (rule.field === 'passwordConfirm')
      callback(new Error('请确认密码'))
    else if (rule.field === 'realName')
      callback(new Error('请输入姓名'))
  } else {
    callback()
  }
}

const passwordInputRef = ref(null)
const loading = ref(false)
const passwordType = ref('password')
const loginFormRef = ref(null)
const router = useRouter()
const store = useStore()
const route = useRoute()
const redirect = ref(undefined)
// ref 不能直接嵌套另一个 ref?
console.log("第一次passwordInputRef", passwordInputRef)
console.log("第一次loginFormRef", loginFormRef)
const showPwd = () => {
  passwordType.value = passwordType.value === 'password' ? '' : 'password'
  nextTick(() => {
    if (loginFormRef.value) {
      // 希望用户进入页面后，光标直接定位在输入框上，或当用户点击某个按钮后，
      // 自动将光标移到某个输入框中，这时就可以使用 focus() 方法。
      // console.log("loginFormRef",loginFormRef)
      // console.log("passwordInputRef",passwordInputRef)
      // passwordInputRef.focus()
      // 上面的不知道为什么不行....
      console.log("聚焦")
    }
  })
}

const handleLogin = () => {
  if (isRegister.value)
    return
  loginFormRef.value.validate(valid => {
    if (valid) {
      loading.value = true
      store.dispatch('user/login', loginForm).then(() => {
        router.push({ path: redirect.value || '/' })
        loading.value = false
      }).catch(() => {
        loading.value = false
        ElMessage.error('登录失败')
      })
    } else {
      return false
    }
  })
}

const isRegister = ref(false)

const handleRegister = () => {
  if (!isRegister.value)
    return
  loginFormRef.value.validate(valid => {
    if (valid) {
      loading.value = true
      const formData = new FormData()
      formData.append('username', loginForm.username)
      formData.append('password', loginForm.password)
      formData.append('name', loginForm.realName)
      register(formData).then((res) => {
        if (res.code === 200) {
          ElMessage.success('注册成功，正在登录')
          isRegister.value = false
          handleLogin()
        } else {
          ElMessage.error(res.msg)
        }
        loading.value = false
      }).catch(() => {
        loading.value = false
      })
    } else {
      return false
    }
  })
}
</script>

<style scoped>
/* 整个页面容器 */
.login-page {
  position: relative;
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: flex-end; /* 将内容推到右边 */
  align-items: center;
  background: url('/med1/med1.gif') no-repeat center center;
  background-size: cover;
}

/* 登录表单容器 */
.login-container {
  width: 400px;
  padding: 40px 20px;  /* 调整内边距 */
  background-color: rgba(255, 255, 255, 0.767);
  box-shadow: 0 0 15px rgba(0, 0, 0, 0.2); /* 加强阴影 */
  border-radius: 8px;
  text-align: left;
  margin-right: 100px; /* 右侧对齐 */
   /* 新增蓝色边框 */
   border-top: 8px solid #007bff; /* 仅顶部添加5px宽的蓝色实线 */
}

h2 {
  color: #007bff;
  font-size: 24px;
  margin-bottom: 40px; /* 增加上下间距 */
  margin-left: 20px;
}

h3 {
  color: #555;
  font-size: 16px;
  margin-bottom: 20px;
}

.el-form-item {
  margin-bottom: 20px;
}

.show-pwd {
  position: absolute;
  right: 10px;
  top: 10px;
  font-size: 16px;
  color: #889aa4;
  cursor: pointer;
  user-select: none;
}

.el-button--text {
  margin-top: -10px;
}
</style>
