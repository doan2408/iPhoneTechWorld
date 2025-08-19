package org.example.websitetechworld.Services.CommonSerivces.XuLySauBanHangService;

import org.example.websitetechworld.Dto.Request.CommonRequest.ActionBeforeCase.ChangeStatusRequest;
import org.example.websitetechworld.Dto.Request.CommonRequest.CreateActionBeforeAfter;
import org.example.websitetechworld.Dto.Request.CommonRequest.CreateReturnRequest;
import org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse.ActionBeforeCaseResponse;
import org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse.XuLyChiTietResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.ActionAfterCase;
import org.example.websitetechworld.Enum.CaseReason.CaseType;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.Imei.HoaDonChiTiet_ImeiAdminServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class XuLySauBanHangServices {
    private final XuLySauBanHangRepository xuLySauBanHangRepository;
    private final LyDoXuLyRepository lyDoXuLyRepository;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    private final HoaDonRepository hoaDonRepository;
    private final ImeiDaBanRepository imeiDaBanRepository;
    private final HoaDonChiTiet_ImeiAdminServices hoaDonChiTiet_ImeiAdminServices;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final ImeiReposiory imeiReposiory;
    private static final Logger log = LoggerFactory.getLogger(XuLySauBanHangServices.class);

    public XuLySauBanHangServices(XuLySauBanHangRepository xuLySauBanHangRepository, LyDoXuLyRepository lyDoXuLyRepository, ChiTietHoaDonRepository chiTietHoaDonRepository, HoaDonRepository hoaDonRepository, ImeiDaBanRepository imeiDaBanRepository, HoaDonChiTiet_ImeiAdminServices hoaDonChiTiet_ImeiAdminServices, SanPhamChiTietRepository sanPhamChiTietRepository, ImeiReposiory imeiReposiory) {
        this.xuLySauBanHangRepository = xuLySauBanHangRepository;
        this.lyDoXuLyRepository = lyDoXuLyRepository;
        this.chiTietHoaDonRepository = chiTietHoaDonRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.imeiDaBanRepository = imeiDaBanRepository;
        this.hoaDonChiTiet_ImeiAdminServices = hoaDonChiTiet_ImeiAdminServices;
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
        this.imeiReposiory = imeiReposiory;
    }

    public void taoDonHoanTienHuyHang(HoaDon hoaDon){
        XuLySauBanHang xuLySauBanHang = new XuLySauBanHang();
        xuLySauBanHang.setIdHoaDon(hoaDon);
        xuLySauBanHang.setLoaiVuViec(CaseType.CANCELLED);
        xuLySauBanHang.setThoiGianYeuCau(LocalDateTime.now());
        LyDoXuLy lyDoXuLy = lyDoXuLyRepository.findByLoaiVuViec(CaseType.CANCELLED);
        xuLySauBanHang.setIdLyDo(lyDoXuLy);
        xuLySauBanHang.setHanhDongSauVuViec(ActionAfterCase.PENDING);
        xuLySauBanHang.setDaKiemTra(false);
        xuLySauBanHangRepository.save(xuLySauBanHang);
    }

    public void taoDonXuLySauBanHang(CreateActionBeforeAfter actionBeforeAfter){
        HoaDon hoaDon = hoaDonRepository.getReferenceById(actionBeforeAfter.getIdHoaDon());
        List<ChiTietHoaDon> lstChiTietHoaDon = chiTietHoaDonRepository.findByIdHoaDon_Id(actionBeforeAfter.getIdHoaDon());
        LyDoXuLy lyDoXuLy = lyDoXuLyRepository.findById(actionBeforeAfter.getIdFailReason()).orElseThrow(
                () -> new IllegalArgumentException("Loai ly do ko ton tai")
        );
        if (lstChiTietHoaDon.isEmpty()) {
            return;
        }
        List<XuLySauBanHang> lstXuLySauBanHang = chiTietHoaDonRepository.findByIdHoaDon_Id(actionBeforeAfter.getIdHoaDon())
                .stream()
                .flatMap(ct -> imeiDaBanRepository.findByIdHoaDonChiTiet_Id(ct.getId()).stream()
                        .map(imei -> {
                            XuLySauBanHang xuLy = new XuLySauBanHang();
                            xuLy.setIdHoaDon(hoaDon);
                            xuLy.setIdImeiDaBan(imei);
                            xuLy.setLoaiVuViec(lyDoXuLy.getLoaiVuViec());
                            xuLy.setThoiGianYeuCau(LocalDateTime.now());
                            xuLy.setIdLyDo(lyDoXuLy);
                            xuLy.setHanhDongSauVuViec(ActionAfterCase.PENDING);
                            xuLy.setDaKiemTra(false);
                            return xuLy;
                        })
                )
                .collect(Collectors.toList());
        if (!lstXuLySauBanHang.isEmpty()) {
            xuLySauBanHangRepository.saveAll(lstXuLySauBanHang);
        }
    }
    public void taoDonTraHang(CreateReturnRequest request) {
        HoaDon hoaDon = hoaDonRepository.getReferenceById(request.getIdHoaDon());

        List<XuLySauBanHang> lstXuLySauBanHang = request.getImeis()
                .stream()
                .map(imeiReq -> {
                    ImeiDaBan imei = imeiDaBanRepository.findByIdHoaDonChiTiet_IdAndSoImei(
                            imeiReq.getIdHoaDonChiTiet(),
                            imeiReq.getSoImei()
                    );

                    LyDoXuLy lyDoXuLy = lyDoXuLyRepository.findById(imeiReq.getIdFailReason())
                            .orElseThrow(() -> new IllegalArgumentException("Lý do không tồn tại"));

                    XuLySauBanHang xuLy = new XuLySauBanHang();
                    xuLy.setIdHoaDon(hoaDon);
                    xuLy.setIdImeiDaBan(imei);
                    xuLy.setLoaiVuViec(lyDoXuLy.getLoaiVuViec());
                    xuLy.setThoiGianYeuCau(LocalDateTime.now());
                    xuLy.setIdLyDo(lyDoXuLy);
                    xuLy.setHanhDongSauVuViec(ActionAfterCase.PENDING);
                    xuLy.setDaKiemTra(false);
                    return xuLy;
                })
                .collect(Collectors.toList());

        if (!lstXuLySauBanHang.isEmpty()) {
            xuLySauBanHangRepository.saveAll(lstXuLySauBanHang);
        }
    }

    public Page<ActionBeforeCaseResponse> getAllLyDoXuLy(Integer pageNo, Integer pageSize, String search, CaseType status, String sortDir){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        log.info(">>> getAllLyDoXuLy called with search={}, status={}, sortDir={}, pageNo={}, pageSize={}",
                search, status, sortDir, pageable.getPageNumber(), pageable.getPageSize());
        Page<ActionBeforeCaseResponse> responses =  xuLySauBanHangRepository.getAllLyDoXuLy(search, status, pageable,sortDir);
        log.info("Total:{}", responses.getTotalElements());
        return responses;
    }

    public List<XuLyChiTietResponse> findByIdHoaDon(Integer idHoaDon){
        List<XuLyChiTietResponse> lstResponse = xuLySauBanHangRepository.getAllCtXuLy(idHoaDon);
        log.info("data return:{}", lstResponse);
        BigDecimal soTienHoan = tinhTienCanTra(lstResponse);

        if (!lstResponse.isEmpty()) {
            XuLyChiTietResponse firstItem = lstResponse.get(0);
            firstItem.setSoTienHoan(soTienHoan);
        }
        log.info("tien can tra:{}", soTienHoan);
        return lstResponse;
    }

    public BigDecimal tinhTienCanTra(List<XuLyChiTietResponse> lstResponse) {
        if (lstResponse == null || lstResponse.isEmpty()) {
            throw new IllegalArgumentException("Danh sách xử lý chi tiết trống");
        }
        Integer idHoaDon = lstResponse.get(0).getIdHoaDon();
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Hoa don ko ton tai"));

        // Gán mặc định nếu null
        BigDecimal tongTien = Optional.ofNullable(hoaDon.getTongTien()).orElse(BigDecimal.ZERO);
        BigDecimal soTienGiam = Optional.ofNullable(hoaDon.getSoTienGiam()).orElse(BigDecimal.ZERO);
        BigDecimal thanhTien = Optional.ofNullable(hoaDon.getThanhTien()).orElse(BigDecimal.ZERO);

        BigDecimal dieuKienApDung = BigDecimal.ZERO;
        if (hoaDon.getIdPhieuGiamGia() != null && hoaDon.getIdPhieuGiamGia().getGiaTriDonHangToiThieu() != null) {
            dieuKienApDung = hoaDon.getIdPhieuGiamGia().getGiaTriDonHangToiThieu();
        }

        List<XuLySauBanHang> xuLySauBanHangList =
                xuLySauBanHangRepository.findXuLySauBanHangByIdHoaDon_IdAndHanhDongSauVuViec(
                        idHoaDon, ActionAfterCase.RETURN_TO_STOCK);

        BigDecimal soTienTra = BigDecimal.ZERO;
        for (XuLySauBanHang xuLySauBanHang : xuLySauBanHangList) {
            BigDecimal donGia = Optional.ofNullable(
                    xuLySauBanHang.getIdImeiDaBan()
                            .getIdHoaDonChiTiet()
                            .getDonGia()
            ).orElse(BigDecimal.ZERO);

            soTienTra = soTienTra.add(donGia);
        }

        BigDecimal tongTienSauKhiTra = tongTien.subtract(soTienTra);

        BigDecimal tienHoan;
        if (tongTienSauKhiTra.compareTo(dieuKienApDung) < 0) {
            tienHoan = thanhTien.subtract(tongTienSauKhiTra);
        } else {
            if (tongTien.compareTo(BigDecimal.ZERO) == 0) {
                tienHoan = BigDecimal.ZERO;
            } else {
                BigDecimal giamPhanBo = soTienTra.multiply(soTienGiam).divide(tongTien, 2, RoundingMode.HALF_UP);
                tienHoan = soTienTra.subtract(giamPhanBo);
            }
        }

        return tienHoan.max(BigDecimal.ZERO);
    }



    public void changeStatus(ChangeStatusRequest request) {
        XuLySauBanHang xuLySauBanHang = xuLySauBanHangRepository.findXuLySauBanHangByIdImeiDaBan_SoImei(request.getSoImei());
        if (request.getStatus().equals(ActionAfterCase.RETURN_TO_STOCK)){
            ImeiDaBan imeiDaBan = imeiDaBanRepository.findBySoImei(request.getSoImei());
            imeiDaBan.setTrangThai(TrangThaiImei.RETURNED);
            imeiDaBanRepository.save(imeiDaBan);
            Imei imei = imeiReposiory.findBySoImei(request.getSoImei());
            imei.setTrangThaiImei(TrangThaiImei.AVAILABLE);
            sanPhamChiTietRepository.tangSoLuongTon(imei.getIdSanPhamChiTiet().getId(),1);

        }
        xuLySauBanHang.setHanhDongSauVuViec(request.getStatus());
        xuLySauBanHang.setThoiGianXuLy(LocalDateTime.now());
        xuLySauBanHang.setDaKiemTra(true);
        xuLySauBanHangRepository.save(xuLySauBanHang);
    }
}
