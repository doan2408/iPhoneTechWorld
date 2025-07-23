import { getAllPinList } from "@/Service/Adminservice/Products/ProductAdminService";
import api from "@/Service/LoginService/axiosInstance";

const baseURL = "/client/hang";


export const getListHang = async() => {
    try {
        const response = await api.get(`${baseURL}/listHang`);
        return response.data;
    }
    catch (err) {
        throw err.response?.data  || "Cannot getting rank of customer"
    }
}

export const getHangThanhVien =  async() => {
    try {
        const response = await api.get(`${baseURL}`);
        return response.data;
    }
    catch(err) {
        throw err.response?.data || "Throw errors when getting rank of client";
    }
}