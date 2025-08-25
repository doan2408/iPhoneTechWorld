package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDon;

import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Enum.HoaDon.LoaiHoaDon;
import org.example.websitetechworld.Repository.HoaDonRepository;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ChiTietHoaDon.HoaDonChiTietAdminServices;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HoaDonAdminCleanerService {

    private final HoaDonRepository hoaDonRepository;
    private final HoaDonChiTietAdminServices hoaDonChiTietAdminServices;

    public HoaDonAdminCleanerService(HoaDonRepository hoaDonRepository, HoaDonChiTietAdminServices hoaDonChiTietAdminServices) {
        this.hoaDonRepository = hoaDonRepository;
        this.hoaDonChiTietAdminServices = hoaDonChiTietAdminServices;
    }

    @Scheduled(fixedRate = 60 * 1000)
    @Transactional
    public void xoaHoaDonPOSqua30phut(){
        LocalDateTime thoiHan = LocalDateTime.now().minusMinutes(30);

        List<HoaDon> hoaDons =  hoaDonRepository
                .findByLoaiHoaDonAndNgayThanhToanIsNullAndNgayTaoHoaDonBefore(
                        LoaiHoaDon.POS,thoiHan
                );

        if(!hoaDons.isEmpty()){
            for (HoaDon hoaDonCanDel : hoaDons){
                for (ChiTietHoaDon chiTietHoaDon: hoaDonCanDel.getChiTietHoaDons()){
                    hoaDonChiTietAdminServices.deleleHdct30p(chiTietHoaDon.getId());
                }
            }
        }
        hoaDonRepository.deleteAll(hoaDons);
    }

}
