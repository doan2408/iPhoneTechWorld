import axios from "axios"

const url_base = "/admin/giao-hang"

export const giaoHangGetAll = (pageNo = 0, pageSize = 10) => {
    return axios.get(url_base,{
        params:{
            pageNo,
            pageSize
        }
    })
}
export const giaoHangDetail = (id) => {
    const url = url_base + '/' + id;
    return axios.get(url)
}