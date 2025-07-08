import api from "@/Service/LoginService/axiosInstance";

const baseURL = '/gio-hang'; // Adjust to your backend URL

export const cartService = {
    
  async getCart (idKhachHang) {
    try {
      const response = await api.get(`${baseURL}/${idKhachHang}`);
      return response.data;
    } catch (error) {
      console.error('Lỗi lấy giỏ hàng:', error);
      throw error;
    }
  },

  async addToCart(idGioHangChiTiet) {
    try {
      const response = await api.post(`${baseURL}`, idGioHangChiTiet);
      return response.data;
    } catch (error) {
      console.error('Lỗi thêm sản phẩm vào giỏ hàng:', error);
      throw error;
    }
  },

  async updateQuantity(idGioHangChiTiet, soLuong) {
    try {
      const response = await api.put(`${baseURL}/${idGioHangChiTiet}`, null, {
        params: { soLuong }
      });
      return response.data;
    } catch (error) {
      console.error('Lỗi sửa số lượng sản phẩm:', error);
      throw error;
    }
  },

  async removeItem(idGioHangChiTiet) {
    try {
      await api.delete(`${baseURL}/remove/${idGioHangChiTiet}`);
    } catch (error) {
      console.error('Lỗi xóa sản phẩm khỏi giỏ hàng:', error);
      throw error;
    }
  },

  async clearCart(idKhachHang) {
    try {
      await api.delete(`${baseURL}/clear/${idKhachHang}`);
    } catch (error) {
      console.error('Lỗi xóa giỏ hàng:', error);
      throw error;
    }
  }
};