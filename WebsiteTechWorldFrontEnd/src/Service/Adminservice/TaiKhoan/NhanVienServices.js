import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080/admin/staff",
  withCredentials: true, // ❗ Gửi cookie (JSESSIONID) qua CORS
});

export const getAllStaff = async (page = 0) => {
  try {
    const response = await axiosInstance.get("", {
      params: { page },
    });
    return response.data;
  } catch (error) {
    console.error("An error was thrown while loading the staff of admin: ", error);
    throw error.response?.data || "Error getting staff";
  }
};

export const detailStaff = async (id) => {
  try {
    const response = await axiosInstance.get(`/${id}`);
    return response.data;
  } catch (error) {
    console.error("Có lỗi khi Detail Staff phía admin:", error);
    throw error.response?.data || "Lỗi lấy Staff";
  }
};

export const updateStaff = async(id, staffRequest) => {
    try {
        const response = await axiosInstance.put(`/${id}`, staffRequest);
        return response.date;
    }
    catch(error) {
     console.error("Có lỗi khi update staff phía admin:", error);
    throw error.response?.data || "Lỗi update Satff";
  }
}

export const addStaff = async(staffRequest) => {
    try {
        const response = await axiosInstance.post(``, staffRequest);
        return response.data;
    }
    catch(error) {
     console.error("Có lỗi khi tạo staff phía admin:", error.response ? error.response.data : error.message);
    throw error.response?.data || "Lỗi tạo Satff";
  }
}
