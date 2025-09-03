package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDon;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.DeleteInvoiceRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.ThanhToanAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.PhieuGiamGiaAdminRequest.PhieuGiamGiaAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.InvoiceRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.*;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.KhachHangGiamGiaResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.PhieuGiamGiaAdminResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.ActionAfterCase;
import org.example.websitetechworld.Enum.CaseReason.CaseType;
import org.example.websitetechworld.Enum.GiaoHang.ShippingMethod;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;
import org.example.websitetechworld.Enum.HoaDon.HanhDongLichSuHoaDon;
import org.example.websitetechworld.Enum.HoaDon.LoaiHoaDon;
import org.example.websitetechworld.Enum.HoaDon.TrangThaiThanhToan;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Enum.KhachHang.TrangThaiKhachHang;
import org.example.websitetechworld.Enum.KhuyenMai.DoiTuongApDung;
import org.example.websitetechworld.Enum.KhuyenMai.TrangThaiKhuyenMai;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.Services.AdminServices.GiaoHangAdminServces.GiaoHangAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.Imei.HoaDonChiTiet_ImeiAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.SanPham.HoaDonChiTiet_SanPhamAdminServices;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.ImeiAdminService;
import org.example.websitetechworld.Services.ClientServices.DiemServices.LichSuDiemService;
import org.example.websitetechworld.Services.CommonSerivces.EmailCommonService.EmailServicces;
import org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices.ThanhToanFactory;
import org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices.ThanhToanStrategy;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.example.websitetechworld.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HoaDonAdminService {
    private final HoaDonRepository hoaDonRepository;
    private final LichSuHoaDonRepository lichSuHoaDonRepository;
    private final ChiTietThanhToanRepository chiTietThanhToanRepository;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    private final KhachHangRepository khachHangRepository;
    private static final Logger logger = LoggerFactory.getLogger(HoaDonAdminService.class);
    private final ThanhToanFactory thanhToanFactory;
    private final ImeiAdminService imeiAdminService;
    private final HoaDonChiTiet_ImeiAdminServices hoaDonChiTiet_ImeiAdminServices;
    private final HoaDonChiTiet_SanPhamAdminServices hoaDonChiTiet_sanPhamAdminServices;
    private final PhieuGiamGiaRepository phieuGiamGiaRepository;
    private final EntityManager entityManager;
    private final EmailServicces emailServicces;
    private final GiaoHangAdminServices giaoHangAdminServices;
    private final KhachHangGiamGiaRepository khachHangGiamGiaRepository;
    private final XuLySauBanHangRepository xuLySauBanHangRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;
    private final GioHangRepository gioHangRepository;
    private final ViDiemRepository viDiemRepository;
    private final NhanVienRepository nhanVienRepository;
    private final LichSuDiemService lichSuDiemService;

    public HoaDonAdminService(HoaDonRepository hoaDonRepository, LichSuHoaDonRepository lichSuHoaDonRepository, ChiTietThanhToanRepository chiTietThanhToanRepository, ChiTietHoaDonRepository chiTietHoaDonRepository, KhachHangRepository khachHangRepository, ThanhToanFactory thanhToanFactory, ImeiAdminService imeiAdminService, HoaDonChiTiet_ImeiAdminServices hoaDonChiTiet_ImeiAdminServices, HoaDonChiTiet_SanPhamAdminServices hoaDonChiTietSanPhamAdminServices, PhieuGiamGiaRepository phieuGiamGiaRepository, EntityManager entityManager, EmailServicces emailServicces, GiaoHangAdminServices giaoHangAdminServices, XuLySauBanHangRepository xuLySauBanHangRepository, KhachHangGiamGiaRepository khachHangGiamGiaRepository, SanPhamChiTietRepository sanPhamChiTietRepository, GioHangRepository gioHangRepository, ViDiemRepository viDiemRepository,
                              NhanVienRepository nhanVienRepository, LichSuDiemService lichSuDiemService) {
        this.hoaDonRepository = hoaDonRepository;
        this.lichSuHoaDonRepository = lichSuHoaDonRepository;
        this.chiTietThanhToanRepository = chiTietThanhToanRepository;
        this.chiTietHoaDonRepository = chiTietHoaDonRepository;
        this.khachHangRepository = khachHangRepository;
        this.thanhToanFactory = thanhToanFactory;
        this.imeiAdminService = imeiAdminService;
        this.hoaDonChiTiet_ImeiAdminServices = hoaDonChiTiet_ImeiAdminServices;
        hoaDonChiTiet_sanPhamAdminServices = hoaDonChiTietSanPhamAdminServices;
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
        this.entityManager = entityManager;
        this.emailServicces = emailServicces;
        this.giaoHangAdminServices = giaoHangAdminServices;
        this.khachHangGiamGiaRepository = khachHangGiamGiaRepository;
        this.xuLySauBanHangRepository = xuLySauBanHangRepository;
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
        this.gioHangRepository = gioHangRepository;
        this.viDiemRepository = viDiemRepository;
        this.nhanVienRepository = nhanVienRepository;
        this.lichSuDiemService = lichSuDiemService;
    }

    public List<HoaDonAdminResponse> getAllHoaDon(){
        return hoaDonRepository.findAll().stream().map(HoaDonAdminResponse::convertDto).collect(Collectors.toList());
    }

    public Page<GetAllHoaDonAdminResponse> getPageHoaDon(
            Integer pageNo, Integer pageSize, String sortBy, String direction,
            String keyword,
            TrangThaiThanhToan trangThai,
            LoaiHoaDon loaiHoaDon,
            LocalDate from,
            LocalDate to
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(dir, sortBy));

        LocalDateTime fromDateTime = null;
        LocalDateTime toDateTime = null;

        if (from != null && to != null) {
            fromDateTime = from.atStartOfDay();
            toDateTime = to.atTime(LocalTime.MAX);
        } else if (from != null) {
            fromDateTime = from.atStartOfDay();
            toDateTime = LocalDate.now().atTime(LocalTime.MAX); // hoặc LocalDateTime.MAX
        } else if (to != null) {
            fromDateTime = LocalDate.of(1970, 1, 1).atStartOfDay(); // hoặc LocalDateTime.MIN
            toDateTime = to.atTime(LocalTime.MAX);
        }


        return hoaDonRepository
                .findByIsDeleteIsFalseOrIsDeleteIsNull(keyword,trangThai,loaiHoaDon,fromDateTime,toDateTime,pageable)
                .map(GetAllHoaDonAdminResponse::convertDto);
    }

    public Page<HoaDonAdminResponse> getPageLichSuBanHang (Integer pageNo, Integer pageSize, String sortBy, String direction, String search) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(dir, sortBy));
        return hoaDonRepository
                .findAllLichSuBanHang(TrangThaiThanhToan.COMPLETED, search, pageable)
                .map(HoaDonAdminResponse::convertDto);
    }

    public List<HoaDonAdminResponse> exportExcel(){
        List<HoaDon> hoaDonList = hoaDonRepository.findAllInCurrentMonth();
        return hoaDonList.stream().map(HoaDonAdminResponse::convertDto).toList();
    }

    public HoaDonAdminResponse findById(Integer id){
        return hoaDonRepository.findById(id)
                .map(hoaDon -> {
                    HoaDonAdminResponse hoaDonAdminResponse = HoaDonAdminResponse.convertDto(hoaDon);
                    hoaDonAdminResponse.setChiTietHoaDonAdminResponseList(
                            hoaDonAdminResponse.getChiTietHoaDonAdminResponseList()
                                    .stream().map(response -> {
                                        response.setDonGia(tinhGiaKhuyenMai(response.getIdSanPhamChiTiet(), hoaDonAdminResponse.getIdKhachHang()));
                                        return response;
                                    }).collect(Collectors.toList())
                    );
                    return hoaDonAdminResponse;
                })
                .orElseThrow(() -> new RuntimeException("Khong tim thay hoa don"));
    }

    public HoaDon findByIdHoaDon(Integer id){
        return hoaDonRepository.findById(id).orElseThrow();
    }

    public List<LichSuHoaDonAdminResponse> getPageLichSuHoaDon(Integer hoaDonId){
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn có id:" + hoaDonId));
        return lichSuHoaDonRepository.findByIdHoaDon(hoaDon)
                .stream().sorted(Comparator.comparing(LichSuHoaDon::getId).reversed())
                .map(LichSuHoaDonAdminResponse::convertDto)
                .collect(Collectors.toList());
    }

    public List<XuLySauBanHangResponse> getYeuCau (Integer hoaDonId){
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId).orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hóa đơn có id:" + hoaDonId));
        return xuLySauBanHangRepository.findXuLySauBanHangByIdHoaDon_Id(hoaDon.getId())
                .stream().sorted(Comparator.comparing(XuLySauBanHang::getId).reversed())
                .map(xuLySauBanHang -> {
                    XuLySauBanHangResponse response = new XuLySauBanHangResponse();
                    response.setId(xuLySauBanHang.getId());
                    response.setIdHoaDon(xuLySauBanHang.getIdHoaDon().getId());
                    response.setIdImeiDaBan(xuLySauBanHang.getIdImeiDaBan().getId());
                    response.setSoImei(xuLySauBanHang.getIdImeiDaBan().getSoImei());
                    response.setCaseType(xuLySauBanHang.getLoaiVuViec());
                    response.setLoaiVuViec(xuLySauBanHang.getLoaiVuViec().getDisplayName());
                    response.setIdLyDoXuLy(xuLySauBanHang.getIdLyDo().getId());
                    response.setLyDoXuLy(xuLySauBanHang.getIdLyDo().getTenLyDo());
                    response.setActionAfterCase(xuLySauBanHang.getHanhDongSauVuViec());
                    response.setHanhDongSauVuViec(xuLySauBanHang.getHanhDongSauVuViec().getDisplayName());
                    response.setDaKiemTra(xuLySauBanHang.getDaKiemTra());
                    response.setThoiGianXuLy(xuLySauBanHang.getThoiGianXuLy());
                    response.setThoiGianYeuCau(xuLySauBanHang.getThoiGianYeuCau());
                    response.setUrlHinh(xuLySauBanHang.getUrlHinh());
                    response.setUrlVideo(xuLySauBanHang.getUrlVideo());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<ChiTietThanhToanAdminResponse> getPageChiTietThanhToan(Integer hoaDonId,Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return chiTietThanhToanRepository.findByIdHoaDon_Id(hoaDonId,pageable).stream().map(ChiTietThanhToanAdminResponse::convertDto).toList();
    }

    @Transactional
    public HoaDon createPendingInvoice(){

        HoaDon hoaDon = new HoaDon();
        hoaDon.setNgayTaoHoaDon(LocalDateTime.now());
        hoaDon.setLoaiHoaDon(LoaiHoaDon.POS);
        hoaDon.setPhiShip(BigDecimal.ZERO);
        hoaDon.setSoTienGiam(BigDecimal.ZERO);
        hoaDon.setTrangThaiThanhToan(TrangThaiThanhToan.PENDING);
        hoaDon.setIsDelete(false);
        hoaDon =  hoaDonRepository.save(hoaDon);
        // Refresh để lấy giá trị maHoaDon từ DB (vì nó là cột computed)
        entityManager.refresh(hoaDon);
        return hoaDon;
    }

    public void updateTongTien(Integer hoaDonId){
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId).orElseThrow();

        List<ChiTietHoaDon> chiTietHoaDonList = chiTietHoaDonRepository.findByIdHoaDon_Id(hoaDonId);

        BigDecimal tongTien = chiTietHoaDonList.stream()
                .map(ct -> ct.getDonGia().multiply(BigDecimal.valueOf(ct.getSoLuong())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        hoaDon.setTongTien(tongTien);
        BigDecimal thanhTien = tongTien.subtract(hoaDon.getSoTienGiam()).add(hoaDon.getPhiShip());
        hoaDon.setThanhTien(thanhTien);

        hoaDonRepository.save(hoaDon);
    }


    public void selectKhachHang(Integer hoaDonId, Integer khachHangId){
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonId)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại"));

        if (khachHangId == null){
            hoaDon.setIdKhachHang(null);
            createLshd(hoaDon,HanhDongLichSuHoaDon.UPDATE,"Xóa khách hàng");
        } else {
            KhachHang khachHang = khachHangRepository.findById(khachHangId)
                    .orElseThrow(()-> new IllegalArgumentException("Khách hang nay khong ton tai"));

            String diaChiDayDu = khachHang.getDiaChis().stream()
                    .filter(DiaChi::getDiaChiChinh)
                    .findFirst()
                    .map(DiaChi::getDiaChiDayDu)
                    .orElse(null);

            hoaDon.setIdKhachHang(khachHang);
//        hoaDon.setTenNguoiNhan(khachHang.getTenKhachHang());
//        hoaDon.setDiaChi(diaChiDayDu);
            hoaDon.setIdKhachHang(khachHang);
            hoaDon.setTenNguoiMua(khachHang.getTenKhachHang());
            hoaDon.setSdtNguoiMua(khachHang.getSdt());

            createLshd(hoaDon,HanhDongLichSuHoaDon.UPDATE,"Thêm khách hàng "+khachHang.getTenKhachHang());
        }

        hoaDonRepository.save(hoaDon);
    }

    @Transactional
    public void xuLyPhieuGiamGia(Integer idHoaDon, PhieuGiamGiaAdminRequest phieuGiamGiaAdminRequest) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại: " + idHoaDon));

        Integer idPhieuGiamGia = phieuGiamGiaAdminRequest.getId();
        if (idPhieuGiamGia == null) {
            throw new IllegalArgumentException("Id phiếu giảm giá không được null");
        }

        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(idPhieuGiamGia)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu giảm giá với ID: " + idPhieuGiamGia));

        hoaDon.setIdPhieuGiamGia(phieuGiamGia);
        hoaDonRepository.save(hoaDon);
    }

    @Transactional
    public void deletePhieuGiamGia(Integer idHoaDon){
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại: " + idHoaDon));

        hoaDon.setIdPhieuGiamGia(null);
        hoaDonRepository.save(hoaDon);
    }


    public ThanhToanAdminResponse xuLyThanhToan(Integer idHoaDon, ThanhToanAdminRequest request){
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại: " + idHoaDon));

        if (hoaDon.getTrangThaiThanhToan() == TrangThaiThanhToan.PAID) {
            throw new IllegalArgumentException("Hóa đơn đã được thanh toán.");
        }
        if (hoaDon.getSoTienGiam() != null){
            hoaDon.setSoTienGiam(request.getSoTienGiam());
        }
        if (hoaDon.getIdPhieuGiamGia() != null && hoaDon.getIdKhachHang() != null){
            KhachHangGiamGia khgg = khachHangGiamGiaRepository.findByIdPhieuGiamGiaAndIdKhachHangAndIsUser(
                    hoaDon.getIdPhieuGiamGia(), hoaDon.getIdKhachHang(), false);
            if (khgg != null) {
                khgg.setIsUser(true);
                khachHangGiamGiaRepository.save(khgg);
            }
        }
        String hinhThucThanhToan = request.getHinhThucThanhToan().name();
        ThanhToanStrategy thanhToanStrategy = thanhToanFactory.getStrategy(hinhThucThanhToan);
        ThanhToanAdminResponse response = thanhToanStrategy.thanhToan(hoaDon,request);
        if (hoaDon.getIsShipping() == null || !hoaDon.getIsShipping()){
            createLshd(hoaDon,HanhDongLichSuHoaDon.COMPLETE,"Đơn hàng đã hoàn thành");
        }

        if (response.getMessage().equals("Thanh toán thành công") && Boolean.TRUE.equals(hoaDon.getIsShipping())) {
            hoaDonChiTiet_ImeiAdminServices.updateImeiStautusFromHoaDon(hoaDon.getChiTietHoaDons().stream().toList(), TrangThaiImei.RESERVED);
            updateTongTien(hoaDon.getId());
        }

        return response;
    }

    public void hoaDonSoftDelete (Integer id){
        HoaDon hoaDon = hoaDonRepository.findById(id).orElseThrow();

        boolean hasDetails = chiTietHoaDonRepository.existsByIdHoaDon(hoaDon);
        if (hasDetails) {
            throw new IllegalStateException("Hóa đơn này đã có sản phẩm, không thể xóa.");
        }

        hoaDon.setIsDelete(true);
        hoaDonRepository.save(hoaDon);
    }

    public void hoaDonHardDelete (Integer id){
        HoaDon hoaDon = hoaDonRepository.findById(id).orElseThrow();

        boolean hasDetails = chiTietHoaDonRepository.existsByIdHoaDon(hoaDon);
        if (hasDetails) {
            throw new IllegalStateException("Hóa đơn này đã có sản phẩm, không thể xóa.");
        }

        hoaDonRepository.delete(hoaDon);
    }

    public Long countHoaDon () {
        return hoaDonRepository.countByIsDeleteIsNullOrIsDeleteIsFalse();
    }

    public Integer countHoaDonPending () {
        return hoaDonRepository.countPending(TrangThaiThanhToan.PENDING);
    }

    public BigDecimal doangThuThang () {
        return hoaDonRepository.doanhThuThang();
    }

    public Page<KhachHangGiamGiaResponse> getAllKhachHang (String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KhachHang> khachHangPage = (search == null || search.isEmpty()) ?
                khachHangRepository.findTrangThai_Active(pageable) :
                khachHangRepository.findByTenKhachHangContainingIgnoreCaseOrMaKhachHangContainingIgnoreCaseOrSdtContainingIgnoreCaseAndTrangThai_Active(search, pageable);
        return khachHangPage.map(kh -> new KhachHangGiamGiaResponse(kh.getId(),kh.getMaKhachHang(), kh.getSdt(), kh.getTenKhachHang()));
    }

    public KhachHang addKhachHang (KhachHang khachHang) {
        KhachHang saved = new KhachHang();
        checkValidate(khachHang);
        saved.setTenKhachHang(khachHang.getTenKhachHang());
        saved.setSdt(khachHang.getSdt());
        saved.setEmail(khachHang.getEmail());
        saved.setTrangThai(TrangThaiKhachHang.ACTIVE);

        saved = khachHangRepository.save(saved);
        updateKhachHang(saved);

        return saved;
    }

    public void updateKhachHang (KhachHang khachHang) {
        HangThanhVien hangThanhVien = new HangThanhVien();
        hangThanhVien.setId(1);
        khachHang.setHangThanhVien(hangThanhVien);
        khachHangRepository.save(khachHang);

        GioHang gioHang = new GioHang();
        gioHang.setIdKhachHang(khachHang);
        gioHangRepository.save(gioHang);

        ViDiem viDiem = new ViDiem();
        viDiem.setKhachHang(khachHang);
        viDiem.setDiemKhaDung(new BigDecimal(0));
        viDiemRepository.save(viDiem);
    }

    public void checkValidate (KhachHang khachHang) {
        if (khachHangRepository.existsByEmail(khachHang.getEmail())) throw new RuntimeException("Email đã tồn tại!");
        if (khachHangRepository.existsBySdt(khachHang.getSdt())) throw new RuntimeException("Số điện thoại đã tồn tại!");
    }

    @Transactional
    public void updateInvoice(Integer id, InvoiceRequest request) {
        HoaDon invoice = hoaDonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));

        // Gán giá trị để tránh lỗi validation
        invoice.setTenNguoiNhan(request.getTenNguoiNhan() != null ? request.getTenNguoiNhan() : "");
        invoice.setSdtNguoiNhan(request.getSdtNguoiNhan() != null ? request.getSdtNguoiNhan() : "");
        invoice.setDiaChiGiaoHang(request.getDiaChiGiaoHang() != null ? request.getDiaChiGiaoHang() : "");
        invoice.setPhiShip(request.getPhiShip() != null ? request.getPhiShip() : BigDecimal.ZERO);
        invoice.setIsShipping(request.getIsShipping() != null ? request.getIsShipping() : false);
        invoice.setNgayDatHang(LocalDateTime.now());
        invoice.setEmailNguoiNhan(request.getEmailNguoiNhan() != null ? request.getEmailNguoiNhan() : "");

        // Gán shippingMethod
        invoice.setShippingMethod(request.getShippingMethod() != null ?
                ShippingMethod.valueOf(request.getShippingMethod()) : ShippingMethod.EXPRESS);



        // Tính thanhTien
        BigDecimal tongTien = invoice.getTongTien() != null ? invoice.getTongTien() : BigDecimal.ZERO;
        BigDecimal soTienGiam = invoice.getSoTienGiam() != null ? invoice.getSoTienGiam() : BigDecimal.ZERO;
        BigDecimal phiShip = invoice.getPhiShip() != null ? invoice.getPhiShip() : BigDecimal.ZERO;
        BigDecimal thanhTien = tongTien.add(phiShip).subtract(soTienGiam);
        invoice.setThanhTien(thanhTien);

        try {
            // Lưu entity để kiểm tra validation
            hoaDonRepository.save(invoice);
            // Thực thi query JPQL
            System.out.println("sdt nguoi mua"+invoice.getSdtNguoiNhan());
            hoaDonRepository.updateInvoice(
                    id,
                    invoice.getTenNguoiNhan(),
                    invoice.getSdtNguoiNhan(),
                    invoice.getDiaChiGiaoHang(),
                    invoice.getPhiShip(),
                    invoice.getIsShipping(),
                    invoice.getMaVanDon(),
                    invoice.getThanhTien(),
                    invoice.getShippingMethod(),
                    TrangThaiGiaoHang.PENDING,
                    invoice.getEmailNguoiNhan(),
                    invoice.getNgayDatHang()
            );
            createLshd(invoice,HanhDongLichSuHoaDon.UPDATE,"Cập nhật thông tin giao hàng");
        } catch (Exception e) {
            System.err.println("Error executing JPQL query for invoice id " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to execute JPQL query: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteInvocieShipping(Integer id){
        HoaDon invoice = hoaDonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found with id: " + id));

        invoice.setTenNguoiNhan(null);
        invoice.setSdtNguoiNhan(null);
        invoice.setEmailNguoiNhan(null);
        invoice.setDiaChiGiaoHang(null);
        invoice.setThanhTien(invoice.getThanhTien().subtract(invoice.getPhiShip()));
        invoice.setPhiShip(BigDecimal.ZERO);
        invoice.setIsShipping(false);
        invoice.setShippingMethod(null);

        hoaDonRepository.save(invoice);

        createLshd(invoice,HanhDongLichSuHoaDon.UPDATE,"Thay đổi không giao hàng và xóa thông tin");
    }

    public void updateMaVanDon(Integer idHoaDon) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon).orElseThrow(
                () -> new IllegalArgumentException(("Hoa don ko ton tai"))
        );
        hoaDon.setMaVanDon(generateMaVanDon(idHoaDon));
        sendMailByyMaVanDon(hoaDon);
        hoaDonRepository.save(hoaDon);
    }

    private String generateMaVanDon(Integer invoiceId) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder suffix = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            suffix.append(characters.charAt(random.nextInt(characters.length())));
        }
        return "VD" + invoiceId + "-" + suffix;
    }
    public void exportExcelToResponse(HttpServletResponse response) throws IOException {
        List<HoaDonAdminResponse> hoaDonAdminResponses = exportExcel();

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Danh sách hóa đơn");

        // Tạo header
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Mã hóa đơn");
        header.createCell(2).setCellValue("Mã vận đơn");
        header.createCell(3).setCellValue("Id tài khoản");
        header.createCell(4).setCellValue("Mã tài khoản");
        header.createCell(5).setCellValue("Tên tài khoản");
        header.createCell(6).setCellValue("Hạng khách hàng");
        header.createCell(7).setCellValue("Id phiếu giam giá");
        header.createCell(8).setCellValue("Mã phiếu giảm giá");
        header.createCell(9).setCellValue("Tên phiếu giảm giá");
        header.createCell(10).setCellValue("Tên người mua");
        header.createCell(11).setCellValue("Sdt người mua");
        header.createCell(12).setCellValue("Tên người nhận");
        header.createCell(13).setCellValue("Sdt người nhận");
        header.createCell(14).setCellValue("Email người nhận");
        header.createCell(15).setCellValue("Phí Ship");
        header.createCell(16).setCellValue("Tổng tiền");
        header.createCell(17).setCellValue("Số tiền giảm");
        header.createCell(18).setCellValue("Thành tiền");
        header.createCell(19).setCellValue("Ngày tạo");
        header.createCell(20).setCellValue("Ngày đặt hàng");
        header.createCell(21).setCellValue("Địa chỉ giao hàng");
        header.createCell(22).setCellValue("Phương thức giao hàng");
        header.createCell(23).setCellValue("Loại hóa đơn");
        header.createCell(24).setCellValue("isShipping");
        header.createCell(25).setCellValue("Ngày thanh toán");
        header.createCell(26).setCellValue("Trạng thái đơn hàng");
        header.createCell(27).setCellValue("Trạng thái thanh toán");
        // ... tạo các header còn lại

        int rowNum = 1;
        for (HoaDonAdminResponse hd : hoaDonAdminResponses) {
            Row row = sheet.createRow(rowNum++);
            createCell(row, 0, hd.getIdHoaDon());
            createCell(row, 1, hd.getMaHoaDon());
            createCell(row, 2, hd.getMaVanDon());
            createCell(row, 3, hd.getIdKhachHang());
            createCell(row, 4, hd.getMaKhachHang());
            createCell(row, 5, hd.getTenKhachHang());
            createCell(row, 6, hd.getHangKhachHang());
            createCell(row, 7, hd.getIdPhieuGiamGia());
            createCell(row, 8, hd.getMaPhieuGiamGia());
            createCell(row, 9, hd.getTenPhieuGiamGia());
            createCell(row, 10, hd.getTenNguoiMua());
            createCell(row, 11, hd.getSdtNguoiMua());
            createCell(row, 12, hd.getTenNguoiNhan());
            createCell(row, 13, hd.getSdtNguoiNhan());
            createCell(row, 14, hd.getEmailNguoiNhan());
            createCell(row, 15, hd.getPhiShip());
            createCell(row, 16, hd.getTongTien());
            createCell(row, 17, hd.getSoTienGiam());
            createCell(row, 18, hd.getThanhTien());
            createCell(row, 19, hd.getNgayTao());
            createCell(row, 20, hd.getNgayDatHang());
            createCell(row, 21, hd.getDiaChiGiaoHang());
            createCell(row, 22, hd.getShippingMethod());
            createCell(row, 23, hd.getLoaiHoaDon());
            createCell(row, 24, hd.getIsShipping());
            createCell(row, 25, hd.getNgayThanhToan());
            createCell(row, 26, hd.getTrangThaiDonHang());
            createCell(row, 27, hd.getTrangThaiThanhToan());
        }


        workbook.write(response.getOutputStream());
        workbook.close();
    }
    private void createCell(Row row, int column, Object value) {
        Cell cell = row.createCell(column);
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof java.util.Date) {
            cell.setCellValue((java.util.Date) value);
        } else if (value instanceof java.time.LocalDateTime) {
            cell.setCellValue(((LocalDateTime) value).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        } else if (value instanceof java.time.LocalDate) {
            cell.setCellValue(((LocalDate) value).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            cell.setCellValue(value.toString());
        }
    }

    public void sendMailByyMaVanDon(HoaDon hoaDon){
        String maHoaDon = null;
        String subject = "Xác nhận đơn hàng #" + hoaDon.getMaVanDon();

        String html = "<h2 style='color:#2c3e50;'>Cảm ơn bạn đã đặt hàng!</h2>"
                + "<p>Xin chào <b>" + hoaDon.getTenNguoiNhan() + "</b>,</p>"
                + "<p>Đơn hàng <b>" + hoaDon.getMaHoaDon() + "</b> của bạn đã được xác nhận vào ngày "
                + LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ".</p>"
                + "<p><i>Chúng tôi sẽ sớm liên hệ với bạn để giao hàng.</i></p>"
                + "<br><p>Trân trọng,</p><p>Đội ngũ bán hàng</p>";

        emailServicces.sendHtmlEmail(hoaDon.getEmailNguoiNhan(), subject, html);
    }

    public void updateStatus(Integer idHoaDon, TrangThaiThanhToan newStatus) {
        Optional<HoaDon> optionalHoaDon = hoaDonRepository.findById(idHoaDon);
        if (!optionalHoaDon.isPresent()) {
            throw new IllegalArgumentException("Không tìm thấy hoa don hàng với ID: " + idHoaDon);
        }
        HoaDon hoaDon = optionalHoaDon.get();
        hoaDon.setTrangThaiThanhToan(newStatus);
        List<ChiTietHoaDon> danhSachChiTiet = giaoHangAdminServices.getHoaDonChiTietByMaHoaDon(hoaDon.getMaHoaDon());

        if (newStatus.equals(TrangThaiThanhToan.PAID)){
            List<ChiTietThanhToan> chiTietThanhToanList = chiTietThanhToanRepository.findByIdHoaDon_Id(hoaDon.getId());
            for (ChiTietThanhToan chiTietThanhToan : chiTietThanhToanList){
                chiTietThanhToan.setThoiGianThanhToan(LocalDateTime.now());
            }
            chiTietThanhToanRepository.saveAll(chiTietThanhToanList);
            createLshd(hoaDon,HanhDongLichSuHoaDon.THANH_TOAN,"Đã nhận được tiền");
        }
        if (TrangThaiGiaoHang.DELIVERED.equals(hoaDon.getTrangThaiDonHang()) && TrangThaiThanhToan.PAID.equals(hoaDon.getTrangThaiThanhToan())) {
            hoaDon.setTrangThaiThanhToan(TrangThaiThanhToan.COMPLETED);
            hoaDonChiTiet_ImeiAdminServices.updateImeiStautusFromHoaDon(danhSachChiTiet, TrangThaiImei.SOLD);
            createLshd(hoaDon,HanhDongLichSuHoaDon.COMPLETE,"Đơn hàng đã hoàn thành");
            lichSuDiemService.congDiemTuHoaDon(hoaDon.getId());
        }
        if (TrangThaiThanhToan.REFUNDED.equals(newStatus) && TrangThaiGiaoHang.CANCELLED.equals(hoaDon.getTrangThaiDonHang())){
            XuLySauBanHang xuLySauBanHang = xuLySauBanHangRepository.findByIdHoaDon_IdAndLoaiVuViec(hoaDon.getId(), CaseType.CANCELLED);
            xuLySauBanHang.setThoiGianXuLy(LocalDateTime.now());
            xuLySauBanHang.setHanhDongSauVuViec(ActionAfterCase.REFUND);
            xuLySauBanHang.setDaKiemTra(true);
            xuLySauBanHangRepository.save(xuLySauBanHang);
            createLshd(hoaDon,HanhDongLichSuHoaDon.HUY,"Tạo yêu cầu hủy đơn thành công");
        }
        if (TrangThaiGiaoHang.CANCELLED.equals(hoaDon.getTrangThaiDonHang()) && TrangThaiThanhToan.REFUNDED.equals(newStatus)){
            hoaDonChiTiet_ImeiAdminServices.updateImeiHoanTien(danhSachChiTiet, TrangThaiImei.AVAILABLE,TrangThaiImei.RETURNED);
            createLshd(hoaDon,HanhDongLichSuHoaDon.UPDATE,"Hoàn thành công cho khách hàng "+hoaDon.getThanhTien()+" VND");
        }

        hoaDonRepository.save(hoaDon);
    }

    public List<PhieuGiamGiaAdminResponse> layDanhSachPhieuGiamGiaCuaKhach(
            String timKiem, Integer idKhachHang, BigDecimal giaTriDonHangToiThieu) {

        if (idKhachHang == null) return Collections.emptyList();

        List<PhieuGiamGia> phieuList = khachHangGiamGiaRepository.findPhieuGiamGiaCuaKhach(
                idKhachHang, giaTriDonHangToiThieu, timKiem);

        List<PhieuGiamGiaAdminResponse> result = new ArrayList<>();
        for (PhieuGiamGia pgg : phieuList) {
            PhieuGiamGiaAdminResponse resp = new PhieuGiamGiaAdminResponse();
            resp.setId(pgg.getId());
            resp.setMaGiamGia(pgg.getMaGiamGia());
            resp.setTenGiamGia(pgg.getTenGiamGia());
            resp.setSoLuong(pgg.getSoLuong());
            resp.setGiaTriGiamGia(pgg.getGiaTriGiamGia());
            resp.setGiaTriDonHangToiThieu(pgg.getGiaTriDonHangToiThieu());
            resp.setGiaTriGiamGia(pgg.getGiaTriGiamGia());
            resp.setGiaTriGiamGiaToiDa(pgg.getGiaTriGiamGiaToiDa());
            resp.setLoaiGiamGia(pgg.getLoaiGiamGia());
            resp.setNgayBatDau(pgg.getNgayBatDau());
            resp.setNgayKetThuc(pgg.getNgayKetThuc());
            resp.setTrangThaiPhatHanh(pgg.getTrangThaiPhatHanh());
            resp.setTrangThaiPhieuGiamGia(pgg.getTrangThaiPhieuGiamGia());
            resp.setSoDiemCanDeDoi(pgg.getSoDiemCanDeDoi());
            resp.setDieuKienApDung(pgg.getDieuKienApDung());
            resp.setHangToiThieu(String.valueOf(pgg.getHangToiThieu()));
            result.add(resp);
        }
        return result;
    }

    private BigDecimal tinhGiaKhuyenMai(Integer idSpct, Integer selectedIdKhachHang) {
        try {
            if (idSpct == null) {
                return BigDecimal.ZERO;
            }
            SanPhamChiTiet spct = sanPhamChiTietRepository.findById(idSpct)
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm chi tiết với ID: " + idSpct));

            BigDecimal giaGoc = spct.getGiaBan();
            if (giaGoc == null) {
                return BigDecimal.ZERO;
            }
            List<KhuyenMai> danhSachKhuyenMai = spct.getDanhSachKhuyenMai().stream()
                    .map(KhuyenMaiSanPhamChiTiet::getIdKhuyenMai)
                    .toList();
            LocalDateTime hienTai = LocalDateTime.now();
            KhuyenMai khuyenMai = danhSachKhuyenMai.stream()
                    .filter(km -> km.getNgayBatDau() != null && km.getNgayKetThuc() != null)
                    .filter(km -> !km.getNgayBatDau().isAfter(hienTai) && !km.getNgayKetThuc().isBefore(hienTai))
                    .findFirst()
                    .orElse(null);
            if (khuyenMai == null) {
                return giaGoc;
            }
            if (khuyenMai.getTrangThai() != TrangThaiKhuyenMai.ACTIVE) {
                return giaGoc;
            }
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(khuyenMai.getNgayBatDau()) || now.isAfter(khuyenMai.getNgayKetThuc())) {
                return giaGoc;
            }

            Integer discountValue = Optional.ofNullable(khuyenMai.getPhanTramGiam()).orElse(0);
            BigDecimal tyLeGiam = BigDecimal.valueOf(discountValue)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

            DoiTuongApDung doiTuong = khuyenMai.getDoiTuongApDung();
            if (doiTuong == DoiTuongApDung.ALL) {
                return tinhGiaKhuyenMai(giaGoc, tyLeGiam);
            }
            if (selectedIdKhachHang == null || selectedIdKhachHang == 0) {
                return giaGoc;
            }
            boolean khachHangCu = hoaDonRepository.countHoaDonByIdKhachHang(selectedIdKhachHang) > 0;
            boolean khongHopLe =
                    (doiTuong == DoiTuongApDung.NEW_CUSTOMER && khachHangCu) ||
                            (doiTuong == DoiTuongApDung.OLD_CUSTOMER && !khachHangCu);
            if (khongHopLe) {
                return giaGoc;
            }

            return tinhGiaKhuyenMai(giaGoc, tyLeGiam);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal tinhGiaKhuyenMai (BigDecimal giaGoc, BigDecimal tyLeGiam) {
        return giaGoc.subtract(giaGoc.multiply(tyLeGiam)).max(BigDecimal.ZERO);
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

    public void updateShippingMethod (Integer idHoaDon) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại"));
        hoaDon.setShippingMethod(ShippingMethod.STANDARD);
        hoaDonRepository.save(hoaDon);
    }
}
