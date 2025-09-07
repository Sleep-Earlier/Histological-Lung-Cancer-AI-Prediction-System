## 前端项目修改指南

启动项目:

```
npm install
npm run dev
```



```
src/store/modules/permission.js
```

这里放着路由的相关内容, 登录后路由守卫会自动添加里面的内容

@指代的是src根目录

```
由vite.config.js中配置如下
resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src') // 将 @ 映射到 src 目录
    }
  }
```

发送请求给后端的时候使用类似 

```
src/api/user.js 
```

里的文件先import request from '@/utils/request' ,这样可以默认带上token ,同时返回值会先进行一次code的判断





