import axios from "axios"; // Sửa đường dẫn import

const api = axios.create({
  baseURL: "http://localhost:8080",
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");
  console.log("Token đính kèm trong interceptor:", token);
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    if (
      error.response?.status === 401 &&
      !originalRequest._retry &&
      originalRequest.url !== "/api/auth/refresh" &&
      originalRequest.url !== "/api/auth/login"
    ) {
      originalRequest._retry = true;
      const refreshToken = localStorage.getItem("refreshToken");
      console.log("Thử refresh token:", refreshToken);
      if (!refreshToken) {
        console.error("Không có refreshToken trong localStorage");
        window.location.href = "/login";
        return Promise.reject(error);
      }
      try {
        const response = await api.post(`/api/auth/refresh`, { refreshToken });
        const newAccessToken = response.data.accessToken;
        console.log("Lưu newAccessToken:", newAccessToken);
        localStorage.setItem("accessToken", newAccessToken);
        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
        console.log("Gửi lại yêu cầu:", originalRequest);
        return api(originalRequest);
      } catch (refreshError) {
        console.error("Lỗi refresh token:", refreshError);
        localStorage.clear();
        window.location.href = "/login";
        return Promise.reject(refreshError);
      }
    }
    console.error("Lỗi từ server:", error.response?.data);
    return Promise.reject(error);
  }
);

export default api;