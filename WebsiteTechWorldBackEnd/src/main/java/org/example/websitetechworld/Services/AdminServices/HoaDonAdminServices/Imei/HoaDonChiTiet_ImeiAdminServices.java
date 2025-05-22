package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.Imei;

import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.Imei;
import org.example.websitetechworld.Entity.ImeiDaBan;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Repository.ImeiDaBanRepository;
import org.example.websitetechworld.Repository.ImeiReposiory;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ImeiDaBan.ImeiDaBanAdminServices;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public void updateImeiStautusFromHoaDon(List<ChiTietHoaDon> chiTietHoaDons, TrangThaiImei trangThaiImei){
        List<Imei> imeiList = new ArrayList<>();
        List<ImeiDaBan> imeiDaBansToUpdate = new ArrayList<>();

        for (ChiTietHoaDon chiTietHoaDon : chiTietHoaDons) {
            List<ImeiDaBan> imeiDaBans = imeiDaBanRepository.findByIdHoaDonChiTiet_Id(chiTietHoaDon.getId());
            if (imeiDaBans.size() < chiTietHoaDon.getSoLuong()) {
                throw new IllegalArgumentException("Không đủ IMEI cho chi tiết hóa đơn: " + chiTietHoaDon.getId());
            }
            for (ImeiDaBan imeiDaBan: imeiDaBans) {
                if (imeiDaBan.getTrangThai() != TrangThaiImei.RESERVED) {
                    throw new IllegalArgumentException("IMEI không ở trạng thái đặt trước: " + imeiDaBan.getSoImei());
                }
                Imei imei = imeiReposiory.findBySoImei(imeiDaBan.getSoImei());
                if (imei == null){
                    throw new IllegalArgumentException("Imei khong ton tai" +imeiDaBan.getSoImei());
                }
                imeiList.add(imei);
                // Cập nhật trạng thái trong ImeiDaBan
                imeiDaBan.setTrangThai(trangThaiImei);
                imeiDaBansToUpdate.add(imeiDaBan);
            }
        }
        imeiDaBanRepository.saveAll(imeiDaBansToUpdate);
        // Đồng bộ trạng thái trong Imei
        changeStatusImei(imeiList, trangThaiImei);
    }



}
