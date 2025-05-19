package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.Imei;

import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.Imei;
import org.example.websitetechworld.Entity.ImeiDaBan;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Repository.ImeiDaBanRepository;
import org.example.websitetechworld.Repository.ImeiReposiory;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ImeiDaBan.ImeiDaBanAdminServices;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonChiTiet_ImeiAdminServices {
    private final ImeiDaBanRepository imeiDaBanRepository;
    private final ImeiReposiory imeiReposiory;
    private final ImeiDaBanAdminServices imeiDaBanAdminServices;

    public HoaDonChiTiet_ImeiAdminServices(ImeiDaBanRepository imeiDaBanRepository, ImeiReposiory imeiReposiory, ImeiDaBanAdminServices imeiDaBanAdminServices) {
        this.imeiDaBanRepository = imeiDaBanRepository;
        this.imeiReposiory = imeiReposiory;
        this.imeiDaBanAdminServices = imeiDaBanAdminServices;
    }

    public void giamSoLuong(ChiTietHoaDon chiTietHoaDon, int soLuongCanGiam) {
        List<ImeiDaBan> imeiDaBans = imeiDaBanRepository.findByIdHoaDonChiTiet_Id(chiTietHoaDon.getId());
        List<ImeiDaBan> imeiCanGiam = imeiDaBans.subList(0, soLuongCanGiam);

        imeiDaBanRepository.deleteAll(imeiCanGiam);

        for (ImeiDaBan imeiCanG : imeiCanGiam) {
            Imei imei = imeiReposiory.findBySoImei(imeiCanG.getSoImei());
            imei.setTrangThaiImei(TrangThaiImei.AVAILABLE);
            imeiReposiory.save(imei);
        }
    }
    public void tangSoLuong(ChiTietHoaDon chiTietHoaDon, int soLuongCanThem) {
        List<Imei> imeisAvailable = imeiReposiory.findTopByIdSanPhamChiTietAndTrangThaiImei(
                chiTietHoaDon.getIdSanPhamChiTiet().getId(), "AVAILABLE", soLuongCanThem
        );

        if (imeisAvailable.size() < soLuongCanThem) {
            throw new IllegalArgumentException("Không đủ IMEI có sẵn: cần " + soLuongCanThem + ", có " + imeisAvailable.size());
        }

        changeStatusImei(imeisAvailable,TrangThaiImei.RESERVED);

        List<ImeiDaBan> imeiDaBans = imeiDaBanAdminServices.generateImeiDaBan(chiTietHoaDon, imeisAvailable,TrangThaiImei.RESERVED);
        imeiDaBanRepository.saveAll(imeiDaBans);
    }

    //Doi trang thai imei
    public void changeStatusImei(List<Imei> imeis,TrangThaiImei trangThaiImei) {
        imeis.forEach(imei -> imei.setTrangThaiImei(trangThaiImei));
        imeiReposiory.saveAll(imeis);
    }


}
