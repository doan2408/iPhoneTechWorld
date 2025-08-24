import api from "@/Service/LoginService/axiosInstance";

const baseURL = '/admin/loai-bao-hanh';

// using for dropdown
export const loaiBaoHanhList = async(idImeiDaBan) => {
  try {
    const response = await api.get(`${baseURL}/list/${idImeiDaBan}`);
    return response.data;
  }
  catch(err) {
    throw err.response?.data || new Error('Lỗi khi lấy danh sách loại bảo hành')
  }
}

export const getAllLoaiBaoHanh = async (params) => {
  try {
    const response = await api.get(baseURL, { params });
    return response.data;
  } catch (error) {
    console.error('Lỗi khi lấy danh sách loại bảo hành:', error);
    throw error.response?.data || new Error('Lỗi khi lấy danh sách loại bảo hành');
  }
};

export const getLoaiBaoHanhById = async (id) => {
  try {
    const response = await api.get(`${baseURL}/${id}`);
    return response.data;
  } catch (error) {
    console.error('Lỗi khi lấy chi tiết loại bảo hành:', error);
    throw error.response?.data || new Error('Lỗi khi lấy chi tiết loại bảo hành'); 
  }
};

export const addWarrantyType = async (request) => {
  try {
    const response = await api.post(baseURL, request);
    return response.data;
  } catch (error) {
    console.error("Lỗi khi thêm loại bảo hành:", error);
    throw error.response?.data || new Error('Lỗi khi thêm loại bảo hành');
  }
};

export const updateWarrantyType = async (id, request) => {
  try {
    const response = await api.put(`${baseURL}/${id}`, request);
    return response.data;
  } catch (error) {
    console.error("Lỗi khi cập nhật loại bảo hành:", error);
    throw error.response?.data || new Error('Lỗi khi cập nhật loại bảo hành');
  }
};

// export const deleteWarrantyType = async (id) => {
//   try {
//     const response = await api.delete(`${baseURL}/${id}`);
//     return response.data;
//   } catch (error) {
//     console.error("Lỗi khi xóa loại bảo hành:", error);
//     throw error.response?.data || new Error('Lỗi khi xóa loại bảo hành');
//   }
// };