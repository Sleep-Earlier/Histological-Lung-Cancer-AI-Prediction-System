<template>
    <div>
      <h1>文件夹读取测试</h1>
      <button @click="getFolderPath">选择文件夹</button>
      <div v-if="folderName">已选择文件夹: {{ folderName }}</div>
      <ul>
        <li v-for="(file, index) in fileList" :key="index">{{ file }}</li>
      </ul>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        folderName: '', // 存储文件夹名称
        fileList: [],   // 存储文件名列表
      };
    },
    methods: {
      async getFolderPath() {
        try {
          // 1. 请求用户选择文件夹
          const directoryHandle = await window.showDirectoryPicker();
  
          // 2. 获取文件夹路径名称
          this.folderName = directoryHandle.name;
          console.log('Selected folder:',this.folderName);
          // 3. 读取文件夹中的文件
          this.fileList = []; // 清空之前的文件列表
          for await (const entry of directoryHandle.values()) {
            if (entry.kind === 'file') {
              this.fileList.push(entry.name);
            }
          }
        } catch (error) {
          console.error('Error accessing folder:', error);
        }
      },
    },
  };
  </script>
  
  <style scoped>
  h1 {
    margin-bottom: 20px;
  }
  button {
    margin-bottom: 10px;
    padding: 10px;
  }
  ul {
    list-style-type: none;
    padding: 0;
  }
  li {
    padding: 5px 0;
  }
  </style>
  
  <!-- async getFolderPath() {
  try {
    // 1. 请求用户选择文件夹
    const directoryHandle = await window.showDirectoryPicker();

    // 2. 获取文件夹名称
    this.folderName = directoryHandle.name;
    console.log('Selected folder:', this.folderName);

    // 3. 清空之前的文件列表
    this.fileList = [];

    // 4. 递归遍历文件夹中的所有文件
    await this.readDirectory(directoryHandle);
  } catch (error) {
    console.error('Error accessing folder:', error);
  }
},

// 递归读取文件夹中的文件
async readDirectory(directoryHandle) {
  for await (const entry of directoryHandle.values()) {
    if (entry.kind === 'file') {
      // 如果是文件，加入文件列表
      this.fileList.push(entry.name);
    } else if (entry.kind === 'directory') {
      // 如果是文件夹，递归遍历子文件夹
      const subDirectoryHandle = await directoryHandle.getDirectoryHandle(entry.name);
      await this.readDirectory(subDirectoryHandle); // 递归调用
    }
  }
}
 -->