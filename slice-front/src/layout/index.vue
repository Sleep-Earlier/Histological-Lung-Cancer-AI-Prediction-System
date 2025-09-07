<template>
  <div class="layout">
    <!-- 导航栏 -->
    <div class="navbar">
      <div class="left-menu">
        <!-- Logo 部分 -->
        <div class="logo">
          <!-- <img src="/path/to/logo.png" alt="Logo" class="logo-img"> -->
          <span class="logo-text">Microsense</span>
        </div>
        <!-- 导航按钮 -->
        <div class="nav-links">
          <router-link to="/dashboard">
            <el-button type="primary" text>首页</el-button>
          </router-link>
          <!-- 只有当 roles 不为空时才显示 "我的应用" -->
          <template v-if="roles.length > 0">
            <router-link to="/applications">
              <el-button type="primary" text>我的应用</el-button>
            </router-link>
          </template>
        </div>
      </div>
      
      <div class="right-menu">
      <!-- 用户头像或立即登录按钮 -->
      <template v-if="isLoggedIn">
        <el-dropdown class="avatar-container" trigger="click">
          <div class="avatar-wrapper">
            <img :src="avatar + '?imageView2/1/w/80/h/80'" class="user-avatar">
            <i class="el-icon-caret-bottom" />
          </div>
          <template #dropdown>
            <router-link style="text-decoration: none;" to="/">
              <el-dropdown-item>首页</el-dropdown-item>
            </router-link>
            <a target="_blank" style="text-decoration: none;" href="https://gitee.com/z040221/slice">
              <el-dropdown-item style="text-decoration: none;">仓库</el-dropdown-item>
            </a>
            <el-dropdown-item divided @click="logout">
              <span style="display:block;text-decoration: none;">登出</span>
            </el-dropdown-item>
          </template>
        </el-dropdown>
      </template>
      <template v-else>
        <el-button type="primary" @click="goToLogin">立即登录</el-button>
      </template>
      </div>
    </div>
    
    <!-- 内容部分 -->
    <div class="content">
      <router-view />
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

export default {
  setup() {
    const store = useStore()
    const router = useRouter()
    const avatar = computed(() => store.getters.avatar)

    // 获取用户的 roles
    const roles = computed(() => store.state.user.roles)

    // 判断用户是否已登录
    const isLoggedIn = computed(() => store.state.user.token)

    const logout = async () => {
      await store.dispatch('user/logout')
      router.push(`/`)
    }

    // 跳转到登录页面
    const goToLogin = () => {
      router.push('/login')
    }

    return {
      avatar,
      roles,
      isLoggedIn,
      logout,
      goToLogin
    }
  }
}
</script>

<style scoped>
.layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* 导航栏样式 */
.navbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);
  margin-bottom: 5px;
}

.left-menu {
  display: flex;
  align-items: center;
  margin-left: 20px;
}

.logo {
  display: flex;
  align-items: center;
}

.logo-img {
  width: 32px;
  height: 32px;
  margin-right: 8px;
}

.logo-text {
  font-size: 20px;
  font-weight: bold;
}

.nav-links {
  margin-left: 30px;
}

.right-menu {
  /* 拒绝使用浮动 */
  display: flex;
  align-items: center; /* 垂直居中 */
  justify-content: flex-end; /* 水平靠右对齐 */
  height: 100%;
  line-height: 50px;
  margin-right: 20px;
}

.avatar-container {
  margin-right: 30px;
}

.avatar-wrapper {
  position: relative;
  margin-top: 0px;
}

.user-avatar {
  cursor: pointer;
  width: 40px;
  height: 40px;
  border-radius: 10px;
}

.el-icon-caret-bottom {
  cursor: pointer;
  position: absolute;
  right: -20px;
  top: 25px;
  font-size: 12px;
}

.content {
  flex: 1;
  max-height: calc(100vh - 55px); /* 设置最大高度为100vh减去导航栏的高度 */
  overflow-y: auto; /* 如果内容超过这个高度，允许垂直滚动 */
}
</style>
