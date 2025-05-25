package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ChiTietHoaDon;

import org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest.ChiTietHoaDonAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest.CthdUpdateSoLuongAdminRequest;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.Imei.HoaDonChiTiet_ImeiAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ImeiDaBan.ImeiDaBanAdminServices;
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
    private final ImeiDaBanAdminServices imeiDaBanAdminServices;
    private final HoaDonChiTiet_ImeiAdminServices hoaDonChiTiet_imeiAdminServices;

    public HoaDonChiTietAdminServices(ChiTietHoaDonRepository chiTietHoaDonRepository, HoaDonRepository hoaDonRepository, SanPhamRepository sanPhamRepository, SanPhamChiTietRepository sanPhamChiTietRepository, ImeiDaBanRepository imeiDaBanRepository, ImeiReposiory imeiReposiory, ImeiDaBanAdminServices imeiDaBanAdminServices, HoaDonChiTiet_ImeiAdminServices hoaDonChiTietImeiAdminServices) {
        this.chiTietHoaDonRepository = chiTietHoaDonRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.sanPhamRepository = sanPhamRepository;
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
        this.imeiDaBanRepository = imeiDaBanRepository;
        this.imeiReposiory = imeiReposiory;
        this.imeiDaBanAdminServices = imeiDaBanAdminServices;
        hoaDonChiTiet_imeiAdminServices = hoaDonChiTietImeiAdminServices;
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
    public ChiTietHoaDon findById(Integer id){
        return chiTietHoaDonRepository.findById(id).orElseThrow();
    }

    //ham tao chi tiet hoa donn ( them san pham )
    @Transactional
    public ChiTietHoaDon createChiTietHoaDon(ChiTietHoaDonAdminRequest request){
        HoaDon hoaDon = hoaDonRepository.findById(request.getIdHoaDon())
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại"));

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(request.getIdSanPhamChiTiet())
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm chi tiết không tồn tại"));

        ChiTietHoaDon cthdCheck = chiTietHoaDonRepository.findByIdHoaDon_IdAndIdSanPhamChiTiet_Id(request.getIdHoaDon(), request.getIdSanPhamChiTiet());

        BigDecimal donGia = sanPhamChiTiet.getGiaBan();
        int soLuong = request.getSoLuong();

        if (cthdCheck != null){
            hoaDonChiTiet_imeiAdminServices.tangSoLuong(cthdCheck,soLuong);
            cthdCheck.setSoLuong(cthdCheck.getSoLuong()+soLuong);
            return chiTietHoaDonRepository.save(cthdCheck);
        }


        List<Imei> imeisAvailable = imeiReposiory.findTopByIdSanPhamChiTietAndTrangThaiImei(
                sanPhamChiTiet.getId(),"AVAILABLE",soLuong
        );
        if (imeisAvailable.size() < soLuong){
            throw new IllegalArgumentException("Không đủ IMEI có sẵn để tạo hóa đơn");
        }

        hoaDonChiTiet_imeiAdminServices.changeStatusImei(imeisAvailable, TrangThaiImei.RESERVED);


        ChiTietHoaDon chiTietHoaDon = toEntity(request,hoaDon,sanPhamChiTiet,donGia);

        ChiTietHoaDon cthdSave = chiTietHoaDonRepository.save(chiTietHoaDon);

        List<ImeiDaBan> imeiDaBans = imeiDaBanAdminServices.generateImeiDaBan(cthdSave,imeisAvailable,TrangThaiImei.RESERVED);
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
            hoaDonChiTiet_imeiAdminServices.giamSoLuong(chiTietHoaDon,soLuongCu - soLuongMoi);
        }
        if (soLuongCu<soLuongMoi){
           hoaDonChiTiet_imeiAdminServices.tangSoLuong(chiTietHoaDon,soLuongMoi-soLuongCu);
        }
        chiTietHoaDonRepository.save(chiTietHoaDon);
    }





    //ham xoa hoa don chi tiet khoi hoa don
    public void deleleHdct(Integer hdctId){
        ChiTietHoaDon cthdCanXoa = chiTietHoaDonRepository.findById(hdctId).orElseThrow();
        List<ImeiDaBan> imeiDaBanList = cthdCanXoa.getImeiDaBans().stream().toList();
        // Lấy danh sách Imei từ soImei trong imeiDaBan
        List<String> soImeis = imeiDaBanList.stream()
                .map(ImeiDaBan::getSoImei)
                .toList();

        List<Imei> imeiList = imeiReposiory.findAllBySoImeiIn(soImeis);

        hoaDonChiTiet_imeiAdminServices.changeStatusImei(imeiList,TrangThaiImei.AVAILABLE);
        chiTietHoaDonRepository.deleteById(hdctId);
    }


}
