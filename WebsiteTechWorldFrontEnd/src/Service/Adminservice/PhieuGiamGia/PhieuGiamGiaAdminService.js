import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/admin/phieu-giam-gia' 
});

export const getAll = async (params) => {
  try {
    const response = await axiosInstance.get('', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy phiếu giảm giá:', error);
    throw error.response?.data || 'Lỗi lấy phiếu giảm giá';
  }
};

export const detail = async (id) => {
  try {
    const response = await axiosInstance.get(`/${id}`);
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy phiếu giảm giá:', error);
    throw error.response?.data || 'Lỗi lấy phiếu giảm giá';
  }
};

export const add = async (data) => {
  try {
    const response = await axiosInstance.post('', data); 
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi thêm phiếu giảm giá:', error);
    throw error.response?.data || 'Lỗi thêm phiếu giảm giá';
  }
};

export const update = async (id, data) => {
  try {
    const response = await axiosInstance.put(`/${id}`, data); 
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi cập nhật phiếu giảm giá:', error);
    throw error.response?.data || 'Lỗi cập nhật phiếu giảm giá';
  }
};





