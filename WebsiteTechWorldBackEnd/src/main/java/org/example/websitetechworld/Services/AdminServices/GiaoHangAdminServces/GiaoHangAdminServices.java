package org.example.websitetechworld.Services.AdminServices.GiaoHangAdminServces;

import org.example.websitetechworld.Dto.Request.AdminRequest.GiaoHangAdminRequest.AddGIaoHangAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang.GetAllGiaoHangResponseAdmin;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang.ViewGiaoHangAdminResponse;
import org.example.websitetechworld.Entity.GiaoHang;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;
import org.example.websitetechworld.Repository.GiaoHangRepository;
import org.example.websitetechworld.Repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GiaoHangAdminServices {

    private final HoaDonRepository hoaDonRepository;

    public GiaoHang toEntity(AddGIaoHangAdminRequest request) {
        GiaoHang giaoHang = new GiaoHang();
        giaoHang.setNgayDatHang(LocalDate.now());
        giaoHang.setDiaChiGiaoHang(request.getDiaChiGiaoHang());
        return giaoHang;
    }

    private final GiaoHangRepository giaoHangRepository;

    public GiaoHangAdminServices(GiaoHangRepository giaoHangRepository, HoaDonRepository hoaDonRepository) {
        this.giaoHangRepository = giaoHangRepository;
        this.hoaDonRepository = hoaDonRepository;
    }

    public List<GetAllGiaoHangResponseAdmin> getPageGiaoHang(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return giaoHangRepository.findAll(pageable).stream()
                .map(GetAllGiaoHangResponseAdmin::convertDto).toList();
    }
    public ViewGiaoHangAdminResponse findById(Integer id) {
        return giaoHangRepository.findById(id).map(ViewGiaoHangAdminResponse::convertDto).orElseThrow(
                () -> new RuntimeException("Không ton tai giao hang voi id nay")
        );
    }

    public void addGiaoHang(AddGIaoHangAdminRequest request){
        GiaoHang giaoHang = new GiaoHang();
        HoaDon hoaDon = hoaDonRepository.findById(request.getIdHoaDon()).orElseThrow(
                () -> new IllegalArgumentException("Hoa Don Nay Khong Ton tai")
        );
        giaoHang.setIdKhachHang(hoaDon.getIdKhachHang());
        giaoHang.setIdHoaDon(hoaDon);
        giaoHang.setNgayDatHang(LocalDate.now());
        giaoHang.setTongGiaTriDonHang(hoaDon.getThanhTien());
        giaoHang.setDiaChiGiaoHang(request.getDiaChiGiaoHang());
        giaoHang.setTrangThaiDonHang(TrangThaiGiaoHang.PENDING);
        giaoHangRepository.save(giaoHang);
    }

    public void changePacked(){
        GiaoHang giaoHang = new GiaoHang();
        giaoHang.setTrangThaiDonHang(TrangThaiGiaoHang.PACKED);
    }

    public void changeShipping(){
        GiaoHang giaoHang = new GiaoHang();
        giaoHang.setTrangThaiDonHang(TrangThaiGiaoHang.SHIPPING);
    }

}
