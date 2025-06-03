import axios from "axios";
import { da } from "element-plus/es/locales.mjs";

const axiosInstance = axios.create({
    baseURL: "http://localhost:8080/admin/client",
    withCredentials: true, 
});

export const getAllClient = async(page =0) => {
    try {
        const response = await axiosInstance.get('', {
            params: { page }, // <- Đây là shorthand của { page: page }
        })
        return response.data;
    } catch( err ){
        console.error("An error was thrown while loading the client of admin: ", err);
        throw err.response?.data || "Error getting client"; 
    }
}

export const detailClient = async(id) => {
    try {
        const response = await axiosInstance.get(`/${id}`);
        return response.data;
    }catch (err) {
        console.error("Có lỗi khi Detail Client phía admin:", error);
        throw error.response?.data || "Lỗi lấy Client";
    }
}

export const updateClient = async(id, clientRequest) => {
    try {
        const response = await axiosInstance.put(`/${id}`, clientRequest);
        return response.date;
    }
    catch(error) {
     console.error("Có lỗi khi update client phía admin:", error);
    throw error.response?.data || "Lỗi update Client";
  }
}

export const addClient = async(clientRequest) => {
    try {
        const response = await axiosInstance.post(``, clientRequest);
        return response.data;
    }
    catch(error) {
     console.error("Có lỗi khi tạo client phía admin:", error.response ? error.response.data : error.message);
    throw error.response?.data || "Lỗi tạo client";
  }
}

//xem các địa chỉ của 1 khách
export const getAdressesClient = async(id) => {
    try {
        const response = await axiosInstance.get(`/addresses/${id}`);
        return response.data;
    }catch(err) {
        console.log("An errors was thrown while loading the addresses of client in admin: ", err);
        throw err.response?.data || "Error getting addresses"
    }
}

//xem từng địa chỉ của khách hàng (trang chỉnh sửa)
export const getAdress = async(id) => {
    try {
        const response = await axiosInstance.get(`/address/${id}`);
        return response.data;
    }catch(err) {
        console.log("An errors was thrown while loading the address of client in admin: ", err);
        throw err.response?.data || "Error getting addresses"
    }
}

//update 1 địa chỉ
export const updateAddress = async(id, addressRequest) => {
    try {
        const response = await axiosInstance.put(`/address/${id}`, addressRequest);
        return response.data;
    }catch(err) {
        console.log("An errors was thrown while loading the address of client in admin: ", err);
        throw err.response?.data || "Error getting addresses"
    }
}