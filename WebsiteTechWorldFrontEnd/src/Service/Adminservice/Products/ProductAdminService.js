import axios from 'axios';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/admin',
  withCredentials: true // ❗ Gửi cookie (JSESSIONID) qua CORS
});


// Hàm chung lấy dữ liệu từ API
const fetchData = async (url) => {
  try {
    const response = await axiosInstance.get(url);
    return response.data.content; // trả về data từ server
  } catch (error) {
    console.error(`Lỗi lấy dữ liệu từ ${url}:`, error);
    throw error.response?.data || `Lỗi lấy dữ liệu từ ${url}`;
  }
}

export const getAllSanPham = (page = 0) => fetchData(`/product?page=${page}`);
export const detailSanPham = (id) => fetchData(`/product/${id}`);

export const getAllMauSac = () => fetchData('/mauSac');
export const getAllRam = () => fetchData('/ram');
export const getAllRom = () => fetchData('/rom');
export const getAllManHinh = () => fetchData('/manHinh');
export const getAllHeDieuHanh = () => fetchData('/heDieuHanh');
export const getAllPin = () => fetchData('/pin');
export const getAllCpu = () => fetchData('/cpu');
export const getAllCameraTruoc = () => fetchData('/cameraTruoc');
export const getAllCameraSau = () => fetchData('/cameraSau');
export const getAllXuatXu = () => fetchData('/xuatXu');
export const getAllLoai = () => fetchData('/loai');
export const getAllNhaCungCap = () => fetchData('/nhaCungCap');
