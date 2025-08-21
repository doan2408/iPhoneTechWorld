import api from "@/Service/LoginService/axiosInstance";


const baseURL = '/admin/bao-hanh';


export const getAllBaoHanh = async (params) => {
  try {
    const response = await api.get(baseURL, { params });
    return response.data;
  } catch (error) {
    console.error('Lỗi khi lấy danh sách bảo hành:', error);
    throw error.response?.data || new Error('Lỗi khi lấy danh sách bảo hành');
  }
};