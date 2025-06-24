import api from "@/Service/LoginService/axiosInstance";
import { ref } from "vue";

const url_base = "/admin/hoa-don"

// api getAll hoadon
export const hoaDonGetAll = (pageNo = 0 ,pageSize = 10 ) => {
    return api.get(url_base,{
        params:{
            pageNo,
            pageSize
        }
    })
}   

//api detail hoa don
export const hoaDonDetail = (id) => {
    const url = url_base+ '/' + id;
    return api.get(url)
}

// api view lich su hoa don
export const viewLichSuHoaDon = (id, pageNo, pageSize) => {
    const url = url_base + '/' + id +'/lich-su';
    return api.get(url,{
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
    return api.get(url)
}

export const hoaDonSoftDelete = (id) => {
    const url = url_base+ '/soft-delete/' + id;
    return api.delete(url)
}

export const hoaDonHardDelete = (id) => {
    const url = url_base+ '/hard-delete/' + id;
    return api.delete(url)
}

export const doanhThuTheoThang = () => {
    const url = url_base+ '/doanh-thu-thang';
    return api.get(url)
}

export const countHoaDonPending = () => {
    const url = url_base+ '/count/pending';
    return api.get(url)
}

export const addProductIntoInvoice = (idHoaDon,requestData) => {
    const url =url_base +'/'+idHoaDon +'/them-san-pham';
    return api.post(url,requestData)
}

export const deleteDetailInvoice = (idChiTietHoaDon) => {
    const url = url_base + '/hdct/' + idChiTietHoaDon ;
    return api.delete(url)
}

export const fetchImeisJs = (productId,page,size) => {
    const url = '/admin/imei/available'
    return api.get(url, {
        params: {
            productId: productId,
            page: page ,
            size: size
        }
    });
     
}