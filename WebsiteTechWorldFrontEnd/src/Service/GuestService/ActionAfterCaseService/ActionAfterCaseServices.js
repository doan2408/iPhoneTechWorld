import api from "@/Service/LoginService/axiosInstance";
import { id } from "element-plus/es/locales.mjs";
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

export const getAllLyDoXuLy = (pageNo, pageSize, search, status, sortDir) => {
    const url = baseURL ;
    return api.get(url,{
        params: {
            pageNo,
            pageSize,
            search,
            status,
            sortDir
        }
    })
}

export const getAllCtXuLy = (idHoaDon) => {
    const url = baseURL + '/detail/' + idHoaDon;
    return api.get(url)
}

export const changeStatusPending = (soImeis,idHoaDon,status) => {
    const url = baseURL + '/change-status'
    const request = {
        soImeis: soImeis,
        idHoaDon:idHoaDon,
        status: status
    };
    return api.post(url,request)
}

export const uploadAnhAndVid = (formData) => {
    const url = '/api/upload/upload-imei'
    return api.post(url, formData)
}

export const updateStatusPending = (idHoaDon,soImei, hanhDong) => {
    const url = baseURL + '/update-status'
    const request = {
        idHoaDon: idHoaDon,
        soImei: soImei,
        hanhDong: hanhDong
    };
    return api.put(url, request)
}

export const countDonHangByStatus = () => {
    const url = baseURL + '/stats';
    return api.get(url)
}

export const tuChoiDonHang = (idHoaDon) => {
    const url = baseURL+'/'+idHoaDon;
    return api.post(url)
}