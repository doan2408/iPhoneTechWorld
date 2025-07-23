import api from "@/Service/LoginService/axiosInstance";

const baseURL = "/client/my-order"

export const getMyOrder = async () => {
    return  await api.get(baseURL);
};  
export const findIdHoaDonByMVD = async (maVanDon) => {
    return await api.get(baseURL +'/mvd/'+ maVanDon);
};  

export const hoaDonDetail = (id) => {
    const url = baseURL + '/' + id;
    return api.get(url)
}

export const loadPaymentMethod = () => {
    return api.get('/client/payment-methods');
}