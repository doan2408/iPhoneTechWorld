import api from "@/Service/LoginService/axiosInstance";

const baseURL= '/admin/thong-ke' 
export const getDashboardAdmin = async (params) => {
  try {
    const response = await api.get(`${baseURL}`+'/tong-quan', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy thống kê tổng quan:', error);
    throw error.response?.data || 'Lỗi lấy thống kê tổng quan';
  }
};

export const getDoanhThuTheoThang = async (params) => {
  try {
    const response = await api.get(`${baseURL}`+'/doanh-thu-theo-thang', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy thống kê tổng quan:', error);
    throw error.response?.data || 'Lỗi lấy thống kê tổng quan';
  }
};

export const getSanPhamBanChay = async (params) => {
  try {
    const response = await api.get(`${baseURL}`+'/san-pham-ban-chay', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy thống kê tổng quan:', error);
    throw error.response?.data || 'Lỗi lấy thống kê tổng quan';
  }
};

export const getKhachHangTrungThanh = async (params) => {
  try {
    const response = await api.get(`${baseURL}`+'/khach-hang-trung-thanh', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy danh sách khách hàng trung thành:', error);
    throw error.response?.data || 'Lỗi lấy danh sách khách hàng trung thành';
  }
};

export const getSanPhamSapHetHang = async (params) => {
  try {
    const response = await api.get(`${baseURL}`+'/san-pham-sap-het-hang', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy danh sách khách hàng trung thành:', error);
    throw error.response?.data || 'Lỗi lấy danh sách khách hàng trung thành';
  }
};

export const getTopSanPhamBanChay = async (startDate, endDate) => {
  try {
    const response = await api.get(`${baseURL}`+'/top-san-pham-ban-chay', { startDate, endDate });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy thống kê tổng quan:', error);
    throw error.response?.data || 'Lỗi lấy thống kê tổng quan';
  }
};

export const getDonHangHuy = async (params) => {
  try {
    const response = await api.get(`${baseURL}`+'/don-huy-theo-thang', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy thống kê tổng quan:', error);
    throw error.response?.data || 'Lỗi lấy thống kê tổng quan';
  }
};

