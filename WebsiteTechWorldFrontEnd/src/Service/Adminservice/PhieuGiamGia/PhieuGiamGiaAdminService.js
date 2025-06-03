import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/admin/phieu-giam-gia',
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
    const errorMessage = error.response?.data?.message || 'Lỗi khi lấy danh sách phiếu giảm giá';
    throw new Error(errorMessage);
  }
};

export const detail = async (id) => {
  try {
    const response = await axiosInstance.get(`/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Lỗi khi lấy chi tiết phiếu giảm giá với ID ${id}:`, error);
    const errorMessage = error.response?.data?.message || 'Lỗi khi lấy chi tiết phiếu giảm giá';
    throw new Error(errorMessage);
  }
};

export const add = async (data) => {
  try {
    const response = await axiosInstance.post('', data);
    return response.data;
  } catch (error) {
    console.error('Lỗi khi thêm phiếu giảm giá:', error);
    const errorMessage = error.response?.data?.message || 'Lỗi khi thêm phiếu giảm giá';
    throw new Error(errorMessage);
  }
};

export const update = async (id, data) => {
  try {
    const response = await axiosInstance.put(`/${id}`, data);
    return response.data;
  } catch (error) {
    console.error(`Lỗi khi cập nhật phiếu giảm giá với ID ${id}:`, error);
    const errorMessage = error.response?.data?.message || 'Lỗi khi cập nhật phiếu giảm giá';
    throw new Error(errorMessage);
  }
};

export const deletePhieuGiamGia = async (id) => {
  try {
    const response = await axiosInstance.delete(`/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Lỗi khi xóa phiếu giảm giá với ID ${id}:`, error);
    const errorMessage = error.response?.data?.message || 'Lỗi khi xóa phiếu giảm giá';
    throw new Error(errorMessage);
  }
};

export const getAllKhachHang = async () => {
  try {
    const response = await axiosInstance.get('/khach-hang');
    return response.data;
  } catch (error) {
    console.error('Lỗi khi lấy danh sách khách hàng:', error);
    const errorMessage = error.response?.data?.message || 'Lỗi khi lấy danh sách khách hàng';
    throw new Error(errorMessage);
  }
};