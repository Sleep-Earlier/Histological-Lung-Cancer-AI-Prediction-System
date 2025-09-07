// 记住vite.config.js中设置的mock文件根目录不是在src
export default [
  // --------------------------------------------------
  {
    url: '/api/user/tokenInfo',
    method: 'get',
    response: (req) => {
      // 检查请求头中是否包含 Authorization，头部字段名称应为小写
      // 检查请求头中是否包含 Authorization
      const token = req.headers['authorization'];
      console.log('tokenInfo请求已经被收到,为:', req);
      console.log('token为:', token);
      if (!token) {
        console.log("没有检测到token");
        return {
          code: 401,
          msg: 'Token is missing',
        };
      }
      console.log('返回正常数据');
      // 模拟返回的数据
      return {
        code: 200,
        msg: null,
        data: {
          roles: ['admin'],
          name: 'xhk',
          avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
          introduction: 'I am an admin role',
          realname: '恒锴'
        }
      };
    }
  },

  // --------------------------------------------------
  {
    url: '/api/user/logoutVerify',
    method: 'post',  // 假设这个接口使用POST方法
    response: (req) => {
      // 打印请求信息
      console.log('logoutVerify请求已经被收到,为:', req);
      return {
        code: 200,
        msg: null,
        data: 'Logout successful'
      };
    }
  },
  // --------------------------------------------------


];

  