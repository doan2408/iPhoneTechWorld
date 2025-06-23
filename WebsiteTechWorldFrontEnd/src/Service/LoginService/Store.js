// src/Service/LoginService/Store.js
import { createStore } from "vuex";
import LoginService from "./Login"; // Import LoginService
import SignUp from "./SignUp";

const store = createStore({
  //State là một nguồn dữ liệu duy nhất (single source of truth) lưu trữ toàn bộ dữ liệu của ứng dụng trong Vuex store.
  //Nó là một đối tượng phản ứng (reactive) chứa các giá trị mà ứng dụng cần để hoạt động,
  //chẳng hạn như dữ liệu người dùng, cài đặt, hoặc trạng thái giao diện.
  state() {
    return {
      isLoggedIn: localStorage.getItem("isLoggedIn") === "true", //trạng thái đăng nhập
      user: JSON.parse(localStorage.getItem("user")) || null, // Thông tin người dùng
      roles: [], // Vai trò của người dùng
      registrationStatus: null, //trạng thái đăng kí
    };
  },
  //mutations là các hàm đồng bộ (synchronous) dùng để thay đổi trạng thái (state) trong store.
  //Chúng là cách duy nhất để thay đổi dữ liệu trong state một cách hợp lệ.
  mutations: {
    setUser(state, user) {
      state.user = user; // Cập nhật thông tin người dùng
    },
    setRoles(state, roles) {
      state.roles = roles; // Cập nhật vai trò
    },
    clearUser(state) {
      state.user = null;
      state.roles = []; // Xóa thông tin người dùng
      state.isLoggedIn = false;
    },
    setLoggedIn(state, status) {
      state.isLoggedIn = status;
    },
    setRegistrationStatus(state, status) {
      state.registrationStatus = status;
    },
  },
  actions: {
    async login({ commit, dispatch }, { taiKhoan, matKhau }) {
      try {
        const { message, roles } = await LoginService.login(
          taiKhoan,
          matKhau
        );
        commit("setRoles", roles);
        commit("setLoggedIn", true);
        localStorage.setItem("isLoggedIn", "true");
        await new Promise((resolve) => setTimeout(resolve, 100)); // đợi 100ms

        // ⬇ Gọi API /me để lấy full user
        const user = await LoginService.getCurrentUser();
        commit("setUser", user);
        localStorage.setItem("user", JSON.stringify(user)); // nếu muốn giữ khi reload

        return message;
      } catch (error) {
        if (Array.isArray(error)) {
          throw error;
        } else {
          throw [
            { field: "server", message: error.message || "Lỗi đăng nhập" },
          ];
        }
      }
    },
    async logout({ commit }) {
      try {
        await LoginService.logout(); // Gọi service logout
        commit("clearUser");
        localStorage.removeItem("isLoggedIn"); // Xóa trạng thái đăng nhập
      } catch (error) {
        console.error("Lỗi đăng xuất:", error);
        commit("clearUser");
      }
    },
    async fetchCurrentUser({ commit }) {
      try {
        const userData = await LoginService.getCurrentUser(); // Lấy thông tin người dùng
        commit("setUser", userData);
        commit("setRoles", userData.roles || []);
        localStorage.setItem("user", JSON.stringify(userData));
      } catch (error) {
        commit("clearUser");
        throw error.response?.data || "Lỗi lấy thông tin người dùng";
      }
    },
    async register({ commit }, userData) {
      try {
        commit("setRegistrationStatus", "loading");

        //call signUp service
        const response = await SignUp.register(userData);
        commit("setRegistrationStatus", "success");
      } catch (error) {
        commit("setRegistrationStatus", "error");
        if (Array.isArray(error)) {
          throw error; // Ném mảng lỗi từ LoginService
        } else {
          throw [
            { field: "server", message: error.message || "Đăng ký thất bại" },
          ]; // Chuẩn hóa thành mảng
        }
      }
    },
  },
  getters: {
    isAdmin(state) {
      return state.roles.includes("ROLE_ADMIN");
    },
    isStaff(state) {
      return state.roles.includes("ROLE_STAFF");
    },
    isCustomer(state) {
      return state.roles.includes("ROLE_KHACH_HANG");
    },
    user(state) {
      return state.user;
    },
  },
});

export default store;
