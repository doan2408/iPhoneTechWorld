import api from "@/Service/LoginService/axiosInstance";
import { ref } from "vue";

const baseURL = "/false-reason";

export const getAllFalseReasonByCaseReason = (caseType) => {
    const url = baseURL + '/case-type';
    return api.get(url,{
        params:{
            caseType: caseType
        }
    })
}
