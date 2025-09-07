// appPage.js

const state = {
    itemTitle: sessionStorage.getItem('itemTitle') || '' ,// 用于存储 item.title,应用的名字
    appId: sessionStorage.getItem('appId') || '' // 用于存储 appId,应用的id
  };
  
  const mutations = {
    // 设置 itemTitle 的 mutation
    SET_ITEM_TITLE(state, title) {
      state.itemTitle = title;
      sessionStorage.setItem('itemTitle', title); 
    },
    // 设置 appId 的 mutation
    SET_APP_ID(state, id) {
      state.appId = id;
      sessionStorage.setItem('appId', id); // 同步到 sessionStorage
    }
  };
  
  const actions = {
    // 用于在组件中调用，设置 itemTitle
    setItemTitle({ commit }, title) {
        console.log('setItemTitle已调用,title为:',title)
        commit('SET_ITEM_TITLE', title);
    },
    // 用于在组件中调用，设置 appId
    setAppId({ commit }, id) {
      console.log('setAppId已调用, id为:', id);
      commit('SET_APP_ID', id);
    }
  };
  
  const getters = {
    // 获取 itemTitle
    getItemTitle: (state) => state.itemTitle,
     // 获取 appId
    getAppId: (state) => state.appId
  };
  
  export default {
    namespaced: true,
    state,
    mutations,
    actions,
    getters
  };
  