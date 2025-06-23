// src/services/ForgotPass.js
import api from "./axiosInstance";

const BASE_PATH = "/api/auth";

//sending code to email
export const sendForgotPasswordEmail = async (email) => {
  try {
    const response = await api.post(`${BASE_PATH}/forgot-password`, { email });
    return response.data;
  } catch (error) {
    // ném lỗi để component xử lý
    throw error.response?.data || error;
  }
};

//verifycode from email
export const verifycode = async (email, code) => {
  try {
    const response = await api.post(`${BASE_PATH}/verify-code`, { email, code });
    return response.data;
  }
  catch (err) {
    throw err.response?.data || err;
  }
}

//reset password
export const resetPassword = async (email, newPassword) => {
  try {
    const response = await api.post(`${BASE_PATH}/reset-password`, { email, newPassword });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

