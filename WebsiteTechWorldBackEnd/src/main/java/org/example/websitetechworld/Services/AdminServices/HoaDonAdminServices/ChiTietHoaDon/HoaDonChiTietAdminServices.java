package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ChiTietHoaDon;

import org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest.ChiTietHoaDonAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest.CthdGiamSoLuong;
import org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest.CthdUpdateSoLuongAdminRequest;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.Imei.HoaDonChiTiet_ImeiAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ImeiDaBan.ImeiDaBanAdminServices;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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
        chiTietHoaDon.setSoLuong(chiTietHoaDonAdminRequest.getSoLuong());
        chiTietHoaDon.setDonGia(donGia);
        chiTietHoaDon.setTenSanPham(sanPhamChiTiet.getIdSanPham().getTenSanPham());
        chiTietHoaDon.setMoTa("Sản phẩm điện thoại chính hãng.");

        return chiTietHoaDon;
    }
    public ChiTietHoaDon findById(Integer id){
        return chiTietHoaDonRepository.findById(id).orElseThrow();
    }

    //ham tao chi tiet hoa donn ( them san pham )
    @Transactional
    public ChiTietHoaDon createChiTietHoaDon(ChiTietHoaDonAdminRequest request){
        //check  ton tai
        HoaDon hoaDon = hoaDonRepository.findById(request.getIdHoaDon())
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại"));
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(request.getIdSanPhamChiTiet())
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm chi tiết không tồn tại"));

        //check de cap nhat so neu neu ton tai
        ChiTietHoaDon cthdCheck = chiTietHoaDonRepository.findByIdHoaDon_IdAndIdSanPhamChiTiet_Id(request.getIdHoaDon(), request.getIdSanPhamChiTiet());

        BigDecimal donGia = sanPhamChiTiet.getGiaBan();
        int soLuong = request.getSoLuong();
        List<String> imeiIdsFromRequest = request.getImeiIds();

        int updatedRows = sanPhamChiTietRepository.giamSoLuongTon(sanPhamChiTiet.getId(), soLuong);
        if (updatedRows == 0) {
            throw new IllegalArgumentException("Sản phẩm " + sanPhamChiTiet.getIdSanPham().getTenSanPham() +
                    " (ID: " + sanPhamChiTiet.getId() + ") không đủ số lượng tồn kho (yêu cầu: " + soLuong + ").");
        }
        if (cthdCheck != null){
            hoaDonChiTiet_imeiAdminServices.tangSoLuong(cthdCheck,soLuong);
            cthdCheck.setSoLuong(cthdCheck.getSoLuong()+soLuong);
            return chiTietHoaDonRepository.save(cthdCheck);
        }

        //kiem tra imei
        if (imeiIdsFromRequest == null || imeiIdsFromRequest.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn ít nhất một IMEI cho sản phẩm này.");
        }
        if (imeiIdsFromRequest.size() != soLuong) {
            throw new IllegalArgumentException("Số lượng IMEI được chọn (" + imeiIdsFromRequest.size() + ") không khớp với số lượng sản phẩm yêu cầu (" + soLuong + ").");
        }

        List<Imei> imeisDaChon = imeiReposiory.findBySoImeiIn(imeiIdsFromRequest);
        if (imeisDaChon.size() != imeiIdsFromRequest.size()) {
            throw new IllegalArgumentException("Một hoặc nhiều IMEI được chọn không tồn tại trong hệ thống.");
        }
        for (Imei imei : imeisDaChon) {
            if (!imei.getIdSanPhamChiTiet().getId().equals(sanPhamChiTiet.getId())) {
                throw new IllegalArgumentException("IMEI " + imei.getSoImei() + " không thuộc về sản phẩm " + sanPhamChiTiet.getIdSanPham().getTenSanPham() + ".");
            }
            if (imei.getTrangThaiImei() != TrangThaiImei.AVAILABLE) {
                throw new IllegalArgumentException("IMEI " + imei.getSoImei() + " không có sẵn (trạng thái: " + imei.getTrangThaiImei() + ").");
            }
        }

        hoaDonChiTiet_imeiAdminServices.changeStatusImei(imeisDaChon, TrangThaiImei.RESERVED);


        ChiTietHoaDon chiTietHoaDon = toEntity(request,hoaDon,sanPhamChiTiet,donGia);

        ChiTietHoaDon cthdSave = chiTietHoaDonRepository.save(chiTietHoaDon);

        List<ImeiDaBan> imeiDaBans = imeiDaBanAdminServices.generateImeiDaBan(cthdSave,imeisDaChon,TrangThaiImei.RESERVED);
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
            int soLuongTG = soLuongCu - soLuongMoi;
            sanPhamChiTietRepository.tangSoLuongTon(chiTietHoaDon.getIdSanPhamChiTiet().getId(), soLuongTG);
            hoaDonChiTiet_imeiAdminServices.giamSoLuong(chiTietHoaDon,soLuongCu - soLuongMoi);
        }
        if (soLuongCu<soLuongMoi){
            int soLuongTG = soLuongMoi-soLuongCu;
            sanPhamChiTietRepository.giamSoLuongTon(chiTietHoaDon.getIdSanPhamChiTiet().getId(), soLuongTG);
            hoaDonChiTiet_imeiAdminServices.tangSoLuong(chiTietHoaDon,soLuongMoi-soLuongCu);
        }
        chiTietHoaDonRepository.save(chiTietHoaDon);
    }

    @Transactional
    public void removeImeisAndUpdateSoLuong(Integer hoaDonId, Integer hdctId, CthdGiamSoLuong request) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại với ID: " + hoaDonId));
        ChiTietHoaDon chiTietHoaDon = chiTietHoaDonRepository.findById(hdctId)
                .orElseThrow(() -> new IllegalArgumentException("Chi tiết hóa đơn không tồn tại với ID: " + hdctId));

        List<String> imeisToReturn = request.getImeisToReturn();

        if (imeisToReturn == null || imeisToReturn.isEmpty()) {
            throw new IllegalArgumentException("Danh sách IMEI cần loại bỏ không được rỗng.");
        }
        hoaDonChiTiet_imeiAdminServices.giamSoLuong(chiTietHoaDon, imeisToReturn);

        if (chiTietHoaDon.getIdSanPhamChiTiet() == null) {
            throw new IllegalStateException("Sản phẩm chi tiết của hóa đơn không được tìm thấy.");
        }
        sanPhamChiTietRepository.tangSoLuongTon(chiTietHoaDon.getIdSanPhamChiTiet().getId(), imeisToReturn.size());

        List<ImeiDaBan> currentLinkedImeiDaBans = imeiDaBanRepository.findByIdHoaDonChiTiet_Id(chiTietHoaDon.getId());
        Integer soLuongMoi = currentLinkedImeiDaBans.size();

        if (soLuongMoi < 0) {
            soLuongMoi = 0;
        }

        if (soLuongMoi == 0) {
            // chua xu ly ( xoa chi tiet neu so luong ve 0 )
        }

        chiTietHoaDon.setSoLuong(soLuongMoi);
        chiTietHoaDonRepository.save(chiTietHoaDon);

    }


    //ham xoa hoa don chi tiet khoi hoa don
    public void deleleHdct(Integer hdctId){
        ChiTietHoaDon cthdCanXoa = chiTietHoaDonRepository.findById(hdctId).orElseThrow();
        List<ImeiDaBan> imeiDaBanList = cthdCanXoa.getImeiDaBans().stream().toList();

        Integer idSanPhamChiTietDeHoanTra = cthdCanXoa.getIdSanPhamChiTiet().getId(); // Lấy ID của SanPhamChiTiet
        int soLuongHoanTra = cthdCanXoa.getSoLuong();
        sanPhamChiTietRepository.tangSoLuongTon(idSanPhamChiTietDeHoanTra, soLuongHoanTra);

        // Lấy danh sách Imei từ soImei trong imeiDaBan
        List<String> soImeis = imeiDaBanList.stream()
                .map(ImeiDaBan::getSoImei)
                .toList();

        List<Imei> imeiList = imeiReposiory.findAllBySoImeiIn(soImeis);

        hoaDonChiTiet_imeiAdminServices.changeStatusImei(imeiList,TrangThaiImei.AVAILABLE);
        chiTietHoaDonRepository.deleteById(hdctId);
    }


}
