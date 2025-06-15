
package org.example.websitetechworld.Services.AdminServices.GiaoHangAdminServces;

import org.example.websitetechworld.Dto.Request.AdminRequest.GiaoHangAdminRequest.AddGIaoHangAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang.GetAllGiaoHangResponseAdmin;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang.ViewGiaoHangAdminResponse;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;
import org.example.websitetechworld.Repository.HoaDonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class GiaoHangAdminServices {

    private final HoaDonRepository hoaDonRepository;

    public GiaoHangAdminServices(HoaDonRepository hoaDonRepository) {
        this.hoaDonRepository = hoaDonRepository;
    }

    public void updateStatus(Integer idGiaoHang, TrangThaiGiaoHang newStatus) {
        Optional<HoaDon> optionalGiaoHang = hoaDonRepository.findById(idGiaoHang);
        if (!optionalGiaoHang.isPresent()) {
            throw new IllegalArgumentException("Không tìm thấy đơn giao hàng với ID: " + idGiaoHang);
        }

        HoaDon hoaDon = optionalGiaoHang.get();
        hoaDon.setTrangThaiDonHang(newStatus);
        hoaDonRepository.save(hoaDon);
    }

}

