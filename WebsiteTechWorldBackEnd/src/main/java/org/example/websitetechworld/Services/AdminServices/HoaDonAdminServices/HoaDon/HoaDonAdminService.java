package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDon;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.ThanhToanAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.PhieuGiamGiaAdminRequest.PhieuGiamGiaAdminRequest;
import org.example.websitetechworld.Dto.Request.InvoiceRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.*;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.KhachHangGiamGiaResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.GiaoHang.ShippingMethod;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;
import org.example.websitetechworld.Enum.HoaDon.LoaiHoaDon;
import org.example.websitetechworld.Enum.HoaDon.TrangThaiThanhToan;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Enum.KhachHang.TrangThaiKhachHang;
import org.example.websitetechworld.Repository.*;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.Imei.HoaDonChiTiet_ImeiAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.SanPham.HoaDonChiTiet_SanPhamAdminServices;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.ImeiAdminService;
import org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices.ThanhToanFactory;
import org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices.ThanhToanStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
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

    public HoaDonAdminService(HoaDonRepository hoaDonRepository, LichSuHoaDonRepository lichSuHoaDonRepository, ChiTietThanhToanRepository chiTietThanhToanRepository, ChiTietHoaDonRepository chiTietHoaDonRepository, KhachHangRepository khachHangRepository, ThanhToanFactory thanhToanFactory, ImeiAdminService imeiAdminService, HoaDonChiTiet_ImeiAdminServices hoaDonChiTiet_ImeiAdminServices, HoaDonChiTiet_SanPhamAdminServices hoaDonChiTietSanPhamAdminServices, PhieuGiamGiaRepository phieuGiamGiaRepository, EntityManager entityManager) {
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
    }

    public List<HoaDonAdminResponse> getAllHoaDon(){
        return hoaDonRepository.findAll().stream().map(HoaDonAdminResponse::convertDto).collect(Collectors.toList());
    }

    public Page<GetAllHoaDonAdminResponse> getPageHoaDon(Integer pageNo, Integer pageSize, String sortBy, String direction) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(dir, sortBy));
        return hoaDonRepository
                .findByIsDeleteFalseOrIsDeleteIsNull(pageable)
                .map(GetAllHoaDonAdminResponse::convertDto);
    }
    public List<HoaDonAdminResponse> exportExcel(){
        List<HoaDon> hoaDonList = hoaDonRepository.findByIsDeleteFalseOrIsDeleteIsNull();
        return hoaDonList.stream().map(HoaDonAdminResponse::convertDto).toList();
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
                khachHangRepository.findByTenKhachHangContainingIgnoreCaseOrMaKhachHangContainingIgnoreCaseOrSdtContainingIgnoreCaseAndTrangThai_Active(search, pageable);
        return khachHangPage.map(kh -> new KhachHangGiamGiaResponse(kh.getId(),kh.getMaKhachHang(), kh.getSdt(), kh.getTenKhachHang()));
    }

    public KhachHang addKhachHang  (KhachHang khachHang) {
        KhachHang saved = new KhachHang();
        saved.setTenKhachHang(khachHang.getTenKhachHang());
        saved.setSdt(khachHang.getSdt());
        saved.setEmail(khachHang.getEmail());
        saved.setTrangThai(TrangThaiKhachHang.ACTIVE);
        return khachHangRepository.save(saved);
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
        // Gán maVanDon
        String maVanDon = request.getMaVanDon();
        if (maVanDon == null || maVanDon.trim().isEmpty()) {
            maVanDon = generateMaVanDon(id);
        }
        invoice.setMaVanDon(maVanDon);

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
                    invoice.getNgayDatHang()
            );
        } catch (Exception e) {
            System.err.println("Error executing JPQL query for invoice id " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to execute JPQL query: " + e.getMessage());
        }
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
        List<HoaDonAdminResponse> hoaDonAdminResponses = exportExcel(); // giả sử đây là phương thức lấy dữ liệu

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



}
