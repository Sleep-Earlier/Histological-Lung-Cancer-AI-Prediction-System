const state = {
    webIp: sessionStorage.getItem('webIp') || "http://122.51.113.54:8088" // 用于存储 webIp
  };
  
  const mutations = {
    // 设置 webIp 的 mutation
    SET_WEB_IP(state, ip) {
      state.webIp = ip;
      sessionStorage.setItem('webIp', ip); // 同步到 sessionStorage
    }
  };
  
  const actions = {
    // 用于在组件中调用，设置 webIp
    setWebIp({ commit }, ip) {
      console.log('setWebIp 已调用，webIp 为:', ip);
      commit('SET_WEB_IP', ip);
    }
  };
  
  const getters = {
    // 获取 webIp
    getWebIp: (state) => state.webIp
  };
  
  export default {
    namespaced: true,
    state,
    mutations,
    actions,
    getters
  };
  