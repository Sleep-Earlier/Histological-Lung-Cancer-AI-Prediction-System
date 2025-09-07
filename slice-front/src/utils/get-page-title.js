// import defaultSettings from '@/settings'

// const title = defaultSettings.title || 'MedImageShowcaseVue3'
const title = 'MedImageShowcaseVue3'
// 生成页面标题的函数
export default function getPageTitle(pageTitle) {
  // 如果传入了具体的页面标题
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  // 如果没有传入页面标题，直接返回全局标题
  return `${title}`
}
