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
};