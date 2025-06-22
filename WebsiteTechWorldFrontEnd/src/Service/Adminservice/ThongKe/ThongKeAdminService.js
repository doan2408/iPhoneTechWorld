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
export const getSanPhamBanChay = async (params) => {
  try {
    const response = await axiosInstance.get('/san-pham-ban-chay', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy thống kê tổng quan:', error);
    throw error.response?.data || 'Lỗi lấy thống kê tổng quan';
  }
};
// api/thongke.js
// export const getSanPhamBanChay = async ({ startDate, endDate, page = 1, limit = 10 }) => {
//   try {
//     const response = await axiosInstance.get('/san-pham-ban-chay', {
//       params: {
//         startDate,
//         endDate,
//         page,
//         limit
//       }
//     });
//     return response.data;
//   } catch (error) {
//     console.error('Lỗi khi lấy sản phẩm bán chạy:', error);
//     throw error.response?.data?.message || 'Lỗi không xác định khi lấy sản phẩm bán chạy';
//   }
// };





