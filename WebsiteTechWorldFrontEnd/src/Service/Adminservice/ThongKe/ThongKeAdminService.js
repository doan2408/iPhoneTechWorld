
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
export const getKhachHangTrungThanh = async (params) => {
  try {
    const response = await axiosInstance.get('/khach-hang-trung-thanh', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy danh sách khách hàng trung thành:', error);
    throw error.response?.data || 'Lỗi lấy danh sách khách hàng trung thành';
  }
};
export const getSanPhamSapHetHang = async (params) => {
  try {
    const response = await axiosInstance.get('/san-pham-sap-het-hang', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy danh sách khách hàng trung thành:', error);
    throw error.response?.data || 'Lỗi lấy danh sách khách hàng trung thành';
  }
};
export const getTopSanPhamBanChay = async (startDate, endDate) => {
  try {
    const response = await axiosInstance.get('/top-san-pham-ban-chay', { startDate, endDate });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy thống kê tổng quan:', error);
    throw error.response?.data || 'Lỗi lấy thống kê tổng quan';
  }
};
export const getDonHangHuy = async (params) => {
  try {
    const response = await axiosInstance.get('/don-huy-theo-thang', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy thống kê tổng quan:', error);
    throw error.response?.data || 'Lỗi lấy thống kê tổng quan';
  }
};
