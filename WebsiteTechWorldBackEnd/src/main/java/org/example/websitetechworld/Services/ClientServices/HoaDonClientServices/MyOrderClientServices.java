package org.example.websitetechworld.Services.ClientServices.HoaDonClientServices;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.HoaDonAdminResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.HoaDonClientResponse.MyOrderClientResponse;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Mapper.Client.MyOrderClientMapper;
import org.example.websitetechworld.Repository.HoaDonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyOrderClientServices {
    MyOrderClientMapper myOrderClientMapper = new MyOrderClientMapper();
    private final HoaDonRepository hoaDonRepository;

    public MyOrderClientServices(HoaDonRepository hoaDonRepository) {
        this.hoaDonRepository = hoaDonRepository;
    }

    public List<MyOrderClientResponse> getOrderByUserLogin(Integer userLoginId){
        List<HoaDon> lstHoaDon = hoaDonRepository.findByIdKhachHang_Id(userLoginId);
        return lstHoaDon.stream().map(hoaDon -> myOrderClientMapper.toMyOrderClientResponse(hoaDon)).toList();
    }

    public List<Integer> findIdHoaDonByMVD(String maVanDon){
        return hoaDonRepository.findIdHoaDonByMVD(maVanDon);
    }

    public HoaDonAdminResponse findById(Integer id){
        return hoaDonRepository.findById(id).map(HoaDonAdminResponse::convertDto).orElseThrow(() -> new RuntimeException("Khong tim thay hoa don"));
    }
}
