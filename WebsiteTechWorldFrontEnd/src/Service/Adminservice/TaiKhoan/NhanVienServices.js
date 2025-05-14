import axios from "axios";

const axiosInstance = axios.create({
    baseURL: "http://localhost:8080/admin/staff",
    withCredentials: true // ❗ Gửi cookie (JSESSIONID) qua CORS
});

export const getAllStaff = async(page = 0) => {
    try {
        const response = await axiosInstance.get('', {
            params: { page }
        });
        return response.data;
    } catch(error) {
        console.error('An error was thrown while loading the staff: ', error);
        throw error.response?.data || "Error getting staff"
    }
};