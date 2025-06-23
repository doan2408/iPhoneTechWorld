import { ElMessage } from "element-plus";
import api from "./axiosInstance";

const login = async (taiKhoan, matKhau) => {
  try {
    const response = await api.post(`/api/auth/login`, { taiKhoan, matKhau });
    const { accessToken, refreshToken, fullName, id, roles, message } = response.data;

    if (!accessToken || !refreshToken) {
      throw new Error("Phản hồi thiếu accessToken hoặc refreshToken");
    }

    let parsedRoles = Array.isArray(roles) ? roles : [roles.role || roles];
    return { message, roles: parsedRoles, accessToken, refreshToken, fullName, id };

  } catch (error) {
    console.error("Lỗi trong LoginService.login:", error);

    const res = error.response?.data;

    if (Array.isArray(res)) {
      throw res;
    }

    // Nếu backend trả về lỗi object
    throw [{
      field: "server",
      message: res?.message || error.message || "Đăng nhập thất bại",
    }];
  }
};



const getCurrentUser = async () => {
  try {
    const response = await api.get(`/api/auth/me`);
    console.log("Thông tin người dùng:", response.data);
    return response.data;
  } catch (error) {
    console.error("Lỗi lấy thông tin người dùng:", error);
    throw error.response?.data || "Lỗi lấy thông tin người dùng";
  }
};

const refreshToken = async (refreshToken) => {
  try {
    const response = await api.post(`/api/auth/refresh`, { refreshToken });
    console.log("Phản hồi refresh token:", response.data);
    return response.data;
  } catch (error) {
    console.error("Lỗi trong LoginService.refreshToken:", error);
    throw error.response?.data || "Lỗi làm mới token";
  }
};

// const logout = async () => {
//   try {
//     const refreshToken = localStorage.getItem("refreshToken");

//     await api.post(`/api/auth/logout`, {
//       refreshToken, // gửi trong body
//     });

//     // Xóa local token
//     localStorage.removeItem("accessToken");
//     localStorage.removeItem("refreshToken");

//     ElMessage.success("Đăng xuất thành công. Đang chuyển hướng...");

//     // Chuyển hướng về trang login
//     setTimeout(() => {
//       window.location.href = "/login";

//       // Sau khi chuyển hướng xong → reload lại sau 1 giây
//       setTimeout(() => {
//         window.location.reload();
//       }, 1000);
//     }, 300); // Delay 1 giây để tránh đụng context cũ
//   } catch (error) {
//     console.error("Lỗi đăng xuất:", error);
//   }
// };


const logout = async () => {
  try {
    await api.post("/api/auth/logout", {
      refreshToken: localStorage.getItem("refreshToken"),
    });
  } catch (error) {
    console.error("Logout fail:", error);
  } finally {
    // Dọn token trước khi reload
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");

    // Xong rồi mới reload lại trang đăng nhập
    window.location.href = "/login";
  }
};


export default { login, getCurrentUser, refreshToken, logout };