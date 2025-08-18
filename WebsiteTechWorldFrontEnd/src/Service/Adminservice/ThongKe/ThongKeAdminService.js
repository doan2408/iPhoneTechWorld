import api from "@/Service/LoginService/axiosInstance";

const baseURL = '/admin/thong-ke'
export const getDashboardAdmin = async (startDate, endDate) => {
  try {
    const response = await api.get(`${baseURL}/tong-quan`, {
      params: { startDate, endDate }
    })
    return response.data
  } catch (error) {
    console.error('Có lỗi khi lấy thống kê tổng quan:', error)
    throw error.response?.data || 'Lỗi lấy thống kê tổng quan'
  }
};

export const getDoanhThuTheoThang = async (params) => {
  try {
    const response = await api.get(`${baseURL}` + '/doanh-thu-theo-thang', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy thống kê tổng quan:', error);
    throw error.response?.data || 'Lỗi lấy thống kê tổng quan';
  }
};

export const getSanPhamBanChay = async (params) => {
  try {
    const response = await api.get(`${baseURL}` + '/san-pham-ban-chay', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy thống kê tổng quan:', error);
    throw error.response?.data || 'Lỗi lấy thống kê tổng quan';
  }
};

export const getKhachHangTrungThanh = async (params) => {
  try {
    const response = await api.get(`${baseURL}` + '/khach-hang-trung-thanh', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy danh sách khách hàng trung thành:', error);
    throw error.response?.data || 'Lỗi lấy danh sách khách hàng trung thành';
  }
};

export const getSanPhamSapHetHang = async (params) => {
  try {
    const response = await api.get(`${baseURL}` + '/san-pham-sap-het-hang', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy danh sách khách hàng trung thành:', error);
    throw error.response?.data || 'Lỗi lấy danh sách khách hàng trung thành';
  }
};

export const getTopSanPhamBanChay = async (startDate, endDate) => {
  try {
    const response = await api.get(`${baseURL}` + '/top-san-pham-ban-chay', { startDate, endDate });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy thống kê tổng quan:', error);
    throw error.response?.data || 'Lỗi lấy thống kê tổng quan';
  }
};

export const getDonHangHuy = async (params) => {
  try {
    const response = await api.get(`${baseURL}` + '/don-huy-theo-thang', { params });
    return response.data;
  } catch (error) {
    console.error('Có lỗi khi lấy thống kê tổng quan:', error);
    throw error.response?.data || 'Lỗi lấy thống kê tổng quan';
  }
};
// Xuất Excel tổng quan Dashboard
export const exportDashboardExcel = async (startDate, endDate) => {
  try {
    const response = await api.get(`${baseURL}/dashboard/export-excel`, {
      params: { startDate, endDate },
      responseType: 'blob' 
    });

    // Tạo URL từ Blob để tải xuống
    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'dashboard-tong-quan.xlsx');
    document.body.appendChild(link);
    link.click();
    link.remove();
  } catch (error) {
    console.error('Có lỗi khi xuất Excel tổng quan:', error);
    throw error.response?.data || 'Lỗi xuất Excel tổng quan';
  }
};
//xuất doanh thu 
export const exportDoanhThuTheoThangExcel = async () => {
  try {
    const response = await api.get(`${baseURL}/doanh-thu-theo-thang/export-excel`, {
      responseType: 'blob' // Quan trọng để tải file Excel
    });

    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'doanh-thu-theo-thang.xlsx');
    document.body.appendChild(link);
    link.click();
  } catch (error) {
    console.error('Có lỗi khi export doanh thu theo tháng:', error);
    throw error.response?.data || 'Lỗi export doanh thu theo tháng';
  }
}
// Xuất danh sách khách hàng trung thành
export const exportKhachHangTrungThanhExcel = async () => {
  try {
    const response = await api.get(`${baseURL}/khach-hang-trung-thanh/export-excel`, {
      responseType: 'blob' // Quan trọng để tải file Excel
    });

    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', 'khach-hang-trung-thanh.xlsx');
    document.body.appendChild(link);
    link.click();
    link.remove();
  } catch (error) {
    console.error('Có lỗi khi export khách hàng trung thành:', error);
    throw error.response?.data || 'Lỗi export khách hàng trung thành';
  }
};


