import api from "@/Service/LoginService/axiosInstance";

const baseUrl = "/client/voucher"

export const getAllVoucher = async(params) => {
    try {
        const response = await api.get(`${baseUrl}`, {params});
        return response.data;
    }
    catch(err) {
        console.error("Errors when getting vouchers");
        throw err.response?.data || new Error("Lỗi khi lấy danh sách phiếu giảm giá")
    }
}