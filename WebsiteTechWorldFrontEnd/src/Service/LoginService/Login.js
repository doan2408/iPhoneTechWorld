import api from "./axiosInstance";

const login = async (taiKhoan, matKhau) => {
  try {
    console.log("Gửi payload:", { taiKhoan, matKhau });
    const response = await api.post(`/api/auth/login`, { taiKhoan, matKhau });
    console.log("Phản hồi từ server:", response.data);
    const { accessToken, refreshToken, fullName, id, roles, message } = response.data;
    if (!accessToken || !refreshToken) {
      throw new Error("Phản hồi thiếu accessToken hoặc refreshToken");
    }
    let parsedRoles = Array.isArray(roles) ? roles : [roles.role || roles];
    return { message, roles: parsedRoles, accessToken, refreshToken, fullName, id };
  } catch (error) {
    console.error("Lỗi trong LoginService.login:", error);
    const errorMessage = error.response?.data?.message || error.message || "Lỗi server không xác định";
    throw [{ field: "server", message: errorMessage }];
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

const logout = async () => {
  try {
    await api.post(`/api/auth/logout`);
  } catch (error) {
    console.error("Lỗi đăng xuất:", error);
  }
};

export default { login, getCurrentUser, refreshToken, logout };