package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDon;

import jakarta.persistence.EntityManager;
import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.ThanhToanAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.ThongTinNguoiNhanAdminRequest;
import org.example.websitetechworld.Dto.Request.InvoiceRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.*;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.KhachHangGiamGiaResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.HoaDon.LoaiHoaDon;
import org.example.websitetechworld.Enum.HoaDon.TrangThaiThanhToan;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Enum.KhachHang.TrangThaiKhachHang;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.Imei.HoaDonChiTiet_ImeiAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.SanPham.HoaDonChiTiet_SanPhamAdminServices;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.ImeiAdminService;
import org.example.websitetechworld.Services.AdminServices.ThanhToanAdminServices.ThanhToanFactory;
import org.example.websitetechworld.Services.AdminServices.ThanhToanAdminServices.ThanhToanStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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
    private final EntityManager entityManager;

    public HoaDonAdminService(HoaDonRepository hoaDonRepository, LichSuHoaDonRepository lichSuHoaDonRepository, ChiTietThanhToanRepository chiTietThanhToanRepository, ChiTietHoaDonRepository chiTietHoaDonRepository, KhachHangRepository khachHangRepository, ThanhToanFactory thanhToanFactory, ImeiAdminService imeiAdminService, HoaDonChiTiet_ImeiAdminServices hoaDonChiTiet_ImeiAdminServices, HoaDonChiTiet_SanPhamAdminServices hoaDonChiTietSanPhamAdminServices, EntityManager entityManager) {
        this.hoaDonRepository = hoaDonRepository;
        this.lichSuHoaDonRepository = lichSuHoaDonRepository;
        this.chiTietThanhToanRepository = chiTietThanhToanRepository;
        this.chiTietHoaDonRepository = chiTietHoaDonRepository;
        this.khachHangRepository = khachHangRepository;
        this.thanhToanFactory = thanhToanFactory;
        this.imeiAdminService = imeiAdminService;
        this.hoaDonChiTiet_ImeiAdminServices = hoaDonChiTiet_ImeiAdminServices;
        hoaDonChiTiet_sanPhamAdminServices = hoaDonChiTietSanPhamAdminServices;
        this.entityManager = entityManager;
    }

    public List<HoaDonAdminResponse> getAllHoaDon(){
        return hoaDonRepository.findAll().stream().map(HoaDonAdminResponse::convertDto).collect(Collectors.toList());
    }

    public Page<GetAllHoaDonAdminResponse> getPageHoaDon(Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return hoaDonRepository.findByIsDeleteFalseOrIsDeleteIsNull(pageable).map(GetAllHoaDonAdminResponse::convertDto);
    }

    public HoaDonAdminResponse findById(Integer id){
        return hoaDonRepository.findById(id).map(HoaDonAdminResponse::convertDto).orElseThrow(() -> new RuntimeException("Khong tim thay hoa don"));
    }
    public HoaDon findByIdHoaDon(Integer id){
        return hoaDonRepository.findById(id).orElseThrow();
    }

    public Page<LichSuHoaDonAdminResponse> getPageLichSuHoaDon(Integer hoaDonId,Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return lichSuHoaDonRepository.findByIdHoaDon_Id(hoaDonId,pageable).map(LichSuHoaDonAdminResponse::convertDto);
    }

    public List<ChiTietThanhToanAdminResponse> getPageChiTietThanhToan(Integer hoaDonId,Integer pageNo, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        return chiTietThanhToanRepository.findByIdHoaDon_Id(hoaDonId,pageable).stream().map(ChiTietThanhToanAdminResponse::convertDto).toList();
    }

    @Transactional
    public HoaDon createPendingInvoice(){

        HoaDon hoaDon = new HoaDon();
        hoaDon.setNgayTaoHoaDon(LocalDate.now());
        hoaDon.setLoaiHoaDon(LoaiHoaDon.POS);
        hoaDon.setPhiShip(BigDecimal.ZERO);
        hoaDon.setSoTienGiam(BigDecimal.ZERO);
        hoaDon.setTrangThaiThanhToan(TrangThaiThanhToan.PENDING);
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
        hoaDon.setTenNguoiMua(khachHang.getTenKhachHang());
        hoaDon.setSdtNguoiMua(khachHang.getSdt());
        hoaDonRepository.save(hoaDon);
    }

    public ThanhToanAdminResponse xuLyThanhToan(Integer idHoaDon, ThanhToanAdminRequest request){
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new IllegalArgumentException("Hóa đơn không tồn tại: " + idHoaDon));

        if (hoaDon.getTrangThaiThanhToan() == TrangThaiThanhToan.PAID) {
            throw new IllegalArgumentException("Hóa đơn đã được thanh toán.");
        }
        String hinhThucThanhToan = request.getHinhThucThanhToan().name();
        ThanhToanStrategy thanhToanStrategy = thanhToanFactory.getStrategy(hinhThucThanhToan);
        ThanhToanAdminResponse response = thanhToanStrategy.thanhToan(hoaDon,request);

        if (response.getMessage().equals("Thanh toán thành công")) {
            hoaDonChiTiet_ImeiAdminServices.updateImeiStautusFromHoaDon(hoaDon.getChiTietHoaDons().stream().toList(), TrangThaiImei.SOLD);
            hoaDonChiTiet_sanPhamAdminServices.updateSoLuongProdcut(hoaDon.getChiTietHoaDons().stream().toList());
        }

        return response;
    }

    public void hoaDonSoftDelete (Integer id){
        HoaDon hoaDon = hoaDonRepository.findById(id).orElseThrow();
        hoaDon.setIsDelete(true);
        hoaDonRepository.save(hoaDon);
    }

    public void hoaDonHardDelete (Integer id){
        HoaDon hoaDon = hoaDonRepository.findById(id).orElseThrow();
        hoaDonRepository.delete(hoaDon);
    }

    public Integer countHoaDonPending () {
        return hoaDonRepository.countByTrangThaiThanhToan(TrangThaiThanhToan.PENDING);
    }

    public BigDecimal doangThuThang () {
        return hoaDonRepository.doanhThuThang();
    }

    public Page<KhachHangGiamGiaResponse> getAllKhachHang (String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KhachHang> khachHangPage = (search == null || search.isEmpty()) ?
                khachHangRepository.findTrangThai_Active(pageable) :
                khachHangRepository.findByTenKhachHangContainingIgnoreCaseAndTrangThai_Active(search, pageable);
        return khachHangPage.map(kh -> new KhachHangGiamGiaResponse(kh.getId(),kh.getMaKhachHang(), kh.getTenKhachHang()));
    }

    public KhachHang addKhachHang  (KhachHang khachHang) {
        KhachHang saved = new KhachHang();
        saved.setTenKhachHang(khachHang.getTenKhachHang());
        saved.setSdt(khachHang.getSdt());
        saved.setEmail(khachHang.getEmail());
        saved.setTrangThai(TrangThaiKhachHang.ACTIVE);
        return khachHangRepository.save(saved);
    }

    public void updateInvoice(Integer id, InvoiceRequest request) {
        HoaDon invoice = hoaDonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoice.setTenNguoiNhan(request.getTenNguoiNhan());
        invoice.setSdtNguoiNhan(request.getSdtNguoiNhan());
        invoice.setDiaChiGiaoHang(request.getDiaChiGiaoHang());
        invoice.setPhiShip(request.getPhiShip());
        invoice.setIsShipping(request.isShipping());
        invoice.setMaVanDon(request.getMaVanDon());

        // Cập nhật thanh_tien nếu có phiShip
        if (request.getPhiShip() != null) {
            BigDecimal tongTien = invoice.getTongTien() != null ? invoice.getTongTien() : BigDecimal.ZERO;
            BigDecimal soTienGiam = invoice.getSoTienGiam() != null ? invoice.getSoTienGiam() : BigDecimal.ZERO;
            BigDecimal thanhTien = tongTien.add(request.getPhiShip()).subtract(soTienGiam);
            invoice.setThanhTien(thanhTien);
        }

        hoaDonRepository.updateInvoice(
                id,
                invoice.getTenNguoiNhan(),
                invoice.getSdtNguoiNhan(),
                invoice.getDiaChiGiaoHang(),
                invoice.getPhiShip(),
                invoice.getIsShipping(),
                invoice.getMaVanDon(),
                invoice.getThanhTien()
        );
    }
}
