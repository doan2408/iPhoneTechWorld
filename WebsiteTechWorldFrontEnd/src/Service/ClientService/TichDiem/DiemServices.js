import api from "@/Service/LoginService/axiosInstance";
import { ca } from "element-plus/es/locales.mjs";

const baseURL = "/client/viDiem";

export const getDiemKhaDung = async () => {
  try {
    const response = await api.get(`${baseURL}`);
    return response.data;
  } catch (err) {
    console.log("Thrown errors when loading point of client");
    throw err.response?.data || "Thrown errors when loading point of client";
  }
};
