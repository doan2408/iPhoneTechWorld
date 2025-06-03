import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/admin/thong-ke' 
});
export const getDashboardAdmin = async (params) => {
  try {
    const response = await axiosInstance.get('/tong-quan', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy thống kê tổng quan:', error);
    throw error.response?.data || 'Lỗi lấy thống kê tổng quan';
  }
};
export const getDoanhThuTheoThang = async (params) => {
  try {
    const response = await axiosInstance.get('/doanh-thu-theo-thang', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy thống kê tổng quan:', error);
    throw error.response?.data || 'Lỗi lấy thống kê tổng quan';
  }
};


