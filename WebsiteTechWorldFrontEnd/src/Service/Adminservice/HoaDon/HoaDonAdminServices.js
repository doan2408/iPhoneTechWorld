import axios from "axios";
import { ref } from "vue";

const url_base = "/admin/hoa-don"

export const hoaDonGetAll = (pageNo = 0 ,pageSize = 10 ) => {
    return axios.get(url_base,{
        params:{
            pageNo,
            pageSize
        }
    })
}   
export const hoaDonDetail = (id) => {
    const url = url_base+ '/' + id;
    return axios.get(url)
}

export const viewLichSuHoaDon = (id, pageNo, pageSize) => {
    const url = url_base + '/' + id +'/lich-su';
    return axios.get(url,{
            params: {
                pageNo,
                pageSize
            }
        }
    )
}

export const hoaDonSoftDelete = (id) => {
    const url = url_base+ '/soft-delete/' + id;
    return axios.delete(url)
}

export const hoaDonHardDelete = (id) => {
    const url = url_base+ '/hard-delete/' + id;
    return axios.delete(url)
}

export const doanhThuTheoThang = () => {
    const url = url_base+ '/doanh-thu-thang';
    return axios.get(url)
}

export const countHoaDonPending = () => {
    const url = url_base+ '/count/pending';
    return axios.get(url)
}
