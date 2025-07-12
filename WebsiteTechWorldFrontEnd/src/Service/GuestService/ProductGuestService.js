import api from "../LoginService/axiosInstance";

const baseURL = "/home";

const fetchData = async (url) => {
  try {
    const response = await api.get(`${baseURL}${url}`);
    return response.data; // trả về data từ server
  } catch (error) {
    console.error(`Lỗi lấy dữ liệu từ ${baseURL} + ${url}:`, error);
    throw error.response?.data || `Lỗi lấy dữ liệu từ ${baseURL} + ${url}`;
  }
};

export const getAllSanPham = async ({
  page = 0,
  tenSanPham = null,
  idLoai = null,
  giaMin = null,
  giaMax = null,
  sort = null,
} = {}) => {
  const params = { page };

  try {
    if (tenSanPham && tenSanPham.trim() !== "") params.tenSanPham = tenSanPham.trim();
    if (idLoai !== null) params.idLoai = idLoai;
    if (giaMin !== null) params.giaMin = giaMin;
    if (giaMax !== null) params.giaMax = giaMax;
    if (sort && sort.trim() !== "") params.sort = sort.trim();

    const response = await api.get(`${baseURL}`, { params });
    return response.data;
  } catch (e) {
    console.log("error:", e);
  }
};

export const detailSanPham = async (id) => {
  try {
    const response = await api.get(`${baseURL}/${id}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || "An error occurred while getting the product";
  }
};

export const getChiTietBienThe = async (idSp, idMau, idRom) => {
  try {
    const response = await api.get(
      `${baseURL}/${idSp}/mau/${idMau}/rom/${idRom}`
    );
    return response.data;
  } catch (error) {
    throw (
      error.response?.data || "An error occurred while getting product variant"
    );
  }
};

export const getThongSo = async (idsp, idRom) => {
  try {
    const response = await api.get(`${baseURL}/thongSo/${idsp}/rom/${idRom}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || "Unable to fetch the product specifications";
  }
};

export const getListAnhByMau = async (idSp, idMau) => {
  try {
    const response = await api.get(`${baseURL}/anh/${idSp}/mau/${idMau}`);
    return response.data;
  } catch (error) {
    throw error.response?.data || "Did not find images";
  }
};

export const getLoai = async() => {
  try {
    const response = await api.get(`${baseURL}/loai`)
    return response.data;
  }
  catch(error) {
    throw error.response?.data || "Can't get list of loai"
  }
};
