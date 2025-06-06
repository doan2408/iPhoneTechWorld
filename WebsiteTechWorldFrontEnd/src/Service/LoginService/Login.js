import axios from 'axios'

export const API_URL = 'http://localhost:8080/api/auth' 

// Đăng nhập
const login = async (tai_khoan, mat_khau) => {
  try {
    const response = await axios.post(`${API_URL}/login`, { tai_khoan, mat_khau }, { withCredentials: true });
    const { message, roles } = response.data;
    return { message, roles };
  } catch (error) {
    if (error.response && error.response.data) {
      throw error.response.data;
    } else if (error.request) {
      throw [{ field: "network", message: "Lỗi kết nối mạng" }];
    } else {
      throw [{ field: "request", message: "Có lỗi xảy ra khi gửi yêu cầu đăng nhập" }];
    }
  }
}

// Đăng xuất
export const logout = async () => {
  try {
    const response = await axios.post(`${API_URL}/logout`, {}, {
      withCredentials: true
    })
    // Đăng xuất thành công → xóa localStorage / Vuex state nếu có
    localStorage.removeItem('user')  // nếu có lưu thông tin user
    window.location.href = '/login';
    // Tự động làm mới sau 0,5 giây
    setTimeout(() => window.location.reload(), 500);
    return response.data
  } catch (error) {
    if (error.response && error.response.data) {
      throw error.response.data;
    } else if (error.request) {
      throw [{ field: "network", message: "Lỗi kết nối mạng" }];
    } else {
      throw [{ field: "request", message: "Có lỗi xảy ra khi gửi yêu cầu đăng nhập" }];
    }
  }
}

// Lấy thông tin người dùng hiện tại
export const getCurrentUser = async () => {
  try {
    const response = await axios.get(`${API_URL}/me`, {
      withCredentials: true
    })
    return response.data  // Trả về thông tin người dùng
  } catch (error) {
    throw error.response?.data || 'Lỗi lấy thông tin người dùng'
  }
}

export default {
  login,
  logout,
  getCurrentUser
}
