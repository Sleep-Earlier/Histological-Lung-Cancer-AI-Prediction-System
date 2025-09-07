import request from '@/utils/request'

// 获取切片列表
export function getSliceList(params) {
  return request({
    url: `${window.location.origin}/api/slice/list`,
    method: 'get',
    params // 简写方式，等效于 params: params
  })
}

// 上传切片
export function uploadSlice(data) {
  return request({
    url: `slice/insert`,
    method: 'post',
    data
  })
}

export function getSliceListBackEnd(params) {
  return request({
    url: `/slice/queryByPage`,
    method: 'get',
    params
  })
}

// 获取切片结果
export function getSliceResult(params) {
  return request({
    url: `/slice/getSliceResult`,
    method: 'get',
    params
  })
}

// 获取轮廓线结果
export function getCurveResult(sliceId) {
  return request({
    url: `/slice/getCurveResult`,
    method: 'get',
    params: {
      sliceId: sliceId,
    }
  })
}

// 获取热力图结果
export function getHeatMapResult(sliceId) {
  return request({
    url: `/slice/getHeatmapResult`,
    method: 'get',
    params: {
      sliceId: sliceId,
    }
  })
}

export function exportCSV(sliceId) {
  return request({
    url: `/slice/exportCSV`,
    method: 'get',
    params: {
      sliceId: sliceId,
    }
  })
}