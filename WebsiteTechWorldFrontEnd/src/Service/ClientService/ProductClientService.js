import api from "../LoginService/axiosInstance";

 const baseURL = '/home';

export const getAllSanPham = async () => {
  try {
    const response = await api.get(`${baseURL}`);
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy sản phẩm:', error);
    throw error.response?.data || 'Lỗi lấy sản phẩm';
  }
};
