import api from '@/Service/LoginService/axiosInstance';

const BASE_URL = "/admin/danh-gia-san-pham";

export const DanhGiaSanPhamAdminService = {
    
   async layTatCaDanhGiaAdmin(page = 0, size = 10, soSao = null, trangThai = null) {
    const response = await api.get(`${BASE_URL}`, {
        params: {
            page: page,
            size: size,
            sort: 'ngayDanhGia,desc', // Sắp xếp theo ngày đánh giá mới nhất
            soSao: soSao, // Thêm tham số lọc số sao
            trangThai: trangThai // Thêm tham số lọc trạng thái
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


