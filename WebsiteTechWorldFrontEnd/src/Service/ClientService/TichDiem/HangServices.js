import { getAllPinList } from "@/Service/Adminservice/Products/ProductAdminService";
import api from "@/Service/LoginService/axiosInstance";
import { ca } from "element-plus/es/locales.mjs";

const baseURL = "/client/hang";

export const getListHang = async () => {
  try {
    const response = await api.get(`${baseURL}/listHang`);
    return response.data;
  } catch (err) {
    throw err.response?.data || "Cannot getting rank of customer";
  }
};

//get ten hang
export const getHangThanhVien = async () => {
  try {
    const response = await api.get(`${baseURL}`);
    return response.data;
  } catch (err) {
    throw err.response?.data || "Throw errors when getting rank of client";
  }
};

//get diem xet hang
export const getDiemXetHang = async (idViDiem) => {
  try {
    const response = await api.get(`${baseURL}/diemXetHang/${idViDiem}`);
    return response.data;
  } catch (err) {
    throw (
      err.response?.data || "Throw errors when getting diemXetHang of client"
    );
  }
};
