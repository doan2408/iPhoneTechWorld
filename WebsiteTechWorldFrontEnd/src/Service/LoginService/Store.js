// src/store.js
import { createStore } from 'vuex';
import LoginService from '@/Service/LoginService/Login';  // Import LoginService

const store = createStore({
  state() {
    return {
      user: null,
      roles: []
    };
  },
  mutations: {
    setUser(state, user) {
      state.user = user;
    },
    setRoles(state, roles) {
      state.roles = roles;
    },
    clearUser(state) {
      state.user = null;
      state.roles = [];
    }
  },
  actions: {
    async login({ commit }, { tai_khoan, mat_khau }) {
      try {
        const { message, roles } = await LoginService.login(tai_khoan, mat_khau);
        commit('setUser', { tai_khoan });
        commit('setRoles', roles);
        return message;  // Trả về thông báo sau khi đăng nhập thành công
      } catch (error) {
        throw error.response?.data || 'Lỗi đăng nhập';
      }
    },
    async logout({ commit }) {
      try {
        await LoginService.logout();  // Gọi service logout
        commit('clearUser');
      } catch (error) {
        console.error('Lỗi đăng xuất:', error);
        commit('clearUser');
      }
    },
    async fetchCurrentUser({ commit }) {
      try {
        const userData = await LoginService.getCurrentUser();  // Gọi service để lấy thông tin người dùng
        commit('setUser', userData);
        commit('setRoles', userData.roles || []);
      } catch (error) {
        commit('clearUser');
        throw error.response?.data || 'Lỗi lấy thông tin người dùng';
      }
    }
  },
  getters: {
    isAdmin(state) {
      return state.roles.includes('ROLE_ADMIN');
    },
    isStaff(state) {
      return state.roles.includes('ROLE_STAFF');
    },
    isCustomer(state) {
      return state.roles.includes('ROLE_KHACH_HANG');
    },
    user(state) {
      return state.user;
    }
  }
});

export default store;
