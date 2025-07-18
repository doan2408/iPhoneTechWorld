import api from "@/Service/LoginService/axiosInstance";
import { ref } from "vue";

const url_base = "/admin/hoa-don"

// api getAll hoadon
export const hoaDonGetAll = (pageNo = 0, pageSize = 10) => {
    return api.get(url_base, {
        params: {
            pageNo,
            pageSize
        }
    })
}

//api detail hoa don
export const hoaDonDetail = (id) => {
    const url = url_base + '/' + id;
    return api.get(url)
}

// api view lich su hoa don
export const viewLichSuHoaDon = (id, pageNo, pageSize) => {
    const url = url_base + '/' + id + '/lich-su';
    return api.get(url, {
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
    const url = url_base + '/soft-delete/' + id;
    return api.delete(url)
}

export const hoaDonHardDelete = (id) => {
    const url = url_base + '/hard-delete/' + id;
    return api.delete(url)
}

export const doanhThuTheoThang = () => {
    const url = url_base + '/doanh-thu-thang';
    return api.get(url)
}

export const countHoaDonPending = () => {
    const url = url_base + '/count/pending';
    return api.get(url)
}

export const addProductIntoInvoice = (idHoaDon, requestData) => {
    const url = url_base + '/' + idHoaDon + '/them-san-pham';
    return api.post(url, requestData)
}

export const deleteDetailInvoice = (idChiTietHoaDon) => {
    const url = url_base + '/hdct/' + idChiTietHoaDon;
    return api.delete(url)
}

export const fetchImeisJs = (productId, page, size) => {
    const url = '/admin/imei/available'
    return api.get(url, {
        params: {
            productId: productId,
            page: page,
            size: size
        }
    });

}

export const loadImeiDaBan = (idCthd) => {
    const url = '/admin/imei-da-ban'
    return api.get(url, {
        params: {
            idCthd: idCthd
        }
    })
}

export const updateSoLuongAndTrangThai = (idHd, idCthd, imeisToReturn) => {
    const url = '/admin/hoa-don/' + idHd + '/chi-tiet/' + idCthd + '/remove-imeis'
    const requestBody = {
        imeisToReturn: imeisToReturn // Đây là key mà backend của bạn mong đợi
    };
    return api.patch(url, requestBody)
}

//Khach hang
export const getListKhachHang = (search, page, size) => {
    const url = url_base + '/list-khach-hang'
    return api.get(url, {
        params: {
            search: search,
            page: page,
            size: size
        }
    })
}

export const selectKhachHang = (idHoaDon, idKH) => {
    const url = url_base + '/' + idHoaDon + '/khach-hang';
    return api.put(url, idKH)
}

export const addKhachHang = (data) => {
    const url = url_base + '/add-khach-hang';
    return api.post(url, data)
}

//Phieu giam gia
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
    return api.put('/admin/hoa-don/' +id + '/phieu-giam-gia', request)
}


export const getTinhThanh = () => {
    const url = '/admin/tinh-thanh/provinces'
    return api.get(url)
}
export const getHuyen = (code) => {
    const url = '/admin/tinh-thanh/districts/' + code
    return api.get(url)
}
export const getXa = (code) => {
    const url = '/admin/tinh-thanh/wards/' + code
    return api.get(url)
}
export const getLatLon = (address) => {
    return api.get('/admin/tinh-thanh/geo', {
        params: {
            address: address
        }
    })
}

// Tính khoảng cách giữa hai tọa độ
export const getDistance = (from, to) => {
    return api.get('/admin/tinh-thanh/distance', {
        params: {
            fromLat: from.lat,
            fromLon: from.lon,
            toLat: to.lat,
            toLon: to.lon
        }
    })
}

//cap nhat thong tin giao hang
export const updateTTShipping = (id, shippingInfo, fullAddressForDB, isShipping) => {
    return api.put(`/admin/hoa-don/update-invoice/${id}`, {
        tenNguoiNhan: shippingInfo.tenNguoiNhan,
        sdtNguoiNhan: shippingInfo.sdtNguoiNhan,
        diaChiGiaoHang: fullAddressForDB,
        phiShip: shippingInfo.phiShip ? Number(shippingInfo.phiShip) : null,
        isShipping: isShipping,
        maVanDon: null,
        thanhTien: null
    });
}

//create pending invoice
export const createPendingInvoice = () => {
    const url = url_base;
    return api.post(url);
}

export const loadPaymentMethod = () => {
    return api.get('/admin/payment-methods');
}

export const thanhToan = (id, paymentPayload) => {
    return api.put('/admin/hoa-don/' +id + '/thanh-toan',paymentPayload)
}

export const findHdctByImeiDaBan = (pageNo = 0, pageSize = 5, idHoaDon ) => {
    return api.get(url_base + '/' + idHoaDon +'/hdct-by-imei-da-ban', {
        params: {
            pageNo,
            pageSize
        }
    })
}

export const findProductByImei = (soImei) => {
    return api.get('/admin/imei/ma-vach/'+soImei)
}