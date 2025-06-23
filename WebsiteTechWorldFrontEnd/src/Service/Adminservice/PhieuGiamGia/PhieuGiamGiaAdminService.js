import api from "@/Service/LoginService/axiosInstance";


const baseURL= '/admin/phieu-giam-gia';


export const getAll = async (params) => {
  try {
    const response = await api.get(baseURL, { params });
    return response.data;
  } catch (error) {
    console.error('Lỗi khi lấy danh sách phiếu giảm giá:', error);
    throw error.response?.data || new Error('Lỗi khi lấy danh sách phiếu giảm giá');
  }
};

export const detail = async (id) => {
  try {
    const response = await api.get( baseURL +`/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Lỗi khi lấy chi tiết phiếu giảm giá với ID ${id}:`, error);
    throw error.response?.data || new Error('Lỗi khi lấy chi tiết phiếu giảm giá');
  }
};

export const add = async (data) => {
  try {
    const response = await api.post(baseURL, data);
    return response.data;
  } catch (error) {
    console.error('Lỗi khi thêm phiếu giảm giá:', error);
    throw error.response?.data || new Error('Lỗi khi thêm phiếu giảm giá');
  }
};

export const update = async (id, data) => {
  try {
    const response = await api.put( baseURL +`/${id}`, data);
    return response.data;
  } catch (error) {
    console.error(`Lỗi khi cập nhật phiếu giảm giá với ID ${id}:`, error);
    throw error.response?.data || new Error('Lỗi khi cập nhật phiếu giảm giá');
  }
};

export const deletePhieuGiamGia = async (id) => {
  try {
    const response = await api.delete( baseURL +`/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Lỗi khi xóa phiếu giảm giá với ID ${id}:`, error);
    throw error.response?.data || new Error('Lỗi khi xóa phiếu giảm giá');
  }
};

export const getAllKhachHang = async () => {
  try {
    const response = await api.get(baseURL +'/khach-hang');
    return response.data;
  } catch (error) {
    console.error('Lỗi khi lấy danh sách khách hàng:', error);
    throw error.response?.data || new Error('Lỗi khi lấy danh sách khách hàng');
  }
};