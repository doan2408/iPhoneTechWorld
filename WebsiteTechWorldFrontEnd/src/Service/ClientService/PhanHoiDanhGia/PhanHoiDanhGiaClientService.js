import api from '@/Service/LoginService/axiosInstance';

const BASE_URL = "/client/phan-hoi";

export const PhanHoiDanhGiaClientService = {
    async layPhanHoiTheoDanhGia(idDanhGia) {
        try {
        const response = await api.get(`${BASE_URL}/${idDanhGia}/phan-hoi`);
        return response.data;
        } catch (error) {
        throw error;
        }
    },

};