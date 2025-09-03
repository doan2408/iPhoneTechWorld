package org.example.websitetechworld.Controller.AdminController.HoaDonAdminController;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest.ChiTietHoaDonAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest.CthdGiamSoLuong;
import org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest.CthdUpdateSoLuongAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest.SelectKhachHang;
import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.CapNhatTrangThaiThanhToan;
import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.DeleteInvoiceRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.ThanhToanAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.PhieuGiamGiaAdminRequest.PhieuGiamGiaAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.InvoiceRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.*;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.KhachHangGiamGiaResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.PhieuGiamGiaAdminResponse;
import org.example.websitetechworld.Entity.*;
import org.example.websitetechworld.Enum.HoaDon.LoaiHoaDon;
import org.example.websitetechworld.Enum.HoaDon.TrangThaiThanhToan;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDon.HoaDonAdminService;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ChiTietHoaDon.HoaDonChiTietAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.Imei.HoaDonChiTiet_ImeiAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ImeiDaBan.ImeiDaBanAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.LichSuHoaDon.LichSuHoaDonAdminServices;
import org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices.ThanhToanFactory;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/admin/hoa-don")
public class HoaDonAdminController {
    private final HoaDonAdminService hoaDonAdminService;
    private final LichSuHoaDonAdminServices lichSuHoaDonAdminServices;
    private final HoaDonChiTietAdminServices hoaDonChiTietAdminServices;
    private final ThanhToanFactory thanhToanFactory;
    private final HoaDonChiTiet_ImeiAdminServices hoaDonChiTiet_imeiAdminServices;
    private final ImeiDaBanAdminServices imeiDaBanAdminServices;

    private static final int PAGE_SIZE = 4;

    public HoaDonAdminController(HoaDonAdminService hoaDonAdminService, LichSuHoaDonAdminServices lichSuHoaDonAdminServices, HoaDonChiTietAdminServices hoaDonChiTietAdminServices, ThanhToanFactory thanhToanFactory, HoaDonChiTiet_ImeiAdminServices hoaDonChiTietImeiAdminServices, ImeiDaBanAdminServices imeiDaBanAdminServices) {
        this.hoaDonAdminService = hoaDonAdminService;
        this.lichSuHoaDonAdminServices = lichSuHoaDonAdminServices;
        this.hoaDonChiTietAdminServices = hoaDonChiTietAdminServices;
        this.thanhToanFactory = thanhToanFactory;
        hoaDonChiTiet_imeiAdminServices = hoaDonChiTietImeiAdminServices;
        this.imeiDaBanAdminServices = imeiDaBanAdminServices;
    }

    @GetMapping
    public Page<GetAllHoaDonAdminResponse> getAll(@RequestParam(defaultValue = "0", value = "pageNo") int pageNo,
                                                  @RequestParam(defaultValue = "10", value = "pageSize") int pageSize,
                                                  @RequestParam(required = false) String keyWord,
                                                  @RequestParam(required = false) TrangThaiThanhToan trangThai,
                                                  @RequestParam(required = false) LoaiHoaDon loaiHoaDon,
                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayTaoFrom,
                                                  @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayTaoTo) {
        return hoaDonAdminService.getPageHoaDon(pageNo, pageSize,"ngayTaoHoaDon", Sort.Direction.DESC.name(),keyWord,trangThai,loaiHoaDon,ngayTaoFrom,ngayTaoTo);
    }

    @GetMapping("/lich-su")
    public Page<HoaDonAdminResponse> getLichSuBanHang (@RequestParam(defaultValue = "0", value = "pageNo") int pageNo,
                                                  @RequestParam(defaultValue = "10", value = "pageSize") int pageSize,
                                                  @RequestParam(defaultValue = "", value = "search") String search) {
        return hoaDonAdminService.getPageLichSuBanHang(pageNo, pageSize,"ngayTaoHoaDon", Sort.Direction.DESC.name(), search);
    }

    @GetMapping("/{idHoaDon}")
    public HoaDonAdminResponse findById(@PathVariable("idHoaDon") int idHoaDon) {
        return hoaDonAdminService.findById(idHoaDon);
    }

