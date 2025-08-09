// src/Service/ClientService/DanhGia/DanhGiaSanPhamClientService.js
import api from '@/Service/LoginService/axiosInstance';

const BASE_URL = "/client/danh-gia-san-pham";

export const DanhGiaSanPhamClientService = {
  async taoMoiDanhGia(request) {
    const response = await api.post(BASE_URL, request);
    return response.data;
  },

  async capNhatDanhGia(id, request) {
    const response = await api.put(`${BASE_URL}/${id}`, request);
    return response.data;
  },

  async xoaDanhGia(id) {
    const response = await api.delete(`${BASE_URL}/${id}`);
    return response.data;
  },

  async layDanhGiaTheoId(id) {
    const response = await api.get(`${BASE_URL}/${id}`);
    return response.data;
  },

  async layTatCaDanhGia() {
    const response = await api.get(BASE_URL);
    return response.data;
  },

  async layDanhGiaTheoSanPham (idSanPhamChiTiet, page = 0, size = 10, soSao = null, hasMedia = null, idKhachHang) {
    const response = await api.get(`${BASE_URL}/san-pham-chi-tiet/${idSanPhamChiTiet}`, {
      params: {
        page,
        size,
        sortBy: "ngayDanhGia",
        sortDir: "desc",
        soSao,
        hasMedia,
        idKhachHang,
      },
    });

    console.log(response, "cường nèeee")
    return response.data;
  },

  async layDanhGiaTheoKhachHang(idKhachHang) {
    const response = await api.get(`${BASE_URL}/khach-hang/${idKhachHang}`);
    return response.data;
  },

  async layDanhGiaTheoTrangThai(trangThai) {
    const response = await api.get(`${BASE_URL}/trang-thai/${trangThai}`);
    return response.data;
  },

  async layDanhGiaTheoSoSao(soSao) {
    const response = await api.get(`${BASE_URL}/so-sao/${soSao}`);
    return response.data;
  },

  async layDanhGiaTheoKhoangThoiGian(tuNgay, denNgay) {
    const response = await api.get(`${BASE_URL}/theo-thoi-gian`, {
      params: { tuNgay, denNgay },
    });
    return response.data;
  },

  async tinhDiemTrungBinhSanPham(idSanPhamChiTiet) {
    const response = await api.get(`${BASE_URL}/thong-ke/diem-trung-binh/${idSanPhamChiTiet}`);
    console.log("idSanPhamChiTiet", response)
    return response.data;
  },

  async tinhDiemTrungBinhSanPhamSpct(idSanPhamChiTiet) {
    const response = await api.get(`${BASE_URL}/thong-ke-spct/diem-trung-binh/${idSanPhamChiTiet}`);
    console.log("idSanPhamChiTiet", response)
    return response.data;
  },

  async thongKeSoSaoSanPham(idSanPhamChiTiet) {
    const response = await api.get(`${BASE_URL}/thong-ke/so-sao/${idSanPhamChiTiet}`);
    return response.data;
  },

  async layDanhGiaCoPhanhoi() {
    const response = await api.get(`${BASE_URL}/co-phan-hoi`);
    return response.data;
  },

  async layDanhGiaTheoTrang(page, size, sortBy, sortDir) {
    const response = await api.get(`${BASE_URL}/phan-trang`, {
      params: { page, size, sortBy, sortDir },
    });
    return response.data;
  },

  async uploadImage(file) {
    const formData = new FormData();
    formData.append("file", file);
    const response = await api.post("/upload", formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
    return response.data.url; // Adjust based on your upload endpoint response
  },
};