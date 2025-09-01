package org.example.websitetechworld.Services.CommonSerivces.XuLySauBanHangService;

import org.example.websitetechworld.Dto.Request.CommonRequest.ActionBeforeCase.AccepAndInAccepAction;
import org.example.websitetechworld.Dto.Request.CommonRequest.ActionBeforeCase.ChangeStatusRequest;
import org.example.websitetechworld.Dto.Request.CommonRequest.CreateActionBeforeAfter;
import org.example.websitetechworld.Dto.Request.CommonRequest.CreateReturnRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.StatsTrangThaiHoaDon;
import org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse.ActionBeforeCaseResponse;
import org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse.XuLyChiTietResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.ActionAfterCase;
import org.example.websitetechworld.Enum.CaseReason.CaseType;
import org.example.websitetechworld.Enum.HoaDon.HanhDongLichSuHoaDon;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDon.HoaDonAdminService;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.Imei.HoaDonChiTiet_ImeiAdminServices;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final NhanVienRepository nhanVienRepository;
    private final LichSuHoaDonRepository lichSuHoaDonRepository;

    public XuLySauBanHangServices(XuLySauBanHangRepository xuLySauBanHangRepository, LyDoXuLyRepository lyDoXuLyRepository, ChiTietHoaDonRepository chiTietHoaDonRepository, HoaDonRepository hoaDonRepository, ImeiDaBanRepository imeiDaBanRepository, HoaDonChiTiet_ImeiAdminServices hoaDonChiTiet_ImeiAdminServices, SanPhamChiTietRepository sanPhamChiTietRepository, ImeiReposiory imeiReposiory, NhanVienRepository nhanVienRepository, LichSuHoaDonRepository lichSuHoaDonRepository) {
        this.xuLySauBanHangRepository = xuLySauBanHangRepository;
        this.lyDoXuLyRepository = lyDoXuLyRepository;
        this.chiTietHoaDonRepository = chiTietHoaDonRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.imeiDaBanRepository = imeiDaBanRepository;
        this.hoaDonChiTiet_ImeiAdminServices = hoaDonChiTiet_ImeiAdminServices;
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
        this.imeiReposiory = imeiReposiory;
        this.nhanVienRepository = nhanVienRepository;
        this.lichSuHoaDonRepository = lichSuHoaDonRepository;
    }

    public void taoDonHoanTienHuyHang(HoaDon hoaDon){
        XuLySauBanHang xuLySauBanHang = new XuLySauBanHang();
        xuLySauBanHang.setIdHoaDon(hoaDon);
        xuLySauBanHang.setLoaiVuViec(CaseType.CANCELLED);
        xuLySauBanHang.setThoiGianYeuCau(LocalDateTime.now());
        LyDoXuLy lyDoXuLy = lyDoXuLyRepository.findByLoaiVuViec(CaseType.CANCELLED);
        xuLySauBanHang.setIdLyDo(lyDoXuLy);
        xuLySauBanHang.setHanhDongSauVuViec(ActionAfterCase.CANCEL);
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
            createLshd(hoaDon, HanhDongLichSuHoaDon.FALSE_SHIPPING,"Giao thất bại");
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

                    XuLySauBanHang xuLyTonTai = xuLySauBanHangRepository
                            .findByIdImeiDaBan_IdAndIdHoaDon_IdAndHanhDongSauVuViec(
                                    imei.getId(), hoaDon.getId(), ActionAfterCase.CANCEL
                            );
                    if (xuLyTonTai != null) {
                        LyDoXuLy lyDoXuLy = lyDoXuLyRepository.findById(imeiReq.getIdFailReason())
                                .orElseThrow(() -> new IllegalArgumentException("Lý do không tồn tại"));
                        xuLyTonTai.setIdHoaDon(hoaDon);
                        xuLyTonTai.setIdImeiDaBan(imei);
                        xuLyTonTai.setThoiGianYeuCau(LocalDateTime.now());
                        xuLyTonTai.setIdLyDo(lyDoXuLy);
                        xuLyTonTai.setUrlHinh(imeiReq.getUrlHinh());
                        xuLyTonTai.setUrlVideo(imeiReq.getUrlVideo());
                        xuLyTonTai.setHanhDongSauVuViec(ActionAfterCase.PENDING);
                        xuLyTonTai.setDaKiemTra(false);
                        createLshd(hoaDon, HanhDongLichSuHoaDon.RETURN,"Đã yêu cầu trả hàng sau khi bị từ chối với imei "+imei.getSoImei());
                        return xuLyTonTai;
                    }else {
                        LyDoXuLy lyDoXuLy = lyDoXuLyRepository.findById(imeiReq.getIdFailReason())
                                .orElseThrow(() -> new IllegalArgumentException("Lý do không tồn tại"));

                        XuLySauBanHang xuLy = new XuLySauBanHang();
                        xuLy.setIdHoaDon(hoaDon);
                        xuLy.setIdImeiDaBan(imei);
                        xuLy.setLoaiVuViec(lyDoXuLy.getLoaiVuViec());
                        xuLy.setThoiGianYeuCau(LocalDateTime.now());
                        xuLy.setIdLyDo(lyDoXuLy);
                        xuLy.setUrlHinh(imeiReq.getUrlHinh());
                        xuLy.setUrlVideo(imeiReq.getUrlVideo());
                        xuLy.setHanhDongSauVuViec(ActionAfterCase.PENDING);
                        xuLy.setDaKiemTra(false);
                        createLshd(hoaDon, HanhDongLichSuHoaDon.RETURN,"Đã yêu cầu trả hàng với imei "+imei.getSoImei());
                        return xuLy;
                    }


                })
                .collect(Collectors.toList());

        if (!lstXuLySauBanHang.isEmpty()) {
            xuLySauBanHangRepository.saveAll(lstXuLySauBanHang);
        }
    }

    public void createLshd(HoaDon hoaDon,HanhDongLichSuHoaDon action, String moTa){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            Integer nhanVienId = customUserDetails.getId();
            LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
            lichSuHoaDon.setIdHoaDon(hoaDon);
            lichSuHoaDon.setHanhDong(action);
            lichSuHoaDon.setIdNhanVien(nhanVienRepository.findById(nhanVienId).orElse(null));
            lichSuHoaDon.setThoiGianThayDoi(LocalDate.now());
            lichSuHoaDon.setMoTa(moTa);
            lichSuHoaDonRepository.save(lichSuHoaDon);
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

        BigDecimal soTienHoan = BigDecimal.ZERO;

        if (!lstResponse.isEmpty()) {
            XuLyChiTietResponse firstItem = lstResponse.get(0);
            if (firstItem.getTrangThaiDon().equals(ActionAfterCase.RETURN_TO_STOCK)){
                if (CaseType.RETURN.equals(firstItem.getLoaiVuViec())){
                    soTienHoan = tinhTienCanTra(lstResponse);
                } else if (CaseType.FAILED_DELIVERY.equals(firstItem.getLoaiVuViec())) {
                    soTienHoan = tinhTienCanTraGiaoThatBai(lstResponse);
                }
            }
            firstItem.setSoTienHoan(soTienHoan);
        }
        log.info("tien can tra:{}", soTienHoan);
        return lstResponse;
    }

    public BigDecimal tinhTienCanTraGiaoThatBai(List<XuLyChiTietResponse> lstResponse) {
        if (lstResponse == null || lstResponse.isEmpty()) {
            throw new IllegalArgumentException("Danh sách xử lý chi tiết trống");
        }
        Integer idHoaDon = lstResponse.get(0).getIdHoaDon();
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Hoa don ko ton tai"));
        BigDecimal thanhTien = Optional.ofNullable(hoaDon.getThanhTien()).orElse(BigDecimal.ZERO);
        BigDecimal phiShip = Optional.ofNullable(hoaDon.getPhiShip()).orElse(BigDecimal.ZERO);

        BigDecimal soTienCanTra = thanhTien.subtract(phiShip);
        return soTienCanTra;
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
        XuLySauBanHang xuLySauBanHang = null;
        for (String s : request.getSoImeis()){
             xuLySauBanHang = xuLySauBanHangRepository.findXuLySauBanHangByIdImeiDaBan_SoImeiAndIdHoaDon_Id(s,request.getIdHoaDon());
            if (request.getStatus().equals(ActionAfterCase.RETURN_TO_STOCK)){
                ImeiDaBan imeiDaBan = imeiDaBanRepository.findById(xuLySauBanHang.getIdImeiDaBan().getId()).orElseThrow(
                        () -> new IllegalArgumentException("Imei da ban ko ton tai")
                );
                imeiDaBan.setTrangThai(TrangThaiImei.RETURNED);
                imeiDaBanRepository.save(imeiDaBan);
                Imei imei = imeiReposiory.findBySoImei(s);
                imei.setTrangThaiImei(TrangThaiImei.AVAILABLE);
                sanPhamChiTietRepository.tangSoLuongTon(imei.getIdSanPhamChiTiet().getId(),1);

                createLshd(imeiDaBan.getIdHoaDonChiTiet().getIdHoaDon(), HanhDongLichSuHoaDon.RETURN, "Hàng với Imei "+imeiDaBan.getSoImei()+" đã được hoàn về kho");

            }
            xuLySauBanHang.setHanhDongSauVuViec(request.getStatus());
            xuLySauBanHang.setThoiGianXuLy(LocalDateTime.now());
//            xuLySauBanHang.setDaKiemTra(true);
            xuLySauBanHangRepository.save(xuLySauBanHang);

        }
        createLshd(xuLySauBanHang.getIdImeiDaBan().getIdHoaDonChiTiet().getIdHoaDon(), HanhDongLichSuHoaDon.RETURN, "Đã hoàn tiền đơn hàng với mã "+xuLySauBanHang.getIdHoaDon().getMaHoaDon());
    }
    public void changeStatus(AccepAndInAccepAction action) {
        List<XuLySauBanHang> xuLySauBanHangList = xuLySauBanHangRepository.findByIdHoaDon_IdAndAndIdImeiDaBan_SoImei(action.getIdHoaDon(),action.getSoImei());
        List<XuLySauBanHang> listUpdate = new ArrayList<>();
        for (XuLySauBanHang x : xuLySauBanHangList) {
            x.setDaKiemTra(true);
            x.setHanhDongSauVuViec(action.getHanhDong());
            listUpdate.add(x);

            createLshd(x.getIdHoaDon(),HanhDongLichSuHoaDon.UPDATE,action.getHanhDong().getDisplayName()+" yêu cầu trả hàng");
        }
        xuLySauBanHangRepository.saveAll(listUpdate);

    }

    public StatsTrangThaiHoaDon getStatsTrangThaiHoaDon() {
        StatsTrangThaiHoaDon statsTrangThaiHoaDon = new StatsTrangThaiHoaDon();
        statsTrangThaiHoaDon.setTotal(countStatus());
        statsTrangThaiHoaDon.setFailed(countStatus(CaseType.FAILED_DELIVERY));
        statsTrangThaiHoaDon.setReturns(countStatus(CaseType.RETURN));
        statsTrangThaiHoaDon.setResolved(xuLySauBanHangRepository.countByThoiGianXuLy());
        return statsTrangThaiHoaDon;
    }


    private int countStatus() {
        return xuLySauBanHangRepository.countByHanhDongSauVuViec(String.valueOf(ActionAfterCase.PENDING));
    }

    private int countStatus(CaseType caseType) {
        return xuLySauBanHangRepository.countByLoaiVuViec(String.valueOf(caseType));
    }

    public void tuChoiDonHang(Integer idHoaDon){
        xuLySauBanHangRepository.deleteById(idHoaDon);
    }
}
