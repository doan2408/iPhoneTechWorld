import api from "@/Service/LoginService/axiosInstance";


const baseURL = '/admin/khuyen-mai';


export const getAllKhuyenMai = async (params) => {
  try {
    const response = await api.get(baseURL, { params });
    return response.data;
  } catch (error) {
    console.error('Lỗi khi lấy danh sách khuyến mại:', error);
    throw error.response?.data || new Error('Lỗi khi lấy danh sách khuyến mại');
  }
};

export const getKhuyenMaiById = async (id) => {
  try {
    const response = await api.get(baseURL + `/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Lỗi khi lấy chi tiết khuyến mại với ID ${id}:`, error);
    throw error.response?.data || new Error('Lỗi khi lấy chi tiết khuyến mại');
  }
};

export const createKhuyenMai = async (data) => {
  try {
    const response = await api.post(baseURL, data);
    return response.data;
  } catch (error) {
    console.error('Lỗi khi thêm khuyến mại:', error);
    throw error.response?.data || new Error('Lỗi khi thêm khuyến mại');
  }
};

export const updateKhuyenMai = async (id, data) => {
  try {
    const response = await api.put(baseURL + `/${id}`, data);
    return response.data;
  } catch (error) {
    console.error(`Lỗi khi cập nhật khuyến mại với ID ${id}:`, error);
    throw error.response?.data || new Error('Lỗi khi cập nhật khuyến mại');
  }
};

export const deleteKhuyenMai = async (id) => {
  try {
    const response = await api.delete(baseURL + `/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Lỗi khi xóa khuyến mại với ID ${id}:`, error);
    throw error.response?.data || new Error('Lỗi khi xóa khuyến mại');
  }
};

export const getAllSanPham = async (params) => {
  try {
    const response = await api.get(baseURL + '/san-pham', { params });
    return response.data;
  } catch (error) {
    console.error('Lỗi khi lấy danh sách sản phẩm:', error);
    throw error.response?.data || new Error('Lỗi khi lấy danh sách sản phẩm');
  }
};

export const getSanPhamChiTietsBySanPhamIds = async (sanPhamIds) => {
  try {
    const response = await api.post(baseURL + '/san-pham-chi-tiet', sanPhamIds);
    return response.data;
  } catch (error) {
    console.error('Lỗi khi lấy danh sách sản phẩm chi tiết:', error);
    throw error.response?.data || new Error('Lỗi khi lấy danh sách sản phẩm chi tiết');
  }
};

export const getSanPhamChiTietByIdKhuyenMai = async (id) => {
  try {
    const response = await api.get(baseURL + '/san-pham-chi-tiet' + `/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Lỗi khi lấy chi tiết sản phẩm ID ${id}:`, error);
    throw error.response?.data || new Error('Lỗi khi lấy chi tiết sản phẩm');
  }
};

// Lấy danh sách khuyến mãi hiện tại của sản phẩm chi tiết
export const getKhuyenMai = async (sanPhamChiTietId) => {
  try {
    const response = await api.get(baseURL + `/san-pham-chi-tiet/${sanPhamChiTietId}/promotions`);
    return response.data;
  } catch (error) {
    console.error(`Lỗi lấy danh sách khuyến mãi hiện tại của sản phẩm chi tiết ${sanPhamChiTietId}:`, error);
    throw error.response?.data || new Error('Lỗi lấy danh sách khuyến mãi');
  }
};

// Xóa sản phẩm chi tiết khỏi danh sách khuyến mãi
export const removeProductFromPromotions = async (sanPhamChiTietId, idKhuyenMai) => {

  try {
    const response = await api.post(baseURL + `/remove-product`, {
      sanPhamChiTietId: sanPhamChiTietId,
      khuyenMaiId: idKhuyenMai
    });
    return response.data;
  } catch (error) {
    console.error(`Lỗi xóa sản phẩm chi tiết khỏi danh sách khuyến mãi ${sanPhamChiTietId}:`, error);
    throw error.response?.data || new Error('Lỗi xóa sản phẩm chi tiết khỏi danh sách khuyến mãi');
  }
};

export const nextDelay = async (id, type) => {
  try {
    const response = await api.get(baseURL +'/next-delay');
    return response;
  } catch (error) {
    console.error('Lỗi khi lấy thời gian update:', error);
    throw error.response?.data || new Error('Lỗi khi lấy thời gian update');
  }
};