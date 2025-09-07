<template>
  <div>
    <div class="title-box fade-in" style="position: relative;">
      <h1 class="title " style="position: absolute; top:190px;left: 10%;">{{ itemTitle }}</h1>
      <p style="width: 40%;position: absolute;top:250px;left: 10%; font-weight: 500;">一款基于人工智能技术开发的医疗辅助工具,旨在为医疗机构提供快速、精准的诊断支持
      平台采用先进的深度学习和图像处理技术，结合丰富的临床数据，
      能够在短时间内完成复杂的病理分析，适用于筛查、初诊和辅助诊疗等多个应用场景。
      医生可以通过上传病理切片图像，实时获取系统分析结果
      <el-button type="success" @click="openTableDialog" class="button-below">打开应用</el-button>
    </p>
      
    </div>
    
    
    
    <!-- 上传切片的对话框 -->
    <el-dialog :title="上传切片" v-model="isDialogVisible" width="35%" @open="handleDialogOpen"
      :before-close="handleDialogClose" style="padding: 40px;">
      <!-- 表单 -->
      <el-form ref="uploadForm" :model="uploadForm" :rules="rules" label-width="120px">
        <!-- 切片号 -->
        <el-form-item label="切片号" prop="sliceNumber">
          <el-input v-model="uploadForm.sliceNumber" placeholder="请输入切片号"></el-input>
        </el-form-item>

        <!-- 切片名称 -->
        <el-form-item label="切片名称" prop="sliceName">
          <el-input v-model="uploadForm.sliceName" placeholder="请输入切片名称"></el-input>
        </el-form-item>

        <!-- 上传切片文件 -->
        <el-form-item label="上传切片文件" prop="fileList">
          <el-upload drag action="#" :limit="1" :file-list="uploadForm.fileList" :auto-upload="false"
            :on-change="handleChange" :on-remove="handleFileRemove" :on-exceed="exceedFile">
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">只能上传svs/png/jpg文件</div>
          </el-upload>
        </el-form-item>
      </el-form>

      <!-- 对话框底部按钮 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="isDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="isUploading">提交</el-button>
      </span>
    </el-dialog>

    <!-- 测试按钮 -->
    <!-- 全局遮罩按钮 -->
    <el-button type="primary" @click="showGlobalLoading" v-show="false">全局遮罩</el-button>
    <!-- 表格加载按钮 -->
    <el-button type="primary" @click="toggleTableLoading" v-show="false">表格Loading</el-button>


    <!-- 显露的部分 -->
    <el-dialog :title="itemTitle " v-model="isTableDialogVisible" width="70%">
    <div style="margin-left: 30px;margin-right: 30px; min-height: 60vh;max-height: 60vh;">
      <!-- 顶部操作区域 -->
      <div class="top-actions">
        <!-- 查询切片 -->
        <el-input v-model="searchSliceNumber" placeholder="输入切片号查询" style="width: 200px; margin-right: 20px;" />
        <el-button type="primary" @click="onSearchBySliceNumber">查找切片</el-button>
        <el-button type="warning" @click="onResetSliceNumber">重置</el-button>
        <!-- 打开上传对话框按钮 -->
        <el-button type="success" @click="openUploadDialog">上传切片</el-button>
      </div>
      <!-- 数据表格 -->
      <div class="app-container">
        <el-table v-loading="isListLoading" :data="filteredDataList" element-loading-text="Loading" border fit
          highlight-current-row>
          <el-table-column label="切片号" align="center">
            <template #default="scope">
              {{ scope.row.sliceNumber }}
            </template>
          </el-table-column>

          <el-table-column label="切片名称" align="center">
            <template #default="scope">
              <span>{{ scope.row.sliceName }}</span>
            </template>
          </el-table-column>

          <el-table-column label="解析状态" align="center">
            <template #default="scope">
              <el-tag :type="scope.row.parseStatus === '已解析' ? 'success' : 'warning'">
                {{ scope.row.parseStatus }}
              </el-tag>
            </template>
          </el-table-column>

          <el-table-column label="解析进度" align="center">
            <template #default="scope">
              <el-progress :percentage="scope.row.parseProgress" />
            </template>
          </el-table-column>

          <el-table-column label="操作" align="center">
            <template #default="scope">
              <!-- 已解析 -->
              <el-button v-if="scope.row.parseStatus === '已解析'" type="primary" @click="goToReport(scope.row.tbsReport)"
                plain>
                报告详情
              </el-button>

              <!-- 未解析 -->
              <el-button v-else-if="scope.row.parseStatus === '未解析'" type="warning" @click="startParsing(scope.row)"
                plain>
                解析
              </el-button>

              <!-- 解析中 -->
              <el-button v-else type="warning" :disabled="true" plain>
                解析中
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
   </el-dialog>
  </div>
