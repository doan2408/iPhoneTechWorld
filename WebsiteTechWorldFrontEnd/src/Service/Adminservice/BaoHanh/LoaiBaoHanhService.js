import api from "@/Service/LoginService/axiosInstance";


const baseURL = '/admin/loai-bao-hanh';


export const getAllLoaiBaoHanh = async (params) => {
  try {
    const response = await api.get(baseURL, { params });
    return response.data;
  } catch (error) {
    console.error('Lỗi khi lấy danh sách loại bảo hành:', error);
    throw error.response?.data || new Error('Lỗi khi lấy danh loại sách bảo hành');
  }
};