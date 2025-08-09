import api from '@/Service/LoginService/axiosInstance';

const BASE_URL = "/admin/phan-hoi-danh-gia";

export const PhanHoiDanhGiaAdminService = {
    

    async layPhanHoiDanhGia(idDanhGia) {
        try {
            const response = await api.get(`/admin/phan-hoi-danh-gia/danh-gia/${idDanhGia}`);
            console.log('Dữ liệu phản hồi:', response.data);
            return response.data;
        } catch (error) {
            console.error(`Lỗi khi lấy phản hồi đánh giá với ID ${idDanhGia}:`, error);
            throw error;
        }
    },

    // Phản hồi đánh giá
    async phanHoiDanhGia(idDanhGia, data) {
        try {
           const response = await api.post(`/admin/phan-hoi-danh-gia/danh-gia/${idDanhGia}/phan-hoi`, {
                noiDungPhanHoi: data.noiDungPhanHoi,
                idNhanVien: data.idNhanVien,
            });
            console.log("hahah phan hoi", response.data)
            return response.data;
        } catch (error) {
            console.error(`Lỗi khi phản hồi đánh giá với ID ${idDanhGia}:`, error);
            throw error;
        }
    },

      async capNhatPhanHoi(idPhanHoi, data) {
        try {
        const response = await api.put(`${BASE_URL}/${idPhanHoi}`, {
            noiDungPhanHoi: data.noiDungPhanHoi,
            idNhanVien: data.idNhanVien
        });
        return response.data;
        } catch (error) {
        console.error(`Lỗi khi cập nhật phản hồi ID ${idPhanHoi}:`, error);
        throw error;
        }
    },


};