</template>


<script>
import { ElMessage, ElLoading } from 'element-plus';
import { UploadFilled } from '@element-plus/icons-vue'
import { getSliceList, uploadSlice } from '@/api/slice' // 导入 API 方法
import { mapGetters } from 'vuex';
import { useRouter } from 'vue-router';
export default {
  components: {
    UploadFilled,
  },
  data() {
    return {
      isDialogVisible: false,  // 控制 上传对话框 显示的变量
      isTableDialogVisible: false, // 控制表格对话框的显示
      uploadForm: {
        sliceNumber: '',   // 切片号
        sliceName: '',     // 切片名称
        fileList: []       // 上传的文件列表
      },
      searchSliceNumber: '',  // 用于存储查询的切片号
      dataList: [     
      ],  // 存储表格数据

      isListLoading: false,  // 控制表格加载状态
      isUploading: false, // 控制全局加载状态
      // 验证规则
      rules: {
        sliceNumber: [{ required: true, message: '请输入切片号', trigger: 'blur' }],
        sliceName: [{ required: true, message: '请输入切片名称', trigger: 'blur' }],
        fileList: [{ required: true, message: '请上传切片文件', trigger: 'change' }]
      }
    }
  },
  computed: {
    // 根据查询的切片号过滤数据
    ...mapGetters('appPage', ['getItemTitle']),
    itemTitle() {
      console.log("获取Vuex中的item.title:", this.getItemTitle);
      return this.getItemTitle; // 获取 Vuex 中的 item.title
      
    },
    filteredDataList() {
      if (this.searchSliceNumber) {
        return this.dataList.filter(item => item.sliceNumber.includes(this.searchSliceNumber));
      }
      return this.dataList;
    }
  },
  methods: {
     // 跳转到 /applications，并弹出提示
     redirectToApplications() {
      const router = useRouter(); // 获取 router 实例
      // 弹出提示信息
      ElMessage({
        message: '未指定应用，请选择一个应用。',
        type: 'warning',
        duration: 3000 // 显示3秒
      });
      
      // 跳转到 /applications 页面
      router.push('/applications');
    },
    // 测试函数
    // 显示全局遮罩
    showGlobalLoading() {
      this.isUploading=true;
      // 模拟异步任务3秒后关闭全局遮罩
      setTimeout(() => {
        this.isUploading=false;
        ElMessage.success('全局遮罩关闭');
      }, 3000);
    },
    // 切换表格Loading状态
    toggleTableLoading() {
      this.isListLoading = !this.isListLoading;
      if (this.isListLoading) {
        ElMessage.info('表格加载中...');
      } else {
        ElMessage.success('表格加载结束');
      }
    },

    // 关于请求的函数
    // 开始的时候获取数据
    async fetchDataList() {
      this.isListLoading = true;
      try {
        const response = await getSliceList();  // 调用 API
        this.dataList = response.data || [];
      } catch (error) {
        ElMessage.error('获取数据失败');
      } finally {
        this.isListLoading = false;
      }
    },

    // 提交上传
    async handleSubmit() {
      this.isUploading = true;
      try {
        const formData = new FormData();
        formData.append('sliceNumber', this.uploadForm.sliceNumber);
        formData.append('sliceName', this.uploadForm.sliceName);
        if (this.uploadForm.fileList.length > 0) {
          formData.append('fileList', this.uploadForm.fileList[0].raw);
        }
        const response = await uploadSlice(formData);  // 调用 API
        ElMessage.success(response.msg || '上传成功');
        this.isDialogVisible = false;
        this.fetchDataList();  // 刷新列表
      } catch (error) {
        ElMessage.error('上传失败，请重试');
      } finally {
        this.isUploading = false;
      }
    },

    handleDialogOpen() {
      console.log('Dialog 已打开');
    },

    handleDialogClose(done) {
      if (this.isUploading) {
        ElMessage.warning("正在上传，请稍候...");
      } else {
        done();
      }
    },

    onResetSliceNumber() {
      this.searchSliceNumber = '';
    },
    openUploadDialog() {
      this.isDialogVisible = true;
      console.log('打开上传对话框:', this.isDialogVisible);
    },

    validateFile(file) {
      const validTypes = ['image/png', 'image/jpeg', 'image/jpg'];
      const extension = file.name.split('.').pop().toLowerCase();
      console.log("validateFile中文件类型:", file.raw.type);
      const isValidType = validTypes.includes(file.raw.type) || extension === 'svs';

      if (!isValidType) {
        this.$message.error('文件类型不支持！只允许上传 .svs, .png, .jpg 类型的文件。');
        return false;
      }
      if (file.size > 8 * 1024 * 1024) {
        this.$message.error(`${file.name} 文件大小不能超过 8MB`);
        return false;
      }
      return true;
    },

    //on-remove
    handleFileRemove(file, fileList) {
      console.log('移除文件:', file, fileList);
      this.uploadForm.fileList = fileList;
      console.log("移除后的文件列表:", this.uploadForm.fileList);
    },

    handleChange(file, fileList) {
      // 当有新文件被添加时触发
      console.log("处理新文件上传:", file, fileList);
      const isValid = this.validateFile(file);
      if (!isValid) {
        // 文件不符合要求时，从 fileList 中移除该文件
        const index = fileList.indexOf(file);
        if (index !== -1) {
          fileList.splice(index, 1);
        }
      }
      // 只有this.uploadForm.fileList变化或者说赋值的时候才会改变...el-upload的list...
      // 所以,下面这个必须要有，否则无法显示或者删除
      this.uploadForm.fileList = fileList;
      console.log("处理后uploadForm文件列表:", this.uploadForm.fileList);
    },

    //on-exceed
    exceedFile(files, fileList) {
      this.$message.warning('文件数量超出限制');
    },



    // handleSubmit() {
    //   // 提交逻辑
    //   ElMessage.success("上传成功");
    //   this.isDialogVisible = false;
    // },
    onSearchBySliceNumber() {
      if (!this.searchSliceNumber) {
        ElMessage.warning("请输入切片号进行查询");
      }
    },
    goToReport(tbsReport) {
      
      this.$router.push({ path: '/sliceResultView', query: { reportId: tbsReport } });
      // 跳转到报告详情页面
      // this.$router.push({ path: '/report', query: { reportId: tbsReport } });
    },
    startParsing(row) {
      ElMessage.info("开始解析: " + row.sliceName);
    },
    openTableDialog() {
      this.isTableDialogVisible = true;
    },
  },

  // 页面加载时获取数据
  mounted() {
    if (!this.itemTitle) {
      this.redirectToApplications();
    }
    this.fetchDataList();
  },

  watch: {
    // 监听 isUploading 变量
    isUploading(newValue) {
      if (newValue) {
        // 如果 isUploading 为 true，开启全局加载
        this.loadingInstance = ElLoading.service({
          lock: true,
          text: '正在加载...',
          background: 'rgba(0, 0, 0, 0.7)'
        });
      } else if (this.loadingInstance) {
        // 如果 isUploading 为 false，关闭全局加载
        this.loadingInstance.close();
      }
    }
  },
}
</script>

