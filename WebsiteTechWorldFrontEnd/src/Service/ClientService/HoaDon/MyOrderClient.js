import api from "@/Service/LoginService/axiosInstance";

const baseURL = "/client/my-order"
const baseUrlGuest = '/my-order'

export const getMyOrder = async (params = {}) => {
    try {
        console.log('Gọi API getMyOrder với params:', params);
        
        const response = await api.get(baseURL, { params });
        
        console.log('Response từ API:', response.data);
        
        return response;
    } catch (error) {
        console.error('Lỗi API getMyOrder:', error);
        throw error;
    }
};  

//bảng đánh giá
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
    return api.get('/payment-methods');
}

export const thanhToanClient = (data) => {
    return api.put(baseURL + '/thanh-toan',data);
}
export const thanhToanGuest = (data) => {
    return api.put('/my-order/thanh-toan', data);
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

export const deleteHoaDon = (maHd) => {
    return api.delete(`${baseURL}/hdct/`+maHd);
};

export const deleteHoaDonGuest = (maHd) => {
    return api.delete(`/my-order/hdct/` + maHd);
};

export const findHdctByImeiDaBan = (ctHoaDonId) => {
    return api.get('/my-order' + '/' + ctHoaDonId + '/hdct-by-imei-da-ban')
}

export const hoaDonDetailGuest = (id) => {
    const url = baseUrlGuest + '/' + id;
    return api.get(url)
}

