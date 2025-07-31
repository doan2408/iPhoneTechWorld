import api from '@/Service/LoginService/axiosInstance';
import { darken } from 'element-plus/es/components/button/src/button-custom.mjs';
import { da, pa } from 'element-plus/es/locales.mjs';

 const baseURL= '/admin';


// Hàm chung lấy dữ liệu từ API
const fetchData = async (url) => {
  try {
    const response = await api.get(`${baseURL}${url}`);
    return response.data; // trả về data từ server
  } catch (error) {
    console.error(`Lỗi lấy dữ liệu từ ${baseURL} + ${url}:`, error);
    throw error.response?.data || `Lỗi lấy dữ liệu từ ${baseURL} + ${url}`;
  }
}

// const postData = async (url, data) => {
//   try {
//     const response = await api.post(`${baseURL}${url}`, data);
//     return response.data; // hoặc response.data.content nếu backend trả về theo kiểu đó
//   } catch (error) {
//     console.error('Lỗi gửi dữ liệu tới /admin/product:', {
//       data,
//       status: error.response?.status,
//       responseData: error.response?.data,
//       message: error.message
//     });
//     throw error;
//   }
// };


export const postData = async (url,payload) => {
  try {
    const res = await api.post(`${baseURL}${url}`, payload, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    console.log('📩 [postModelSanPham] Response:', res);
    return res.data;
  } catch (err) {
    console.error('❌ [postModelSanPham] Error:', err);
    throw err;
  }
};



// export const postData = async (url, data, successMessage = '', showError = true) => {
//   try {
//     const response = await api.post(`${baseURL}${url}`, data);

//     if (successMessage) {
//       ElMessage.success(successMessage);
//     }

//     return response.data; // có thể là object, array, v.v.
//   } catch (error) {
//     if (showError) {
//       // In thông tin chi tiết ra console để debug
//       console.error(`❌ POST ${url} failed`, {
//         dataSent: data,
//         status: error.response?.status,
//         responseData: error.response?.data,
//         errorMessage: error.message
//       });

//       // Xử lý lỗi validation từ backend
//       if (error.response?.status === 400 && error.response?.data?.message) {
//         const messages = error.response.data.message;
//         if (typeof messages === 'object') {
//           Object.values(messages).forEach(msg => {
//             ElMessage.error(msg);
//           });
//         } else {
//           ElMessage.error(messages);
//         }
//       } else {
//         ElMessage.error('Đã xảy ra lỗi khi gửi dữ liệu!');
//       }
//     }

//     throw error; // để nơi gọi còn biết mà xử lý tiếp nếu cần
//   }
// };

export const findSanPhamBanHang = (tenSanPham,pageNo, pageSize) => {
  const urlProduct = '/admin/product'
  const url = urlProduct + '/ten-san-pham';
  return api.get(url, {
    params: {
      tenSanPham,
      pageNo,
      pageSize
    }
  }
  )
}

const postDataSpct = async (url, data) => {
  try {

    const response = await api.post(`${baseURL}${url}`, data, {
      headers: {
        'Content-Type': 'application/json',
      },
    });
    return response.data;
  } catch (error) {
    console.error(`Lỗi gửi dữ liệu tới ${baseURL}${url}:`, error);
    throw error.response?.data || `Lỗi gửi dữ liệu tới ${baseURL}${url}`;
  }
};


const putData = async (url, data) => {
  try {
    const response = await api.put(`${baseURL}${url}`, data);
    return response.data; // hoặc response.data.content nếu backend trả về theo kiểu đó
  } catch (error) {
    console.error(`Lỗi gửi dữ liệu tới ${baseURL}${url}:`, error);
    throw error.response?.data || `Lỗi gửi dữ liệu tới ${baseURL}${url}`;
  }
};


const deleteData = async (url) => {
  try {
    const response = await api.delete(`${baseURL}${url}`);
    return response.data; // hoặc response.data.content tùy backend
  } catch (error) {
    console.error(`Lỗi xóa dữ liệu tại ${baseURL}${url}:`, error);
    throw error.response?.data || `Lỗi xóa dữ liệu tại ${baseURL}${url}`;
  }
};

export const getAllSanPhamChiTiet = (pageNo, pageSize) => fetchData(`/sanPhamChiTiet?page=${page}&size=${size}`);
// export const postChiTietSanPham = (data) => postData('/sanPhamChiTiet', data);

export const getAllSanPham = (page = 0, size = 5, keyword = '', idLoai, trangThai = '', maXuatXu = '') => {
  const params = new URLSearchParams({
    keyword,
    page,
    size,
    trangThai,
    maXuatXu
  });
  if (idLoai !== null && idLoai !== undefined) {
    params.append('idLoai', idLoai); // Sửa params26 thành params
  }
  const url = `/product?${params.toString()}`;
  console.log('Request URL:', url);
  return fetchData(url).then(response => {
    console.log('API Response:', response);
    if (!response || response.length === 0) {
      console.warn('No data returned or empty response');
    }
    return response;
  }).catch(error => {
    console.error('Fetch error:', error);
    throw error;
  });
};

export const postSanPham = (data) => postData('/product', data);
export const putDataSanPham = (id, data) => putData(`/product/${id}`, data);
export const getSanPhamById = (id) => fetchData(`/product/${id}`);
export const deleteSanPham = (id) => deleteData(`/product/${id}`);
export const getViewSanPham = (id) => fetchData(`/product/viewSanPham/${id}`);

export const getTrangThaiSanPham = (data) => fetchData('/product/trang-thai', data);


export const getAllXuatXuPage = (page = 0, size = 5, search = '') => fetchData(`/xuatXu/search?search=${search}&page=${page}&size=${size}`);
export const postXuatXu = (dataXuatXu) => postData('/xuatXu', dataXuatXu);
export const putXuatXu = (id, dataXuatXu) => putData(`/xuatXu/${id}`, dataXuatXu);
export const deleteXuatXu = (idXuatXu) => deleteData(`/xuatXu/${idXuatXu}`);

export const getAllPinPage = (page = 0, size = 5, search = '') => fetchData(`/pin/search?search=${search}&page=${page}&size=${size}`);
export const postPin = (dataPin) => postData('/pin', dataPin);
export const putPin = (id, dataPin) => putData(`/pin/${id}`, dataPin);
export const deletePin = (idPin) => deleteData(`/pin/${idPin}`);

export const getAllRamPage = (page = 0, size = 5, search = '') => fetchData(`/ram/search?search=${search}&page=${page}&size=${size}`);
export const postRam = (dataRam) => postData('/ram', dataRam);
export const putRam = (id, dataRam) => putData(`/ram/${id}`, dataRam);
export const deleteRam = (idRam) => deleteData(`/ram/${idRam}`);

export const getAllRomPage = (page = 0, size = 5, search = '') => fetchData(`/rom/search?search=${search}&page=${page}&size=${size}`);
export const postRom = (dataRom) => postData('/rom', dataRom);
export const putRom = (id, dataRom) => putData(`/rom/${id}`, dataRom);
export const deleteRom = (idRom) => deleteData(`/rom/${idRom}`);

export const getAllNhaCungCapPage = (page = 0, size = 5, search = '') => fetchData(`/nhaCungCap/search?search=${search}&page=${page}&size=${size}`);
export const postNCC = (dataNCC) => postData('/nhaCungCap', dataNCC);
export const putNCC = (id, dataNCC) => putData(`/nhaCungCap/${id}`, dataNCC);
export const deleteNCC = (idNCC) => deleteData(`/nhaCungCap/${idNCC}`);

export const getAllLoaiPage = (page = 0, size = 5, search = '') => fetchData(`/loai/search?search=${search}&page=${page}&size=${size}`);
export const postLoai = (dataLoai) => postData('/loai', dataLoai);
export const putLoai = (id, dataLoai) => putData(`/loai/${id}`, dataLoai);
export const deleteLoai = (idLoai) => deleteData(`/loai/${idLoai}`);

export const getAllManHinhPage = (page = 0, size = 5, keyword = null) => {
  let url = `/manHinh?page=${page}&size=${size}`;
  if (keyword && keyword.trim() !== "") {
    url += `&keyword=${encodeURIComponent(keyword.trim())}`;
  }
  return fetchData(url);
};
export const postManHinh = (dataManHinh) => postData('/manHinh', dataManHinh);
export const putManHinh = (id, dataManHinh) => putData(`/manHinh/${id}`, dataManHinh);
export const deleteManHinh = (idManHinh) => deleteData(`/manHinh/${idManHinh}`);

export const getAllMauSacPage = (page = 0, size = 5, search = '') => fetchData(`/mauSac/search?search=${search}&page=${page}&size=${size}`);
export const postMauSac = (dataMauSac) => postData('/mauSac', dataMauSac);
export const putMauSac = (id, dataMauSac) => putData(`/mauSac/${id}`, dataMauSac);
export const deleteMauSac = (idMauSac) => deleteData(`/mauSac/${idMauSac}`);

export const getAllImeiPage = (page = 0, size = 5, keyword = null) => {
  let url = `/imei?page=${page}&size=${size}`;
  if(keyword && keyword.trim() !== "") {
    url += `&keyword=${encodeURIComponent(keyword.trim())}`;
  }
  return fetchData(url);
};
export const postImei = (dataImei) => postData('/imei', dataImei);
export const putImei = (id, dataImei) => putData(`/imei/${id}`, dataImei)
export const deleteImei = (idImei) => deleteData(`/imei/${id}`)

export const getAllHeDieuHanhPage = (page = 0, size = 5, keyword = null) => {
  let url = `/heDieuHanh?page=${page}&size=${size}`;
  if (keyword && keyword.trim() !== "") {
    url += `&keyword=${encodeURIComponent(keyword.trim())}`;
  }
  return fetchData(url);
};
export const postHDH = (dataHDH) => postData('/heDieuHanh', dataHDH);
export const putHDH = (id, dataHDH) => putData(`/heDieuHanh/${id}`, dataHDH);
export const deleteHDH = (idHDH) => deleteData(`/heDieuHanh/${idHDH}`);

export const getAllCpuPage = (page = 0, size = 5, keyword = null) => {
  let url = `/cpu?page=${page}&size=${size}`;
  if (keyword && keyword.trim() !== "") {
    url += `&keyword=${encodeURIComponent(keyword.trim())}`;
  }
  return fetchData(url);
};
export const postCpu = (dataCpu) => postData('/cpu', dataCpu);
export const putCpu = (id, dataCpu) => putData(`/cpu/${id}`, dataCpu);
export const deleteCpu = (idCpu) => deleteData(`/cpu/${idCpu}`);

export const getAllCameraTruocPage = (page = 0, size = 5, keyword = null) => {
  let url = `/cameraTruoc?page=${page}&size=${size}`;
  if(keyword && keyword.trim() !== "") {
    url += `&keyword=${encodeURIComponent(keyword.trim())}`;
  }
  return fetchData(url);
}
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
export const postXuatXuList = (data) => postData('/xuatXu/quick-xuatXu', data);

export const getAllRomList = () => fetchData('/rom/listRom');
export const postRomList = (data) => postData('/rom/quick-rom', data);

export const getAllRamList = () => fetchData('/ram/listRam');
export const postRamList = (data) => postData('/ram/quick-ram', data);

export const getAllPinList = () => fetchData('/pin/listPin');
export const postPinList = (data) => postData('/pin/quick-pin', data);

export const getAllMauSacList = () => fetchData('/mauSac/listMauSac');
export const postMauSacList = (data) => postData('/mauSac/quick-mauSac', data);

export const getAllManHinhList = () => fetchData('/manHinh/listManHinh');
export const postManHinhList = (data) => postData('/manHinh/quick-manHinh', data);

export const getAllLoaiList = () => fetchData('/loai/listLoai');
export const postAllLoaiList = (dataLoai) => postData('/loai',dataLoai);

export const getAllHinhAnhList = () => fetchData('/hinhAnh/listHinhAnh');

export const getAllHDHList = () => fetchData('/heDieuHanh/listHDH');
export const postHDHList = (data) => postData('/heDieuHanh/quick-hdh',data);

export const getAllCpuList = () => fetchData('/cpu/listCpu');
export const postCpuList = (data) => postData('/cpu/quick-cpu', data);

export const getAllCameraTruocList = () => fetchData('/cameraTruoc/listCameraTruoc');
export const postCameraTruocList = (data) => postData('/cameraTruoc/cameraTruoc-quick', data);

export const getAllCameraSauList = () => fetchData('/cameraSau/listCameraSau');
export const postCameraSauList = (data) => postData('/cameraSau/cameraSau-quick', data);


export const getAllModelSanPhamList = () => fetchData('/modelSanPham/listModelSanPham');
export const getAllPageModelSanPham = (page = 0, size = 5) => fetchData(`/modelSanPham?page=${page}&size=${size}`);

export const postModelSanPham = (data) => postData('/modelSanPham', data);

export const putModelSanPham = (id, data) => putData(`/modelSanPham/${id}`, data);
export const deleteModelSanPham = (idModelSanPham) => deleteData(`/modelSanPham/${idModelSanPham}`);
export const finByIdModelSanPham = (idModelSanPham) => fetchData(`/modelSanPham/${idModelSanPham}`);

export const filterModelSanPham = (page = 0, size = 5, search = '', idLoai, idRam, idXuatXu) => {
  const params = new URLSearchParams();

  params.append('page', page);
  params.append('size', size);
  if (search) params.append('search', search);
  if (idLoai !== null && idLoai !== undefined) params.append('idLoai', idLoai);
  if (idRam !== null && idRam !== undefined) params.append('idRam', idRam);
  if (idXuatXu !== null && idXuatXu !== undefined) params.append('idXuatXu', idXuatXu);

  return fetchData(`/modelSanPham/filter?${params.toString()}`);
}

export const loadCategory = (pageNo, pageSize) =>{
  const urlProduct = '/admin/product'
  const url = urlProduct + '/category';
  return api.get(url,{
    params:{
      pageNo,
      pageSize
    }
  })

}
export const loadSanPhamChiTiet = (pageNo,pageSize) => {
  const urlProduct = '/admin/sanPhamChiTiet'
  return api.get(urlProduct, {
    params: {
      pageNo,
      pageSize
    }
  })
}