<style scoped>
.button-below {
  display: block; /* 确保按钮是块级元素，独占一行 */
  margin-top: 20px; /* 调整按钮与上方段落的间距 */
  margin-left: -5px;
}
.fade-in {
  opacity: 0;
  animation: fadeIn 2s ease forwards; /* 3秒渐显效果 */
}

/* 定义渐显动画 */
@keyframes fadeIn {
  0% {
    opacity: 0;
  }
  100% {
    opacity: 1;
  }
}

.title{
  font-size: 36px;
  font-weight: 600;
  color: #333;
  /* border-bottom: 2px solid #eee;  加个底线修饰 */
  font-family: 'Roboto', sans-serif;  /* 引入Google字体 */
}

.title-box {
  /* margin: 10px 10px; */
  margin-top: 0px;
  margin-bottom: 0px;
  /* padding: 10px 0; */
  letter-spacing: 2px;  /* 增加字间距 */

  /* background: #f9f9f9; */
  background: url('/back.png') no-repeat center center;
  /* 让背景图片覆盖整个区域 */
  background-size: cover;  
  /* background-size: contain; */
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  height: calc(100vh - 58px);
}

.dialog-footer {
  text-align: right;
}

.app-container {
  padding: 20px;
}

.top-actions {
  padding-left: 30px;
  margin-bottom: 10px;
  margin-top: 30px;
}
</style>
