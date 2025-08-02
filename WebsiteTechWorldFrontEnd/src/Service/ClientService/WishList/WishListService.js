import api from "@/Service/LoginService/axiosInstance";

const baseURL = "/client/wish-list"

export const getWishListByIdKhachHang = async (pageNo, pageSize) => {
    return await api.get(baseURL, {
        params: {
            pageNo,
            pageSize
        }
    });
};

export const checkExistsWishList = async (idSanPham, idRom, idMauSac) => {
    return await api.get(baseURL + '/check-ton-tai', {
        params: {
            idSanPham,
            idRom,
            idMauSac
        }
    });
};

export const addNewWishList = async (idSanPham, idRom, idMauSac) => {
    return await api.post(baseURL, {
        idSanPham,
        idRom,
        idMauSac
    })
}

export const deleteWishList = async (idSanPham, idRom, idMauSac,idSpct) => {
    return await api.delete(baseURL, {
        data: {
            idSanPham,
            idRom,
            idMauSac,
            idSpct
        }
    })
}