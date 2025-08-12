import api from "@/Service/LoginService/axiosInstance";
import { ref } from "vue";

const url_base = "/hoa-don/phieu-giam-gia"

export const getAllPhieuGiamGia = (search, idKhachHang, giaTriDonHangToiThieu) => {
    const url = url_base + '/list-phieu-giam-gia'
    return api.get(url, {
        params: {
            search: search,
            idKhachHang: idKhachHang,
            giaTriDonHangToiThieu: giaTriDonHangToiThieu
        }
    })
}

export const phieuGiamGia = (id, request) => {
    return api.put(url_base + '/' + id + '/phieu-giam-gia', request)
}