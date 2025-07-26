import api from '@/Service/LoginService/axiosInstance';

const BASE_URL = "/admin/danh-gia-san-pham";

export const DanhGiaSanPhamAdminService = {
    async layTatCaDanhGiaAdmin(page = 0, size = 10) {
        const response = await api.get(`${BASE_URL}`, {
            params: {
                page: page,
                size: size,
                sort: 'ngayDanhGia,desc'  // Sắp xếp theo ngày đánh giá mới nhất
            }
        });
        return response.data;
    },

      // Phê duyệt đánh giá
    async pheDuyetDanhGia(id) {
        try {
            const response = await api.put(`${BASE_URL}/${id}/phe-duyet`);
            return response.data;
        } catch (error) {
            console.error(`Lỗi khi phê duyệt đánh giá với ID ${id}:`, error);
            throw error;
        }
    },

    // Từ chối đánh giá
    async tuChoiDanhGia(id) {
        try {
            const response = await api.put(`${BASE_URL}/${id}/tu-choi`);
            return response.data;
        } catch (error) {
            console.error(`Lỗi khi từ chối đánh giá với ID ${id}:`, error);
            throw error;
        }
    },

    // Xóa đánh giá
    async xoaDanhGia(id) {
        try {
            const response = await api.delete(`${BASE_URL}/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Lỗi khi xóa đánh giá với ID ${id}:`, error);
            throw error;
        }
    },

    // Phản hồi đánh giá
    async phanHoiDanhGia(id, data) {
        try {
            const response = await api.post(`${BASE_URL}/${id}/phan-hoi`, {
                noiDungPhanHoi: data.noiDungPhanHoi
            });
            return response.data;
        } catch (error) {
            console.error(`Lỗi khi phản hồi đánh giá với ID ${id}:`, error);
            throw error;
        }
    }
};


