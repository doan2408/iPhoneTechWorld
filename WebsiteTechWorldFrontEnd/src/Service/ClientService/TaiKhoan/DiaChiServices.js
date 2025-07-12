import api from "@/Service/LoginService/axiosInstance";

const baseURL =  "/client/address";

//xem từng địa chỉ của khách hàng (trang chỉnh sửa)
export const getAdress = async (id) => {
  try {
    const response = await api.get(`${baseURL}/${id}`);
    return response.data;
  } catch (err) {
    console.log(
      "An errors was thrown while loading the address of client in admin: ",
      err
    );
    throw err.response?.data || "Error getting addresses";
  }
};


export const addAddress = async (addressRequest) => {
  try {
    const response = await api.post(`${baseURL}`, addressRequest);
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
      console.error("Lỗi khi gửi yêu cầu add địa chỉ:", err.message);
      throw "Lỗi khi gửi yêu cầu add địa chỉ: " + err.message;
    }
  }
};


//update 1 địa chỉ
export const updateAddress = async (id, addressRequest) => {
  try {
    const response = await api.put(`${baseURL}/${id}`, addressRequest);
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
