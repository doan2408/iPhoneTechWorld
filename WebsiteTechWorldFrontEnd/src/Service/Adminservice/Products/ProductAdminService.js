import axios from 'axios';
import { da } from 'element-plus/es/locales.mjs';

const axiosInstance = axios.create({
  baseURL: 'http://localhost:8080/admin',
  withCredentials: true // ❗ Gửi cookie (JSESSIONID) qua CORS
});


// Hàm chung lấy dữ liệu từ API
const fetchData = async (url) => {
  try {
    const response = await axiosInstance.get(url);
    return response.data; // trả về data từ server
  } catch (error) {
    console.error(`Lỗi lấy dữ liệu từ ${url}:`, error);
    throw error.response?.data || `Lỗi lấy dữ liệu từ ${url}`;
  }
}

const fetchDataList = async (url) => {
  try {
    const response = await axiosInstance.get(url);
    return response.data; // trả về data từ server
  } catch (error) {
    console.error(`Lỗi lấy dữ liệu từ ${url}:`, error);
    throw error.response?.data || `Lỗi lấy dữ liệu từ ${url}`;
  }
}


const postData = async (url, data) => {
  try {
    const response = await axiosInstance.post(url, data);
    return response.data; // hoặc response.data.content nếu backend trả về theo kiểu đó
  } catch (error) {
    console.error(`Lỗi gửi dữ liệu tới ${url}:`, error);
    throw error.response?.data || `Lỗi gửi dữ liệu tới ${url}`;
  }
};


export const getAllSanPham = (page = 0, size = 5) => fetchData(`/product?page=${page}&size=${size}`);
export const postSanPham = (data) => postData('/admin/product', data);

export const detailSanPham = (id) => fetchData(`/product/${id}`);


export const getAllXuatXuPage = (page = 0, size = 5) => fetchData(`/xuatXu?page=${page}&size=${size}`);
export const getAllPinPage = (page = 0, size = 5) => fetchData(`/pin?page=${page}&size=${size}`);
export const getAllRamPage = (page = 0, size = 5) => fetchData(`/ram?page=${page}&size=${size}`);
export const getAllRomPage = (page = 0, size = 5) => fetchData(`/rom?page=${page}&size=${size}`);
export const getAllNhaCungCapPage = (page = 0, size = 5) => fetchData(`/nhaCungCap?page=${page}&size=${size}`);
export const getAllLoaiPage = (page = 0, size = 5) => fetchData(`/loai?page=${page}&size=${size}`);
export const getAllManHinhPage = (page = 0, size = 5) => fetchData(`/manHinh?page=${page}&size=${size}`);
export const getAllMauSacPage = (page = 0, size = 5) => fetchData(`/mauSac?page=${page}&size=${size}`);
export const getAllIemiPage = (page = 0, size = 5) => fetchData(`/imei?page=${page}&size=${size}`);
export const getAllHeDieuHanhPage = (page = 0, size = 5) => fetchData(`/heDieuHanh?page=${page}&size=${size}`);
export const getAllCpuPage = (page = 0, size = 5) => fetchData(`/cpu?page=${page}&size=${size}`);
export const getAllCameraTruocPage = (page = 0, size = 5) => fetchData(`/cameraTruoc?page=${page}&size=${size}`);
export const getAllCameraSauPage = (page = 0, size = 5) => fetchData(`/cameraTruoc?page=${page}&size=${size}`);


export const getAllNhaCungCapList = () => fetchDataList('/nhaCungCap/listNCC');
export const postNhaCungCapList = (data) => postData('/nhaCungCap/quick-create', data)

export const getAllXuatXuList = () => fetchDataList('/xuatXu/listXuatXu');
export const getAllRomList = () => fetchDataList('/rom/listRom');
export const getAllRamList = () => fetchDataList('/ram/listRam');
export const getAllPinList = () => fetchDataList('/pin/listPin');
export const getAllMauSacList = () => fetchDataList('/mauSac/listMauSac');
export const getAllManHinhList = () => fetchDataList('/manHinh/listManHinh');
export const getAllLoaiList = () => fetchDataList('/loai/listLoai');
export const getAllHinhAnhList = () => fetchDataList('/hinhAnh/listHinhAnh');
export const getAllHDHList = () => fetchDataList('/heDieuHanh/listHDH');
export const getAllCpuList = () => fetchDataList('/cpu/listCpu');
export const getAllCameraTruocList = () => fetchDataList('/cameraTruoc/listCameraTruoc');
export const getAllCameraSauList = () => fetchDataList('/cameraSau/listCameraSau');
