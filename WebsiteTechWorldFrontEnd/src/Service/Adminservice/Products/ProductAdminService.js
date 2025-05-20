import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/admin/product',
  withCredentials: true // ❗ Gửi cookie (JSESSIONID) qua CORS
});

export const getAllSanPham = async (page =0) => {
  try {
    const response = await axiosInstance.get('', {
    params:  { page } // Truyền page vào query string
    });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy sản phẩm:', error);
    throw error.response?.data || 'Lỗi lấy sản phẩm';
  }
};


export const detailSanPham = async (id) => {
  try {
    const response = await axiosInstance.get(`/${id}`);
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy sản phẩm:', error);
    throw error.response?.data || 'Lỗi lấy sản phẩm';
  }
};

