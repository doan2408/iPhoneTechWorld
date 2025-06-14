import axios from "axios"
import { id } from "element-plus/es/locales.mjs"

const url_base = "/admin/giao-hang"

export const changeStatusOrder = (id,status) => {
    const url = url_base + '/' +id + '/status'
    const body = { trangThaiDonHang : status}
    return axios.put(url,body)
}