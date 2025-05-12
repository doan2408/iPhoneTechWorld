// src/Service/LoginService/Store.js
import { createStore } from 'vuex';
import LoginService from './Login';  // Import LoginService

const store = createStore({
  state() {
    return {
      user: null,  // Thông tin người dùng
      roles: [],   // Vai trò của người dùng
    };
  },
  mutations: {
    setUser(state, user) {
      state.user = user;  // Cập nhật thông tin người dùng
    },
    setRoles(state, roles) {
      state.roles = roles;  // Cập nhật vai trò
    },
    clearUser(state) {
      state.user = null;
      state.roles = [];  // Xóa thông tin người dùng
    }
  },
  actions: {
    async login({ commit }, { tai_khoan, mat_khau }) {
      try {
        const { message, roles } = await LoginService.login(tai_khoan, mat_khau);
        commit('setUser', { tai_khoan });
        commit('setRoles', roles);
        localStorage.setItem('isLoggedIn', 'true');  // Lưu trạng thái đăng nhập
        return message;
      } catch (error) {
        throw error.response?.data || 'Lỗi đăng nhập';
      }
    },
    async logout({ commit }) {
      try {
        await LoginService.logout();  // Gọi service logout
        commit('clearUser');
        localStorage.removeItem('isLoggedIn');  // Xóa trạng thái đăng nhập
      } catch (error) {
        console.error('Lỗi đăng xuất:', error);
        commit('clearUser');
      }
    },
    async fetchCurrentUser({ commit }) {
      try {
        const userData = await LoginService.getCurrentUser();  // Lấy thông tin người dùng
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
