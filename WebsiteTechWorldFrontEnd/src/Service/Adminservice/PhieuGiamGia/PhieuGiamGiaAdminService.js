import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/admin/phieu-giam-gia',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const getAll = async (params) => {
  try {
    const response = await axiosInstance.get('', { params });
    return response.data;
  } catch (error) {
    console.error('Lỗi khi lấy danh sách phiếu giảm giá:', error);
    throw error.response?.data || new Error('Lỗi khi lấy danh sách phiếu giảm giá');
  }
};

export const detail = async (id) => {
  try {
    const response = await axiosInstance.get(`/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Lỗi khi lấy chi tiết phiếu giảm giá với ID ${id}:`, error);
    throw error.response?.data || new Error('Lỗi khi lấy chi tiết phiếu giảm giá');
  }
};

export const add = async (data) => {
  try {
    const response = await axiosInstance.post('', data);
    return response.data;
  } catch (error) {
    console.error('Lỗi khi thêm phiếu giảm giá:', error);
    throw error.response?.data || new Error('Lỗi khi thêm phiếu giảm giá');
  }
};

export const update = async (id, data) => {
  try {
    const response = await axiosInstance.put(`/${id}`, data);
    return response.data;
  } catch (error) {
    console.error(`Lỗi khi cập nhật phiếu giảm giá với ID ${id}:`, error);
    throw error.response?.data || new Error('Lỗi khi cập nhật phiếu giảm giá');
  }
};

export const deletePhieuGiamGia = async (id) => {
  try {
    const response = await axiosInstance.delete(`/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Lỗi khi xóa phiếu giảm giá với ID ${id}:`, error);
    throw error.response?.data || new Error('Lỗi khi xóa phiếu giảm giá');
  }
};

export const getAllKhachHang = async () => {
  try {
    const response = await axiosInstance.get('/khach-hang');
    return response.data;
  } catch (error) {
    console.error('Lỗi khi lấy danh sách khách hàng:', error);
    throw error.response?.data || new Error('Lỗi khi lấy danh sách khách hàng');
  }
};