import axios from 'axios'

export const API_URL = 'http://localhost:8080/api/auth' 

// Đăng nhập
const login = async (tai_khoan, mat_khau) => {
  try {
    const response = await axios.post(`${API_URL}/login`, {
      tai_khoan,
      mat_khau
    }, {
      withCredentials: true  // Bảo đảm session/cookie được lưu lại
    });

    // Lưu thông tin user và role vào store hoặc localStorage
    const { message, roles } = response.data;
    return { message, roles };  // Trả về thông báo và roles
  } catch (error) {
    throw error.response?.data || 'Lỗi đăng nhập';
  }
};

// Đăng xuất
export const logout = async () => {
  try {
    const response = await axios.post(`${API_URL}/logout`, {}, {
      withCredentials: true
    })
    return response.data  // Thông báo đăng xuất thành công
  } catch (error) {
    throw error.response?.data || 'Lỗi đăng xuất'
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
