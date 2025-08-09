import api from "@/Service/LoginService/axiosInstance";

const baseURL = "/client/my-order"

export const getMyOrder = async (pageNo,pageSize) => {
    return  await api.get(baseURL,{
        params : {
            pageNo,
            pageSize
        }
    });
};  


export const getMyReview = async (pageNo,pageSize) => {
    return  await api.get(baseURL + '/review',{
        params : {
            pageNo,
            pageSize
        }
    });
};  


export const findIdHoaDonByMVD = async (maVanDon) => {
    return await api.get(baseURL +'/mvd/'+ maVanDon);
};  

export const hoaDonDetail = (id) => {
    const url = baseURL + '/' + id;
    return api.get(url)
}

export const loadPaymentMethod = () => {
    return api.get('/client/payment-methods');
}

export const thanhToanClient = (data) => {
    return api.put(baseURL + '/thanh-toan',data);
}

export const getLatLon = (address) => {
    return api.get('/tinh-thanh/geo', {
        params: {
            address: address
        }
    })
}

export const getDistance = (from, to) => {
    return api.get('/tinh-thanh/distance', {
        params: {
            fromLat: from.lat,
            fromLon: from.lon,
            toLat: to.lat,
            toLon: to.lon
        }
    })
}
//cuong

export const getHoaDonAndIdChiTietHoaDon = (id) => {
    return api.get(`${baseURL}/${id}/chi-tiet`);
};
