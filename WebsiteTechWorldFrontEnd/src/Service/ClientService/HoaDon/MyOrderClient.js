import api from "@/Service/LoginService/axiosInstance";

const baseURL = "/client/my-order"

export const getMyOrder = async () => {
    return  await api.get(baseURL);
};  
export const findIdHoaDonByMVD = async (maVanDon) => {
    return await api.get(baseURL +'/'+ maVanDon);
};  