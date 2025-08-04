import api from '@/Service/LoginService/axiosInstance';

const BASE_URL = "/client/media-danh-gia";

export const MediaDanhGiaClientService = {
  async uploadMedia(file, idDanhGia) {
    if (!file || !idDanhGia) {
      console.error("Thiếu file hoặc idDanhGia:", { file, idDanhGia });
      throw new Error("File và idDanhGia là bắt buộc");
    }

    const formData = new FormData();
    formData.append("file", file);
    formData.append("idDanhGia", idDanhGia);

    console.log("🚀 Gửi FormData:", { file: file.name, idDanhGia });

    try {
      const response = await api.post(`${BASE_URL}/upload`, formData);
      return response.data;
    } catch (error) {
      console.error("Lỗi khi upload:", error.response?.data || error.message);
      throw error;
    }
  },

  async getAll() {
    const response = await api.get(BASE_URL);
    return response.data; // Trả về danh sách MediaDanhGiaClientResponse
  },

  async getById(id) {
    const response = await api.get(`${BASE_URL}/${id}`);
    return response.data; // Trả về MediaDanhGiaClientResponse
  },

  async update(id, request) {
    const response = await api.put(`${BASE_URL}/${id}`, request);
    return response.data; // Trả về MediaDanhGiaClientResponse
  },

  async delete(id) {
    const response = await api.delete(`${BASE_URL}/${id}`);
    return response.data; // Trả về void hoặc thông tin xóa
  },
};