    @GetMapping("/{idHoaDon}/lich-su")
    public List<LichSuHoaDonAdminResponse> getPageLichSu(@PathVariable Integer idHoaDon) {
        return hoaDonAdminService.getPageLichSuHoaDon(idHoaDon);
    }

    @GetMapping("/{idHoaDon}/chi-tiet-thanh-toan")
    public List<ChiTietThanhToanAdminResponse> getPageChiTietThanhToan(@PathVariable Integer idHoaDon, @RequestParam(defaultValue = "0") int pageNo) {
        return hoaDonAdminService.getPageChiTietThanhToan(idHoaDon, pageNo, PAGE_SIZE);
    }

    @PostMapping
    public ResponseEntity<?> createPendingInvoice() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            Integer nhanVienId = customUserDetails.getId();
            try {
                // limit one staff only created 4 pending invoice
                List<Map<String, String>> errors = new ArrayList<>();
                Integer number = lichSuHoaDonAdminServices.countPendingHoaDonByNhanVien(nhanVienId, TrangThaiThanhToan.PENDING
                );
                if (number > 4) {
                    errors.add(Map.of("field", "hoaDon", "message", "Mỗi nhân viên chỉ có thể tạo tối đa 5 hóa đơn chờ"));
                }
                if (!errors.isEmpty()) {
                    throw new ValidationException(errors);
                }

                HoaDon hoaDon = hoaDonAdminService.createPendingInvoice();
                System.out.println("id" + hoaDon.getId());
                System.out.println("maHoaDon" + hoaDon.getMaHoaDon());
                LichSuHoaDon lichSuHoaDon = lichSuHoaDonAdminServices.createLSHDWithPendingInvoice(hoaDon.getId(), nhanVienId);
                return ResponseEntity.ok(Map.of(
                        "message", "Hóa đơn chờ rỗng đã được tạo thành công",
                        "messageLichSu", "Lich su duoc tao thanh cong",
                        "hoaDonId", hoaDon.getId(),
                        "maHoaDon", hoaDon.getMaHoaDon(),
                        "status", hoaDon.getTrangThaiThanhToan(),
                        "lichSuHoaDonId", lichSuHoaDon.getId()
                ));
            } catch (ValidationException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrors());
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Lỗi server khi tạo hóa đơn chờ: " + e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể xác định thông tin nhân viên");
    }

    //api them san pham ( tao hdct )
    @PostMapping("/{idHoaDon}/them-san-pham")
    public ResponseEntity<?> createHoaDonChiTiet(@PathVariable Integer idHoaDon, @RequestBody ChiTietHoaDonAdminRequest request) {
        request.setIdHoaDon(idHoaDon);
        ChiTietHoaDon chiTietHoaDon = hoaDonChiTietAdminServices.createChiTietHoaDon(request);
        hoaDonAdminService.updateTongTien(idHoaDon);
        return ResponseEntity.ok(Map.of(
                "message", "Them hoa don ct thanh cong",
                "idHoaDon", chiTietHoaDon.getIdHoaDon().getId(),
                "idSanPhamChiTiet", chiTietHoaDon.getIdSanPhamChiTiet().getId(),
                "soLuong", chiTietHoaDon.getSoLuong()

        ));
    }

    //api update so luong
    @PutMapping("/{idHoaDon}/so-luong/{idCthd}")
    public ResponseEntity<?> updateSoLuong(@PathVariable Integer idHoaDon, @PathVariable Integer idCthd,
                                           @RequestBody CthdUpdateSoLuongAdminRequest request) {
        Integer soLuong = request.getSoLuong();
        hoaDonChiTietAdminServices.updateSoLuong(idHoaDon, idCthd, request);
        hoaDonAdminService.updateTongTien(idHoaDon);
        return ResponseEntity.ok("Sửa số lượng thành công");
    }

    @DeleteMapping("/hdct/{hdctId}")
    public ResponseEntity<?> deleteChiTietHoaDon(@PathVariable Integer hdctId) {
        try {
            ChiTietHoaDon chiTietHoaDon = hoaDonChiTietAdminServices.findById(hdctId);
            hoaDonChiTietAdminServices.deleleHdct(hdctId);
            hoaDonAdminService.updateTongTien(chiTietHoaDon.getIdHoaDon().getId());
            return ResponseEntity.ok("Xóa thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Xóa thất bại: " + e.getMessage());
        }
    }

    // Xoa hoa don
    @DeleteMapping("/soft-delete/{id}")
    public ResponseEntity<?> hoaDonSoftDelete(@PathVariable Integer id) {
        try {
            hoaDonAdminService.hoaDonSoftDelete(id);
            return ResponseEntity.ok("Xóa thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Xóa thất bại: " + e.getMessage());
        }
    }

    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<?> hoaDonHardDelete(@PathVariable Integer id) {
        try {
            hoaDonAdminService.hoaDonHardDelete(id);
            return ResponseEntity.ok("Xóa thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Xóa thất bại: " + e.getMessage());
        }
    }

    //total
    @GetMapping("/count")
    public ResponseEntity<Long> countHoaDon() {
        Long total = hoaDonAdminService.countHoaDon();
        return ResponseEntity.ok(total);
    }

    //So hoa don cho xy ly
    @GetMapping("/count/pending")
    public ResponseEntity<Integer> countHoaDonPending() {
        Integer count = hoaDonAdminService.countHoaDonPending();
        return ResponseEntity.ok(count);

    }

    //Doanh thu thang
    @GetMapping("/doanh-thu-thang")
    public ResponseEntity<BigDecimal> doangThuThang() {
        BigDecimal doanhThu = hoaDonAdminService.doangThuThang();
        return ResponseEntity.ok(doanhThu);
    }

    //Khach hang
    @GetMapping("/list-khach-hang")
    public ResponseEntity<Page<KhachHangGiamGiaResponse>> getAllKhachHang(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<KhachHangGiamGiaResponse> khachHangList = hoaDonAdminService.getAllKhachHang(search, page, size);
        return ResponseEntity.ok(khachHangList);
    }

    @PostMapping("/add-khach-hang")
    public ResponseEntity<?> addKhachHang(@RequestBody KhachHang khachHang) {
        KhachHang saved = hoaDonAdminService.addKhachHang(khachHang);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{idHoaDon}/khach-hang")
    public ResponseEntity<?> selectKhachHang(@PathVariable Integer idHoaDon, @RequestBody SelectKhachHang selectKhachHang) {
        try {
            hoaDonAdminService.selectKhachHang(idHoaDon, selectKhachHang.getKhachHangId());
            return ResponseEntity.ok("Chọn khach hang thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Chon khach hang thất bại: " + e.getMessage());
        }
    }

    //Phieu giam gia
    @GetMapping("/list-phieu-giam-gia")
    public ResponseEntity<List<PhieuGiamGiaAdminResponse>> getAllPhieuGiamGia (
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "0") BigDecimal giaTriDonHangToiThieu,
            @RequestParam(required = false) Integer idKhachHang) {
        List<PhieuGiamGiaAdminResponse> phieuGiamGias = hoaDonAdminService.layDanhSachPhieuGiamGiaCuaKhach(search, idKhachHang, giaTriDonHangToiThieu);
        return ResponseEntity.ok(phieuGiamGias);
    }

    @PutMapping("/{idHoaDon}/phieu-giam-gia")
    public ResponseEntity<?> phieuGiamGia (@PathVariable Integer idHoaDon, @RequestBody PhieuGiamGiaAdminRequest phieuGiamGiaAdminRequest) {
        hoaDonAdminService.xuLyPhieuGiamGia(idHoaDon, phieuGiamGiaAdminRequest);
        return ResponseEntity.ok("Thêm phiếu giảm giá cho hóa đơn thành công");
    }

    @PutMapping("/{idHoaDon}/thanh-toan")
    public ResponseEntity<?> thanhToan(@PathVariable Integer idHoaDon, @RequestBody ThanhToanAdminRequest thanhToanAdminRequest) {
        try {
            ThanhToanAdminResponse response = hoaDonAdminService.xuLyThanhToan(idHoaDon, thanhToanAdminRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Xem log ở console backend
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server: " + e.getMessage());
        }
    }


    @GetMapping("/all-name-hoa-don")
    public ResponseEntity<?> getAllNameHoaDon() {
        List<TabHoaDonAdminResponse> tabList = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            Integer nhanVienId = customUserDetails.getId();
            tabList = lichSuHoaDonAdminServices.findMaHoaDonPendingByNhanVien(nhanVienId);
        }
        return ResponseEntity.ok(tabList);
    }

    @PatchMapping("/{hoaDonId}/chi-tiet/{hdctId}/remove-imeis")
    public ResponseEntity<?> removeImeisFromChiTietHoaDon(
            @PathVariable Integer hoaDonId,
            @PathVariable Integer hdctId,
            @RequestBody CthdGiamSoLuong request) {
        try {
            hoaDonChiTietAdminServices.removeImeisAndUpdateSoLuong(hoaDonId, hdctId, request);
            return ResponseEntity.ok("IMEI đã được loại bỏ và chi tiết hóa đơn cập nhật thành công.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            System.err.println("Lỗi khi loại bỏ IMEI và cập nhật chi tiết hóa đơn: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi khi cập nhật: " + e.getMessage());
        }
    }

    @PutMapping("/update-invoice/{id}")
    public ResponseEntity<?> updateInvoice(@PathVariable("id") Integer id, @Valid @RequestBody InvoiceRequest request, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(Map.of("errors", errors));
        }

        try {
            hoaDonAdminService.updateInvoice(id, request);
            return ResponseEntity.ok("Invoice updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating invoice: " + e.getMessage());
        }
    }

    @PutMapping("/delete-shipping/{id}")
    public ResponseEntity<?> deleteShipping(@PathVariable("id") Integer id) {
        try {
            hoaDonAdminService.deleteInvocieShipping(id);
            return ResponseEntity.ok("Invoice delete successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error delete invoice: " + e.getMessage());
        }
    }

    @GetMapping("/{hoaDonId}/hdct-by-imei-da-ban")
    public ResponseEntity<Page<ImeiTrangHoaDonResponse>> listSpctByImeiDaBan(@RequestParam(defaultValue = "0") int pageNo,
                                                                             @RequestParam(defaultValue = "10") int pageSize,
                                                                             @RequestParam(defaultValue = "0") int idKhachHang,
                                                                             @PathVariable(value = "hoaDonId") Integer hoaDonId){
            return ResponseEntity.ok(imeiDaBanAdminServices.imeiTrangHoaDonList(pageNo,pageSize,hoaDonId, idKhachHang));
    }

    @GetMapping("/export")
    public void exportInvoiceToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String fileName = "hoa_don_" + currentDate + ".xlsx";
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        hoaDonAdminService.exportExcelToResponse(response);
    }

    @PutMapping("/{idHoaDon}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Integer idHoaDon,
                                          @RequestBody CapNhatTrangThaiThanhToan request) {
        TrangThaiThanhToan trangThai = request.getTrangThaiThanhToan();
        hoaDonAdminService.updateStatus(idHoaDon, trangThai);
        return ResponseEntity.ok("Đã cập nhật trạng thái: " + trangThai.getDisplayName());
    }


    @PutMapping("/sua-gia/{idHoaDonChiTiet}")
    public ResponseEntity<?> updateStatus(@PathVariable Integer idHoaDonChiTiet,
                                          @RequestParam BigDecimal donGia) {
        hoaDonChiTietAdminServices.updateGiaHoaDonChiTiet(idHoaDonChiTiet, donGia);
        return ResponseEntity.ok("Đã cập nhật đơn giá: " + idHoaDonChiTiet);
    }

    @GetMapping("/{idHoaDon}/yeu-cau")
    public List<XuLySauBanHangResponse> getYeuCau (@PathVariable Integer idHoaDon) {
        return hoaDonAdminService.getYeuCau(idHoaDon);
    }

    @PutMapping("/{idHoaDon}/update-shipping-method")
    public void updateShippingMethod (@PathVariable Integer idHoaDon) {
        try {
            hoaDonAdminService.updateShippingMethod(idHoaDon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping("/{idHoaDon}/phieu-giam-gia")
    public ResponseEntity<?> deletePhieuGiamGia (@PathVariable Integer idHoaDon) {
        hoaDonAdminService.deletePhieuGiamGia(idHoaDon);
        return ResponseEntity.ok("Thêm phiếu giảm giá cho hóa đơn thành công");
    }
}
