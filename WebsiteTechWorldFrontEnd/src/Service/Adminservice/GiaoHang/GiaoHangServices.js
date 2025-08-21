import api from "@/Service/LoginService/axiosInstance"
import { id } from "element-plus/es/locales.mjs"

const url_base = "/admin/giao-hang"
const url_base_khach_hang = "/giao-hang"

export const changeStatusOrder = (id,status) => {
    const url = url_base + '/' +id + '/status'
    const body = { trangThaiDonHang : status}
    return api.put(url,body)
}

export const changeStatusOrderKhachHang = (id, status) => {
    const url = '/action-after-case' + '/' + id + '/status'
    const body = { trangThaiDonHang: status }
    return api.put(url, body)
}