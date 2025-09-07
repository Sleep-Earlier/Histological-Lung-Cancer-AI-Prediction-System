const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  realname: state => state.user.realname,
  name: state => state.user.name,
  roles: state => state.user.roles,
  permission_routes: state => state.permission.routes,
  appPage: state => state.appPage.itemTitle, // 获取 appPage 模块中的 itemTitle
  getWebIp: state => state.slice.webIp // 新增的 webIp getter
}
export default getters
