import api from "@/Service/LoginService/axiosInstance";

const baseURL = "/admin/staff";

export const getAllStaff = async ({
  page = 0,
  keyword = null,
  gioiTinh = null,
  trangThai = null
} = {}) => {
  try {
    const params = { page };

    if (keyword && keyword.trim() !== "") params.keyword = keyword.trim();
    if (gioiTinh !== null) params.gioiTinh = gioiTinh;
    if (trangThai !== null) params.trangThai = trangThai;

    const response = await api.get(`${baseURL}`, { params });
    return response.data;
  } catch (error) {
    console.error("Error:", error);
  }
};

export const detailStaff = async (id) => {
  try {
    const response = await api.get(`${baseURL}/${id}`);
    return response.data;
  } catch (error) {
    console.error("Có lỗi khi Detail Staff phía admin:", error);
    throw error.response?.data || "Lỗi lấy Staff";
  }
};

export const updateStaff = async (id, staffRequest) => {
  try {
    const response = await api.put(`${baseURL}/${id}`, staffRequest);
    return response.data;
  } catch (error) {
    if (error.response) {
      // Server trả về lỗi (status != 2xx)
      console.error("Lỗi phản hồi từ server:", error.response.data);
      throw error.response.data;
    } else if (error.request) {
      // Request đã gửi đi rồi nhưng không nhận được phản hồi từ server
      console.error("Không có phản hồi từ server:", error.request);
      throw "Không có phản hồi từ server";
    } else {
      // Lỗi xảy ra trong quá trình tạo request (cấu hình axios sai, v.v...)
      console.error("Lỗi khác:", error.message);
      throw error.message;
    }
  }
};

export const addStaff = async (staffRequest) => {
  try {
    const response = await api.post(`${baseURL}`, staffRequest);
    return response.data;
  } catch (error) {
    console.error(
      "Có lỗi khi tạo staff phía admin:",
      error.response ? error.response.data : error.message
    );
    throw error.response?.data || "Lỗi tạo Satff";
  }
};
