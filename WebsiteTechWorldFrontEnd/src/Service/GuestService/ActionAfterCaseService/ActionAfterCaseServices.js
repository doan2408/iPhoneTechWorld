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