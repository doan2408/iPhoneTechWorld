import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/client',
  withCredentials: true // ❗ Gửi cookie (JSESSIONID) qua CORS
});

export const getAllSanPham = async () => {
  try {
    const response = await axiosInstance.get('/home');
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy sản phẩm:', error);
    throw error.response?.data || 'Lỗi lấy sản phẩm';
  }
};
