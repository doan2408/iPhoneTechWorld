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