import api from "./axiosInstance"

export const API_URL = "/api/auth";

// Đăng nhập
const login = async (taiKhoan, matKhau) => {
  try {
    console.log("Gửi payload:", { taiKhoan, matKhau });
    const response = await api.post(`${API_URL}/login`, {
      taiKhoan,
      matKhau,
    });
    console.log("Phản hồi từ server:", response.data);

    if (!response.data || typeof response.data !== "object") {
      throw [{ field: "server", message: "Phản hồi server không hợp lệ" }];
    }

    const {
      accessToken,
      refreshToken,
      fullName,
      id,
      roles,
      accessTokenExpiresAt,
      refreshTokenExpiresAt,
    } = response.data;

    let parsedRoles = Array.isArray(roles) ? roles : [roles];

    // Kiểm tra các trường bắt buộc
    if (!accessToken || !refreshToken || !fullName || !id) {
      throw [{ field: "server", message: "Thiếu dữ liệu trong phản hồi" }];
    }

    // Lưu vào localStorage
    localStorage.setItem("accessToken", accessToken);
    localStorage.setItem("refreshToken", refreshToken);
    localStorage.setItem("user", JSON.stringify({ id, fullName, roles: parsedRoles }));

    console.log("AccessToken:", localStorage.getItem("accessToken"));

    return { message: response.data.message || "Đăng nhập thành công", roles: parsedRoles };
  } catch (error) {
    console.error("Lỗi trong LoginService.login:", error);
    if (error.response?.data) {
      console.log("Dữ liệu lỗi từ server:", error.response.data);
      throw error.response.data;
    } else if (error.request) {
      console.log("Không nhận được phản hồi từ server");
      throw [{ field: "network", message: "Lỗi kết nối mạng" }];
    } else {
      console.log("Lỗi gửi yêu cầu:", error.message);
      throw [{ field: "request", message: "Có lỗi xảy ra khi gửi yêu cầu đăng nhập" }];
    }
  }
};

// Đăng xuất
export const logout = () => {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("refreshToken");
  localStorage.removeItem("user");
  window.location.href = "/login";
};

// Lấy thông tin người dùng hiện tại
export const getCurrentUser = async () => {
  try {
    const token = localStorage.getItem("accessToken");
    console.log("AccessToken sau login:", localStorage.getItem("accessToken"));
    const response = await api.get(`${API_URL}/me`);
    return response.data;
  } catch (error) {
    throw error.response?.data || "Lỗi lấy thông tin người dùng";
  }
};

export default {
  login,
  logout,
  getCurrentUser,
};
