import api from "@/Service/LoginService/axiosInstance";

const baseURL = '/admin/khuyen-mai';

export const getAllKhuyenMai = async (params) => {
    try {
        const response = await api.get(baseURL, { params });
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Lỗi khi lấy danh sách khuyến mãi');
    }
};

export const getKhuyenMaiById = async (id) => {
    try {
        const response = await api.get(`${baseURL}/${id}`);
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Lỗi khi lấy chi tiết khuyến mãi');
    }
};

export const createKhuyenMai = async (data) => {
    try {
        const response = await api.post(baseURL, data);
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Lỗi khi thêm khuyến mãi');
    }
};

export const updateKhuyenMai = async (id, data) => {
    try {
        const response = await api.put(`${baseURL}/${id}`, data);
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Lỗi khi cập nhật khuyến mãi');
    }
};

export const deleteKhuyenMai = async (id) => {
    try {
        await api.delete(`${baseURL}/${id}`);
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Lỗi khi xóa khuyến mãi');
    }
};

export const getAllSanPham = async (params) => {
    try {
        const response = await api.get(`${baseURL}/san-pham`, { params });
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Lỗi khi lấy danh sách sản phẩm');
    }
};

export const getSanPhamChiTietsBySanPhamIds = async (sanPhamIds) => {
    try {
        const response = await api.post(`${baseURL}/san-pham-chi-tiet`, sanPhamIds);
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Lỗi khi lấy danh sách sản phẩm chi tiết');
    }
};

export const getSanPhamChiTietByIdKhuyenMai = async (id) => {
    try {
        const response = await api.get(`${baseURL}/san-pham-chi-tiet/${id}`);
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Lỗi khi lấy danh sách sản phẩm chi tiết');
    }
};

export const getKhuyenMai = async (sanPhamChiTietId) => {
    try {
        const response = await api.get(`${baseURL}/san-pham-chi-tiet/${sanPhamChiTietId}/promotions`);
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Lỗi khi lấy danh sách khuyến mãi');
    }
};

export const removeProductFromPromotions = async (sanPhamChiTietId, khuyenMaiId) => {
    try {
        const response = await api.post(`${baseURL}/remove-product`, {
            sanPhamChiTietId,
            khuyenMaiId,
        });
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Lỗi khi xóa sản phẩm chi tiết khỏi khuyến mãi');
    }
};

export const nextDelay = async () => {
    try {
        const response = await api.get(`${baseURL}/next-delay`);
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Lỗi khi lấy thời gian cập nhật');
    }
};

export const nextDelayClient = async () => {
    try {
        const response = await api.get(`/client/khuyen-mai/next-delay`);
        return response.data;
    } catch (error) {
        throw new Error(error.response?.data?.message || 'Lỗi khi lấy thời gian cập nhật');
    }
};