import api from "@/Service/LoginService/axiosInstance";
import { ref } from "vue";

const baseURL = "/action-after-case";

export const createActionAfterCase = (idHoaDon, idFailReason) => {
    const url = baseURL;
    const requestBody = {
        idHoaDon: idHoaDon,
        idFailReason: idFailReason
    };
    return api.post(url, requestBody)
}

export const createActionAfterCaseReturn = (data) => {
    const url = baseURL + '/tra-hang';
    return api.post(url, data)
}

export const getAllLyDoXuLy = (pageNo, pageSize) => {
    const url = baseURL ;
    return api.get(url,{
        params: {
            pageNo,
            pageSize
        }
    })
}
