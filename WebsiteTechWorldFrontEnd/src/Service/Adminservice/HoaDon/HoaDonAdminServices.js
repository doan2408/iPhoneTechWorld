import axios from "axios";
import { ref } from "vue";

const url_base = "/admin/hoa-don"

export const hoaDonGetAll = (pageNo =0,pageSize = 10) => {
    return axios.get(url_base,{
        params:{
            pageNo,
            pageSize
        }
    })
}   