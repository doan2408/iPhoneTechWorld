import axios from "axios";
import { ref } from "vue";

const url_base = "/admin/hoa-don"

// api getAll hoadon
export const hoaDonGetAll = (pageNo = 0 ,pageSize = 10 ) => {
    return axios.get(url_base,{
        params:{
            pageNo,
            pageSize
        }
    })
}   

//api detail hoa don
export const hoaDonDetail = (id) => {
    const url = url_base+ '/' + id;
    return axios.get(url)
}

// api view lich su hoa don
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


//api hien thi cac hoa don theo id nhan vien ..
export const loadHoaDonByIdNhanVien = () => {
    const url = url_base + '/all-name-hoa-don';
    return axios.get(url)
}

export const hoaDonDelete = (id) => {
    const url = url_base+ '/' + id;
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

export const addProductIntoInvoice = (idHoaDon,requestData) => {
    const url =url_base +'/'+idHoaDon +'/them-san-pham';
    return axios.post(url,requestData)
}

export const deleteDetailInvoice = (idChiTietHoaDon) => {
    const url = url_base + '/hdct/' + idChiTietHoaDon ;
    return axios.delete(url)
}