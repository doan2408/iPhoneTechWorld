import api from "@/Service/LoginService/axiosInstance";


const baseURL = '/admin/bao-hanh';


export const getAllBaoHanh = async (params) => {
  try {
    const response = await api.get(baseURL, { params });
    return response.data;
  } catch (error) {
    console.error('Lỗi khi lấy danh sách bảo hành:', error);
    throw error.response?.data || new Error('Lỗi khi lấy danh sách bảo hành');
  }
};

export const getBaoHanh = async (id) => {
  try {
    const respones = await api.get(baseURL + `${id}`);
    return response.data;
  }
  catch (error) {
    console.error("Lỗi khi detail bảo hành");
    throw error.respones?.data || new Error("Lỗi khi detail bảo hành");
  }
}

export const addWarranty = async (request) => {
  try {
    const response = await api.post(baseURL, request);
    return response.data;
  }
  catch (error) {
    console.log("error occurred whild adding warranty");
    throw error.response?.data || "add error";
  }
}

export const updateWarranty = async (id, request) => {
  try {
    const response = await api.put(`${baseURL}/${id}`, request);
    return response.data;
  }
  catch (error) {
    throw error.response?.data || "Error occurred whild updating warranty"
  }
}

export const imeiDaBanListByKhachHang = async (idKhachHang) => {
  try {
    const response = await api.get(`/admin/imei-da-ban/${idKhachHang}`);
    return response.data;
  }
  catch (err) {
      throw err.response?.data || "Error occurred whild get imeiDaBan list by idKhachHang";
  }
}

export const checkedWarranty = (soImei) => {
  return api.get("/admin/bao-hanh/check-bao-hanh/" + soImei)
}

export const createRequestWarranty = (request) => {
  return api.post("/admin/bao-hanh/create-request-warranty" ,request)
}