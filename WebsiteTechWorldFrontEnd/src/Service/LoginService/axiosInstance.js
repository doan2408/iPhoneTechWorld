  import axios from "axios";

  const api = axios.create({
    baseURL: "http://localhost:8080", // base gốc
  });

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken"); // luôn lấy lại mỗi lần request
    console.log("🔐 Token đính kèm trong interceptor:", token);
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);


  api.interceptors.response.use(
    (response) => response,
    async (error) => {
      const originalRequest = error.config;

      if (
        error.response?.status === 401 &&
        !originalRequest._retry &&
        !originalRequest.url.includes("/api/auth/refresh")
      ) {
        originalRequest._retry = true;

        const refreshToken = localStorage.getItem("refreshToken");

        if (!refreshToken) {
          console.warn("❗Không tìm thấy refresh token trong localStorage");
          window.location.href = "/login";
          return Promise.reject(error);
        }

        try {
          const response = await api.post("/api/auth/refresh", {
            refreshToken,
          });

          const newAccessToken = response.data.accessToken;
          localStorage.setItem("accessToken", newAccessToken);
          originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
          return api(originalRequest);
        } catch (refreshError) {
          localStorage.clear();
          window.location.href = "/login";
          return Promise.reject(refreshError);
        }
      }

      return Promise.reject(error);
    }
  );



  export default api;
