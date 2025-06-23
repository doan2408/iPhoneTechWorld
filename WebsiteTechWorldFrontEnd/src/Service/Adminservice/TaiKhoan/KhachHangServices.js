import api from "@/Service/LoginService/axiosInstance";

const baseURL =  "/admin/client";

// params: { page }, // <- Đây là shorthand của { page: page }
export const getAllClient = async (page = 0, keyword = null) => {
  try {
    const params = { page };
    //only add keyword if it is provided
    if (keyword && keyword.trim() !== "") {
      params.keyword = keyword.trim();
    }
    const response = await api.get(`${baseURL}`, { params });
    return response.data;
  } catch (err) {
    console.error(
      "An error was thrown while loading the client of admin: ",
      err
    );
    throw err.response?.data || "Error getting client";
  }
};

export const detailClient = async (id) => {
  try {
    const response = await api.get(`${baseURL}/${id}`);
    return response.data;
  } catch (err) {
    console.error("Có lỗi khi Detail Client phía admin:", error);
    throw error.response?.data || "Lỗi lấy Client";
  }
};

export const updateClient = async (id, clientRequest) => {
  try {
    const response = await api.put(`${baseURL}/${id}`, clientRequest);
    return response.data;
  } catch (error) {
    if (error.response) {
      // Server trả về lỗi (status != 2xx)
      console.error("Lỗi phản hồi từ server:", error.response.data);
      throw error.response.data;
    } else if (error.request) {
      // Request đã gửi đi rồi nhưng không nhận được phản hồi từ server
      console.error("Không có phản hồi từ server:", error.request);
      throw "Không có phản hồi từ server";
    } else {
      // Lỗi xảy ra trong quá trình tạo request (cấu hình axios sai, v.v...)
      console.error("Lỗi khác:", error.message);
      throw error.message;
    }
  }
};

export const addClient = async (clientRequest) => {
  try {
    const response = await api.post(`${baseURL}`, clientRequest);
    return response.data;
  } catch (error) {
    console.error(
      "Có lỗi khi tạo client phía admin:",
      error.response ? error.response.data : error.message
    );
    throw error.response?.data || "Lỗi tạo client";
  }
};

//xem các địa chỉ của 1 khách
export const getAdressesClient = async (id) => {
  try {
    const response = await api.get(`${baseURL}/addresses/${id}`);
    return response.data;
  } catch (err) {
    console.log(
      "An errors was thrown while loading the addresses of client in admin: ",
      err
    );
    throw err.response?.data || "Error getting addresses";
  }
};

//xem từng địa chỉ của khách hàng (trang chỉnh sửa)
export const getAdress = async (id) => {
  try {
    const response = await api.get(`${baseURL}/address/${id}`);
    return response.data;
  } catch (err) {
    console.log(
      "An errors was thrown while loading the address of client in admin: ",
      err
    );
    throw err.response?.data || "Error getting addresses";
  }
};

//update 1 địa chỉ
export const updateAddress = async (id, addressRequest) => {
  try {
    const response = await api.put(`${baseURL}/address/${id}`, addressRequest);
    return response.data;
  } catch (err) {
    if (err.response) {
      console.error(
        "Lỗi từ backend (status:",
        err.response.status + "):",
        err.response.data
      );
      throw err.response.data;
    } else if (err.request) {
      console.error("Không nhận được phản hồi từ server:", err.request);
      throw "Không nhận được phản hồi từ server.";
    } else {
      console.error("Lỗi khi gửi yêu cầu update địa chỉ:", err.message);
      throw "Lỗi khi gửi yêu cầu update địa chỉ: " + err.message;
    }
  }
};

export const getAdressListOfClient = async (clientId) => {
  try {
    const response = await api.get(`/admin/address/${clientId}`);
    return response.data;
  } catch (err) {
    console.log(
      "An errors was thrown while loading the address of client in admin: ",
      err
    );
    throw err.response?.data || "Error getting addresses";
  }
};
