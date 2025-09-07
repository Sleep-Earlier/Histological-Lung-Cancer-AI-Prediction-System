<template>
  <el-container>
    <el-main>
      <el-row :gutter="20">
        <!-- 显示当前页的数据 -->
        <el-col :span="6" v-for="(item, index) in items" :key="index">
          <!-- style="border-bottom: 4px solid #409eff; /* 3px宽度的蓝色实线 */" -->
          <el-card class="card-total" >
            <!-- <template #header> -->
              <div class="card-header">
                <span style="font-size: 18px;">{{ item.name }}</span>
                <el-tag class="status-tag" type="success">已开通</el-tag>
              </div>
            <!-- </template> -->
            <div style="font-size: 15px; margin-bottom: 30px;">
              <span>{{ item.describe }}</span>
            </div>
            <!-- <template #footer> -->
              <div class="right">
                <el-button type="danger" @click="cancelService(item)">取消开通</el-button>
                <el-button type="primary" @click="viewDetails(item)">查看详情</el-button>
              </div>
            <!-- </template> -->
          </el-card>
        </el-col>
      </el-row>
      <!-- 分页组件 -->
      <el-pagination
        layout="prev, pager, next"
        :current-page="currentPage"
        :page-size="pageSize"
        :total="totalItems"
        @current-change="handlePageChange"
      />
    </el-main>
  </el-container>
</template>

<script>
import { ref, onMounted } from 'vue';
import { ElContainer, ElMain, ElRow, ElCol, ElCard, ElButton, ElPagination } from 'element-plus';
import axios from 'axios';
import { getToken } from '@/utils/auth'; 
import { useRouter } from 'vue-router';
import { ElLoading } from 'element-plus'; // 引入 el-loading 组件
import { ElMessage } from 'element-plus'; // 引入消息提示组件
import { useStore } from 'vuex'; // 引入 useStore

export default {
  components: {
    ElContainer,
    ElMain,
    ElRow,
    ElCol,
    ElCard,
    ElButton,
    ElPagination
  },
  setup() {
    const items = ref([]); // 存储从后端获取的所有 item
    const currentPage = ref(1); // 当前页码
    const pageSize = ref(6); // 每页显示的条数
    const totalItems = ref(0); // 总条数
    const token = ref(getToken());
    const router = useRouter(); // 获取路由实例
    const store = useStore(); // 获取 Vuex store 实例
    // 组件挂载后请求数据
    onMounted(() => {
      fetchItems();
    });

    // 获取数据
    const fetchItems = () => {
      const loadingInstance = ElLoading.service({
        lock: true,
        text: '加载中...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)' // 可以调整背景透明度
      });

      axios.get('http://localhost:8083/application/queryByPage', {
        params: {
          pageNum: currentPage.value,
          pageSize: pageSize.value
        },
        headers: {
          Authorization: `Token:${token.value}` // 在请求头中添加 Token
        },
        //  timeout: 5000 // 设置请求超时时间为 5 秒
        // axios 默认的请求超时时间是没有限制的，也就是说，如果你不设置 timeout，axios 的请求将一直等待，直到服务器返回响应或者连接被手动关闭
        // 还可以设置自动重发请求的次数...
      })
      .then(response => {
        const data = response.data.data;
        console.log(response);

        // 将后端返回的 list 数据赋值给 items
        items.value = data.list;

        // 设置分页数据
        currentPage.value = data.pageNum;
        pageSize.value = data.pageSize;
        totalItems.value = data.total;
        loadingInstance.close();
      })
      .catch(error => {
        console.error('Error fetching items:', error);
        // 如果请求失败，也关闭加载动画
        loadingInstance.close();
        // 显示错误提示信息
        ElMessage({
          message: '加载数据失败，请稍后再试。',
          type: 'error',
          duration: 3000 // 显示时间为3秒
        });
      });
    };

    // 查看详情
    const viewDetails = (item) => {
      console.log(`查看详情: ${item}`);
      console.log(item);
      // 使用 Vuex 存储 item.title
      store.dispatch('appPage/setItemTitle', item.name); 
      store.dispatch('appPage/setAppId', item.id);
      router.push('/sliceResultView'); // 使用 router 实例进行路由跳转

      // 你完全不想在 URL 中暴露参数，不管是 params 还是 query，可以通过使用 Vuex 或局部状态管理器，而不是将参数直接通过 URL 传递
      // 下面两种都会暴露，可以通过 Vuex 存储数据而非通过 URL 传递
      // this.$router.push({ name: 'AppPage', params: { id: item.id } });
      // { path: '/appPage/:id', component: AppPage, name: 'AppPage' }
    };

    // 取消服务
    const cancelService = (item) => {
      axios.post('http://localhost:8083/application/cancel',{ id: item.id }, {
        headers: {
          Authorization: `Token:${token.value}` // 在请求头中添加 Token
        }
        })
        .then(() => {
          fetchItems(); // 重新获取数据
        })
        .catch(error => {
          console.error('Error canceling service:', error);
        });
    };
    
    // 处理分页切换
    const handlePageChange = (newPage) => {
      currentPage.value = newPage;
      fetchItems(); // 切换分页时重新获取数据
    };

    return {
      items,
      currentPage,
      pageSize,
      totalItems,
      fetchItems,
      viewDetails,
      cancelService,
      handlePageChange
    };
  }
};
</script>

<style scoped>
.card-total:hover {
  border-bottom: 4px solid #409eff;
}
/* .card-total :active {
  border-bottom: 4px solid #409eff;
} */
/* 无用,因为scoped */
.el-card__header {
  padding: 0px 0px 0px 0px !important;
}

.right {
  text-align: right; 
  margin-left: -20px;
  margin-right: -20px;
  margin-bottom: -20px;
  padding: 15px 20px 18px 20px;
  background-color: hsl(0, 0%,98%);
}
.status-tag {
  margin-left: 10px;
}

.card-header {
    /* padding: .5rem 1rem; */
    /* 曲线救国 */
    /*  margin-top: -1px; */
    margin-top: 8px;
    margin-bottom:25px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: 900;
    padding: 0px 0px 0px 0px ;
    background-color: rgba(255, 255, 255, 0);
    border-bottom: 1px solid rgba(251, 250, 250, 0.125);
}


</style>
