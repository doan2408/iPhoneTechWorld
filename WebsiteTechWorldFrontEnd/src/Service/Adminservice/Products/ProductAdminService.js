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

// const fetchDataList = async (url) => {
//   try {
//     const response = await axiosInstance.get(url);
//     return response.data; // trả về data từ server
//   } catch (error) {
//     console.error(`Lỗi lấy dữ liệu từ ${url}:`, error);
//     throw error.response?.data || `Lỗi lấy dữ liệu từ ${url}`;
//   }
// }

const postData = async (url, data) => {
  try {
    const response = await axiosInstance.post(url, data, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
    return response.data;
  } catch (error) {
    console.error(`Lỗi gửi dữ liệu tới ${url}:`, error);
    throw error.response?.data || `Lỗi gửi dữ liệu tới ${url}`;
  }
};


// const postDataSpct = async (url, data) => {
//   try {
//     const response = await axiosInstance.post(url, data, {
//       headers: {
//         'Content-Type': 'application/json',
//       },
//     });
//     return response.data; // hoặc response.data.content tùy backend
//   } catch (error) {
//     console.error(`Lỗi gửi dữ liệu tới ${url}:`, error);
//     throw error.response?.data || `Lỗi gửi dữ liệu tới ${url}`;
//   }
// };

const putData = async (url, data) => {
  try {
    const response = await axiosInstance.put(url, data);
    return response.data; // hoặc response.data.content nếu backend trả về theo kiểu đó
  } catch (error) {
    console.error(`Lỗi gửi dữ liệu tới ${url}:`, error);
    throw error.response?.data || `Lỗi gửi dữ liệu tới ${url}`;
  }
};


const deleteData = async (url) => {
  try {
    const response = await axiosInstance.delete(url);
    return response.data; // hoặc response.data.content tùy backend
  } catch (error) {
    console.error(`Lỗi xóa dữ liệu tại ${url}:`, error);
    throw error.response?.data || `Lỗi xóa dữ liệu tại ${url}`;
  }
};

// export const postChiTietSanPham = (data) => postData('/sanPhamChiTiet', data);

export const getAllSanPham = (page = 0, size = 5) => fetchData(`/product?page=${page}&size=${size}`);
export const postSanPham = (data) => postData('/product', data);
export const putDataSanPham = (id, data) => putData(`/product/${id}`, data);
export const getSanPhamById = (id) => fetchData(`/product/${id}`);


export const getAllXuatXuPage = (page = 0, size = 5) => fetchData(`/xuatXu?page=${page}&size=${size}`);
export const postXuatXu = (dataXuatXu) => postData('/xuatXu', dataXuatXu);
export const putXuatXu = (id, dataXuatXu) => putData(`/xuatXu/${id}`, dataXuatXu);
export const deleteXuatXu = (idXuatXu) => deleteData(`/xuatXu/${idXuatXu}`);

export const getAllPinPage = (page = 0, size = 5) => fetchData(`/pin?page=${page}&size=${size}`);
export const postPin = (dataPin) => postData('/pin', dataPin);
export const putPin = (id, dataPin) => putData(`/pin/${id}`, dataPin);
export const deletePin = (idPin) => deleteData(`/pin/${idPin}`);

export const getAllRamPage = (page = 0, size = 5) => fetchData(`/ram?page=${page}&size=${size}`);
export const postRam = (dataRam) => postData('/ram', dataRam);
export const putRam = (id, dataRam) => putData(`/ram/${id}`, dataRam);
export const deleteRam = (idRam) => deleteData(`/ram/${idRam}`);

export const getAllRomPage = (page = 0, size = 5) => fetchData(`/rom?page=${page}&size=${size}`);
export const postRom = (dataRom) => postData('/rom', dataRom);
export const putRom = (id, dataRom) => putData(`/rom/${id}`, dataRom);
export const deleteRom = (idRom) => deleteData(`/rom/${idRom}`);

export const getAllNhaCungCapPage = (page = 0, size = 5) => fetchData(`/nhaCungCap?page=${page}&size=${size}`);
export const postNCC = (dataNCC) => postData('/nhaCungCap', dataNCC);
export const putNCC = (id, dataNCC) => putData(`/nhaCungCap/${id}`, dataNCC);
export const deleteNCC = (idNCC) => deleteData(`/nhaCungCap/${idNCC}`);

export const getAllLoaiPage = (page = 0, size = 5) => fetchData(`/loai?page=${page}&size=${size}`);
export const postLoai = (dataLoai) => postData('/loai', dataLoai);
export const putLoai = (id, dataLoai) => putData(`/loai/${id}`, dataLoai);
export const deleteLoai = (idLoai) => deleteData(`/loai/${idLoai}`);

export const getAllManHinhPage = (page = 0, size = 5) => fetchData(`/manHinh?page=${page}&size=${size}`);
export const postManHinh = (dataManHinh) => postData('/manHinh', dataManHinh);
export const putManHinh = (id, dataManHinh) => putData(`/manHinh/${id}`, dataManHinh);
export const deleteManHinh = (idManHinh) => deleteData(`/manHinh/${idManHinh}`);

export const getAllMauSacPage = (page = 0, size = 5) => fetchData(`/mauSac?page=${page}&size=${size}`);
export const postMauSac = (dataMauSac) => postData('/mauSac', dataMauSac);
export const putMauSac = (id, dataMauSac) => putData(`/mauSac/${id}`, dataMauSac);
export const deleteMauSac = (idMauSac) => deleteData(`/mauSac/${idMauSac}`);


export const getAllIemiPage = (page = 0, size = 5) => fetchData(`/imei?page=${page}&size=${size}`);

export const getAllHeDieuHanhPage = (page = 0, size = 5) => fetchData(`/heDieuHanh?page=${page}&size=${size}`);
export const postHDH = (dataHDH) => postData('/heDieuHanh', dataHDH);
export const putHDH = (id, dataHDH) => putData(`/heDieuHanh/${id}`, dataHDH);
export const deleteHDH = (idHDH) => deleteData(`/heDieuHanh/${idHDH}`);

export const getAllCpuPage = (page = 0, size = 5) => fetchData(`/cpu?page=${page}&size=${size}`);
export const postCpu = (dataCpu) => postData('/cpu', dataCpu);
export const putCpu = (id, dataCpu) => putData(`/cpu/${id}`, dataCpu);
export const deleteCpu = (idCpu) => deleteData(`/cpu/${idCpu}`);

export const getAllCameraTruocPage = (page = 0, size = 5) => fetchData(`/cameraTruoc?page=${page}&size=${size}`);
export const postCameraTruoc = (dataCameraTruoc) => postData('/cameraTruoc',dataCameraTruoc);
export const putCameraTruoc = (id, dataCameraTruoc) => putData(`/cameraTruoc/${id}`, dataCameraTruoc);
export const deleteCameraTruoc = (idCameraTruoc) => deleteData(`/cameraTruoc/${idCameraTruoc}`);

export const getAllCameraSauPage = (page = 0, size = 5) => fetchData(`/cameraSau?page=${page}&size=${size}`);
export const postCameraSau = (dataCameraSau) => postData('/cameraSau',dataCameraSau);
export const putCameraSau = (id, dataCameraSau) => putData(`/cameraSau/${id}`, dataCameraSau);
export const deleteCameraSau = (idCameraSau) => deleteData(`/cameraSau/${idCameraSau}`);


export const getAllNhaCungCapList = () => fetchData('/nhaCungCap/listNCC');
export const postNhaCungCapList = (data) => postData('/nhaCungCap/quick-create', data)

export const getAllXuatXuList = () => fetchData('/xuatXu/listXuatXu');
export const getAllRomList = () => fetchData('/rom/listRom');
export const getAllRamList = () => fetchData('/ram/listRam');
export const getAllPinList = () => fetchData('/pin/listPin');
export const getAllMauSacList = () => fetchData('/mauSac/listMauSac');
export const getAllManHinhList = () => fetchData('/manHinh/listManHinh');
export const getAllLoaiList = () => fetchData('/loai/listLoai');
export const getAllHinhAnhList = () => fetchData('/hinhAnh/listHinhAnh');
export const getAllHDHList = () => fetchData('/heDieuHanh/listHDH');
export const getAllCpuList = () => fetchData('/cpu/listCpu');
export const getAllCameraTruocList = () => fetchData('/cameraTruoc/listCameraTruoc');
export const getAllCameraSauList = () => fetchData('/cameraSau/listCameraSau');
