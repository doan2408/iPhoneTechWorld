package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices;

import org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest.ChiTietHoaDonAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest.CthdUpdateSoLuongAdminRequest;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class HoaDonChiTietAdminServices {
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    private final HoaDonRepository hoaDonRepository;
    private final SanPhamRepository sanPhamRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final ImeiDaBanRepository imeiDaBanRepository;
    private final ImeiReposiory imeiReposiory;

    public HoaDonChiTietAdminServices(ChiTietHoaDonRepository chiTietHoaDonRepository, HoaDonRepository hoaDonRepository, SanPhamRepository sanPhamRepository, SanPhamChiTietRepository sanPhamChiTietRepository, ImeiDaBanRepository imeiDaBanRepository, ImeiReposiory imeiReposiory) {
        this.chiTietHoaDonRepository = chiTietHoaDonRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.sanPhamRepository = sanPhamRepository;
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
        this.imeiDaBanRepository = imeiDaBanRepository;
        this.imeiReposiory = imeiReposiory;
    }

    public ChiTietHoaDon toEntity(ChiTietHoaDonAdminRequest chiTietHoaDonAdminRequest, HoaDon hoaDon, SanPhamChiTiet sanPhamChiTiet, BigDecimal donGia){
        ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
        chiTietHoaDon.setIdHoaDon(hoaDon);
        chiTietHoaDon.setIdSanPhamChiTiet(sanPhamChiTiet);
        chiTietHoaDon.setTenSanPham(chiTietHoaDonAdminRequest.getTenSanPham());
        chiTietHoaDon.setMoTa(chiTietHoaDonAdminRequest.getMoTa());
        chiTietHoaDon.setSoLuong(chiTietHoaDonAdminRequest.getSoLuong());
        chiTietHoaDon.setDonGia(donGia);

        return chiTietHoaDon;
    }

    public ChiTietHoaDon createChiTietHoaDon(ChiTietHoaDonAdminRequest request){
        HoaDon hoaDon = hoaDonRepository.findById(request.getIdHoaDon())
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại"));

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(request.getIdSanPhamChiTiet())
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm chi tiết không tồn tại"));

        int soLuong = request.getSoLuong();
        List<Imei> imeisAvailable = imeiReposiory.findTopByIdSanPhamChiTietAndTrangThaiImei(
                sanPhamChiTiet.getId(),"AVAILABLE",soLuong
        );
        if (imeisAvailable.size() < soLuong){
            throw new IllegalArgumentException("Không đủ IMEI có sẵn để tạo hóa đơn");
        }

        imeisAvailable.forEach(imei->imei.setTrangThaiImei(TrangThaiImei.RESERVED));
        imeiReposiory.saveAll(imeisAvailable);

        BigDecimal donGia = sanPhamChiTiet.getGiaBan();

        ChiTietHoaDon chiTietHoaDon = toEntity(request,hoaDon,sanPhamChiTiet,donGia);

        ChiTietHoaDon cthdSave = chiTietHoaDonRepository.save(chiTietHoaDon);

        List<ImeiDaBan> imeiDaBans = imeisAvailable.stream().map(imei ->{
            ImeiDaBan imeiDaBan = new ImeiDaBan();
            imeiDaBan.setIdHoaDonChiTiet(cthdSave);
            imeiDaBan.setSoImei(imei.getSoImei());
            imeiDaBan.setTrangThai(TrangThaiImei.RESERVED);
            return imeiDaBan;
        }).toList();
        imeiDaBanRepository.saveAll(imeiDaBans);

        return cthdSave;
    }

    @Transactional
    public void updateSoLuong(Integer hoaDonId, Integer hdctId, CthdUpdateSoLuongAdminRequest request){
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại"));
        ChiTietHoaDon chiTietHoaDon = chiTietHoaDonRepository.findById(hdctId)
                .orElseThrow(() -> new IllegalArgumentException("Hdct khong ton tai"));
        Integer soLuongCu = chiTietHoaDon.getSoLuong();
        Integer soLuongMoi = request.getSoLuong();
        chiTietHoaDon.setSoLuong(request.getSoLuong());
        if (soLuongCu > soLuongMoi){
            giamSoLuong(chiTietHoaDon,soLuongCu - soLuongMoi);
        }
        if (soLuongCu<soLuongMoi){
           tangSoLuong(chiTietHoaDon,soLuongMoi-soLuongCu);
        }
        chiTietHoaDonRepository.save(chiTietHoaDon);
    }

    private void giamSoLuong(ChiTietHoaDon chiTietHoaDon, int soLuongCanGiam) {
        List<ImeiDaBan> imeiDaBans = imeiDaBanRepository.findByIdHoaDonChiTiet_Id(chiTietHoaDon.getId());
        List<ImeiDaBan> imeiCanGiam = imeiDaBans.subList(0, soLuongCanGiam);

        imeiDaBanRepository.deleteAll(imeiCanGiam);

        for (ImeiDaBan imeiCanG : imeiCanGiam) {
            Imei imei = imeiReposiory.findBySoImei(imeiCanG.getSoImei());
            imei.setTrangThaiImei(TrangThaiImei.AVAILABLE);
            imeiReposiory.save(imei);
        }
    }
    private void tangSoLuong(ChiTietHoaDon chiTietHoaDon, int soLuongCanThem) {
        List<Imei> imeisAvailable = imeiReposiory.findTopByIdSanPhamChiTietAndTrangThaiImei(
                chiTietHoaDon.getIdSanPhamChiTiet().getId(), "AVAILABLE", soLuongCanThem
        );

        if (imeisAvailable.size() < soLuongCanThem) {
            throw new IllegalArgumentException("Không đủ IMEI có sẵn: cần " + soLuongCanThem + ", có " + imeisAvailable.size());
        }

        reserveImeis(imeisAvailable);

        List<ImeiDaBan> imeiDaBans = generateImeiDaBan(chiTietHoaDon, imeisAvailable);
        imeiDaBanRepository.saveAll(imeiDaBans);
    }

    //Chuyen imei qua reserved
    private void reserveImeis(List<Imei> imeis) {
        imeis.forEach(imei -> imei.setTrangThaiImei(TrangThaiImei.RESERVED));
        imeiReposiory.saveAll(imeis);
    }

    // tao imei da ban
    private List<ImeiDaBan> generateImeiDaBan(ChiTietHoaDon chiTietHoaDon, List<Imei> imeis) {
        return imeis.stream().map(imei -> {
            ImeiDaBan imeiDaBan = new ImeiDaBan();
            imeiDaBan.setIdHoaDonChiTiet(chiTietHoaDon);
            imeiDaBan.setSoImei(imei.getSoImei());
            imeiDaBan.setTrangThai(TrangThaiImei.RESERVED);
            return imeiDaBan;
        }).toList();
    }



}
