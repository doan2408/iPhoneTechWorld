// src/services/ForgotPass.js
import axios from "axios";

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api/auth',
  withCredentials: true, // gửi kèm cookie nếu backend có session/cors cần
  headers: {
    'Content-Type': 'application/json',
  },
});

//sending code to email
export const sendForgotPasswordEmail = async (email) => {
  try {
    const response = await apiClient.post('/forgot-password', { email });
    return response.data;
  } catch (error) {
    // ném lỗi để component xử lý
    throw error.response?.data || error;
  }
};

//verifycode from email
export const verifycode = async (email, code) => {
  try {
    const response = await apiClient.post('/verify-code', { email, code});
    return response.data;
  }
  catch (err) {
    throw err.response?.data || err;
  }
}

//reset password
export const resetPassword = async (email, newPassword) => {
  try {
    const response = await apiClient.post('/reset-password', { email, newPassword });
    return response.data;
  } catch (error) {
    throw error.response?.data || error;
  }
};

