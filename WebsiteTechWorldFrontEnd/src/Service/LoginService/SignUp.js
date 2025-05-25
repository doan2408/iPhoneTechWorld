import axios from 'axios'

export const API_URL = 'http://localhost:8080/api/auth' 

// Đăng ký
const register = async (userData) => {
  try {
    const response = await axios.post(`${API_URL}/register`,
       userData ,
       { withCredentials: true });
    const { message, roles } = response.data;
    return { message, roles };
  } catch (error) {
    if (error.response) {
      // Nếu có response từ server
      throw error.response.data || 'Lỗi đăng ký';
    } else if (error.request) {
      // Nếu không có response (lỗi mạng, timeout, etc.)
      throw 'Lỗi kết nối mạng';
    } else {
      // Lỗi xảy ra khi thiết lập yêu cầu
      throw 'Có lỗi xảy ra khi gửi yêu cầu đăng ký';
    }
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
  register,
  logout,
  getCurrentUser
}
