import api from "./axiosInstance"


const API_URL = '/api/auth' 

// gửi mã xác nhận đăng ký
const registerVerify = async (userData) => {
  try {
    const response = await api.post(API_URL + `/register/verify`,
       userData );
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

// Hoàn tất đăng ký với mã xác nhận
const completeRegistration = async (email, verificationCode) => {
  try {
    const response = await api.post(API_URL + `/register/complete`, {
      email,
      verificationCode
    });
    
    return response.data;
  } catch (error) {
    if (error.response) {
      throw error.response.data || 'Lỗi hoàn tất đăng ký';
    } else if (error.request) {
      throw 'Lỗi kết nối mạng';
    } else {
      throw 'Có lỗi xảy ra khi hoàn tất đăng ký';
    }
  }
};

// Gửi lại mã xác nhận
const resendVerificationCode = async (email) => {
  try {
    const response = await api.post(API_URL + `/register/resend-code`, {
      email
    });
    return response.data;
  } catch (error) {
    if (error.response) {
      throw error.response.data || 'Lỗi gửi lại mã xác nhận';
    } else if (error.request) {
      throw 'Lỗi kết nối mạng';
    } else {
      throw 'Có lỗi xảy ra khi gửi lại mã xác nhận';
    }
  }
};



// Đăng xuất
export const logout = async () => {
  try {
    const response = await api.post(API_URL + `/logout`, {}, {
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
    const response = await api.get(API_URL + `/me`, {
      withCredentials: true
    })
    return response.data  // Trả về thông tin người dùng
  } catch (error) {
    throw error.response?.data || 'Lỗi lấy thông tin người dùng'
  }
}

export default {
  registerVerify,
  completeRegistration,
  resendVerificationCode,
  logout,
  getCurrentUser
}
