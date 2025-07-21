// store/modules/headerState.js
export default {
  namespaced: true,
  state: () => ({
    userName: '',
  }),
  mutations: {
    setUserName(state, name) {
      state.userName = name
    },
  },
  getters: {
    getUserName: (state) => state.userName,
  },
}
