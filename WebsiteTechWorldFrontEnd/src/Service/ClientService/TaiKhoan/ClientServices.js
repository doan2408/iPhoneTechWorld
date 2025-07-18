import api from "@/Service/LoginService/axiosInstance";

const baseURL = "/client"

export const detailClient = async (id) => {
  try {
    const response = await api.get(`${baseURL}/${id}`);
    return response.data;
  } catch (error) {
    console.error("Có lỗi khi Detail client:", error);
    throw error.response?.data || "Lỗi lấy private information";
  }
};


export const updateInfor = async (id, clientRequest) => {
  try {
    const response = await api.put(`${baseURL}/${id}`, clientRequest);
    return response.data;
  } catch (error) {
    console.error("Có lỗi khi update client:", error);
    throw error.response?.data || "Lỗi update private information";
  }
};

//xem các địa chỉ của 1 khách
export const getAdressesClient = async (id) => {
  try {
    const response = await api.get(`${baseURL}/address/addresses/${id}`);
    return response.data;
  } catch (err) {
    console.log(
      "An errors was thrown while loading the addresses of client in admin: ",
      err
    );
    throw err.response?.data || "Error getting addresses";
  }
};
