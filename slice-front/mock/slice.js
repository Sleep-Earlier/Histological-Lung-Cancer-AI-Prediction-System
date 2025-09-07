export default [
    // 模拟获取切片数据的 API
    {
      url: '/api/slice/list',
      method: 'get',
      response: (req) => {
        return {
          code: 200,
          data: [
            {
              sliceNumber: "123456",
              sliceName: "肝脏切片",
              parseStatus: "已解析",
              parseProgress: 100,
              tbsReport: "123456"
            },
            {
              sliceNumber: "234567",
              sliceName: "肺部切片",
              parseStatus: "未解析",
              parseProgress: 0,
              tbsReport: null
            },
            {
                sliceNumber: "345687",
                sliceName: "肺部切片",
                parseStatus: "正在提取特征",
                parseProgress: 40,
                tbsReport: null
              }
          ]
        }
      }
    },
  
    // 模拟上传切片的 API，处理 FormData(  mockjs不支持FormData... )
    {
      url: '/api/slice/upload',
      method: 'post',
      response: (req) => {
        console.log('上传请求已经被收到,为:', req.body);
        // 解析 FormData 数据
        // const formData = req.body; // 这里假设 req.body 是 FormData 类型
        // const formObject = {};
        // formData.forEach((value, key) => {
        //   formObject[key] = value;
        // });
        // const { sliceName, fileList } = formObject;
        // const { sliceName, fileList } = req.body;
        // console.log('上传的切片名称为:', sliceName, '上传的文件列表为:', fileList);
        // if ( !sliceName || !fileList || fileList.length === 0) {
        //   return {
        //     code: 400,
        //     msg: '上传失败，请检查所有字段是否正确填写'
        //   };
        // }
        return {
          code: 200,
          msg: '上传成功'
        };
      }
    }
  ];
  