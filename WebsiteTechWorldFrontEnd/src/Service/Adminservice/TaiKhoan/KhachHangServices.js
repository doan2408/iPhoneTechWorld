import axios from "axios";

const axiosInstance = axios.create({
    baseURL: "http://localhost:8080/admin/client",
    withCredentials: true, 
});

export const getAllClient = async(page =0) => {
    try {
        const response = await axiosInstance.get('', {
            params: { page }, // <- Đây là shorthand của { page: page }
        })
    } catch( err ){
        console.error("An error was thrown while loading the client of admin: ", err);
        throw err.response?.data || "Error getting client"; 
    }
}

export const detailClient = async(page=0) => {
    try {
        const response = await axios.get(`${id}`);
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