// store/modules/headerState.js
export default {
  namespaced: true,
  state: () => ({
    cartItemCount: 0,
  }),
  mutations: {
    setCartItemCount(state, count) {
      state.cartItemCount = count;
    },
  },
  getters: {
    getCartItemCount: (state) => state.cartItemCount,
  },
}

