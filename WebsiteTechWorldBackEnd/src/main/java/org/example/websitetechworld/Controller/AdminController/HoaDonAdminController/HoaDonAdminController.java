package org.example.websitetechworld.Controller.AdminController.HoaDonAdminController;

import org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest.ChiTietHoaDonAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest.CthdGiamSoLuong;
import org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest.CthdUpdateSoLuongAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest.SelectKhachHang;
import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.ThanhToanAdminRequest;
import org.example.websitetechworld.Dto.Request.InvoiceRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.*;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.KhachHangGiamGiaResponse;
import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Entity.LichSuHoaDon;
import org.example.websitetechworld.Enum.GiaoHang.ShippingMethod;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDon.HoaDonAdminService;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.ChiTietHoaDon.HoaDonChiTietAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.Imei.HoaDonChiTiet_ImeiAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.LichSuHoaDon.LichSuHoaDonAdminServices;
import org.example.websitetechworld.Services.AdminServices.ThanhToanAdminServices.ThanhToanFactory;
import org.example.websitetechworld.Services.AdminServices.ThanhToanAdminServices.ThanhToanStrategy;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/admin/hoa-don")
public class HoaDonAdminController {
    private final HoaDonAdminService hoaDonAdminService;
    private final LichSuHoaDonAdminServices lichSuHoaDonAdminServices;
    private final HoaDonChiTietAdminServices hoaDonChiTietAdminServices;
    private final ThanhToanFactory thanhToanFactory;
    private final HoaDonChiTiet_ImeiAdminServices hoaDonChiTiet_imeiAdminServices;

    private static final int PAGE_SIZE = 4;

    public HoaDonAdminController(HoaDonAdminService hoaDonAdminService, LichSuHoaDonAdminServices lichSuHoaDonAdminServices, HoaDonChiTietAdminServices hoaDonChiTietAdminServices, ThanhToanFactory thanhToanFactory, HoaDonChiTiet_ImeiAdminServices hoaDonChiTietImeiAdminServices) {
        this.hoaDonAdminService = hoaDonAdminService;
        this.lichSuHoaDonAdminServices = lichSuHoaDonAdminServices;
        this.hoaDonChiTietAdminServices = hoaDonChiTietAdminServices;
        this.thanhToanFactory = thanhToanFactory;
        hoaDonChiTiet_imeiAdminServices = hoaDonChiTietImeiAdminServices;
    }

    @GetMapping
    public Page<GetAllHoaDonAdminResponse> getAll(@RequestParam(defaultValue = "0",value = "pageNo") int pageNo,
                                                  @RequestParam(defaultValue = "10", value = "pageSize") int pageSize){
        return hoaDonAdminService.getPageHoaDon(pageNo,pageSize);
    }
    @GetMapping("/{idHoaDon}")
    public HoaDonAdminResponse findById(@PathVariable("idHoaDon") int idHoaDon){
        return hoaDonAdminService.findById(idHoaDon);
    }

    @GetMapping("/{idHoaDon}/lich-su")
    public Page<LichSuHoaDonAdminResponse> getPageLichSu(@PathVariable Integer idHoaDon, @RequestParam(defaultValue = "0") int pageNo){
        return hoaDonAdminService.getPageLichSuHoaDon(idHoaDon,pageNo,PAGE_SIZE);
    }
    @GetMapping("/{idHoaDon}/chi-tiet-thanh-toan")
    public List<ChiTietThanhToanAdminResponse> getPageChiTietThanhToan(@PathVariable Integer idHoaDon, @RequestParam(defaultValue = "0") int pageNo){
        return hoaDonAdminService.getPageChiTietThanhToan(idHoaDon,pageNo,PAGE_SIZE);
    }
//    @GetMapping("/{idHoaDon}/xem-giao-hang")
//    public List<GiaoHangAdminResponse> getPageGiaoHang(@PathVariable Integer idHoaDon, @RequestParam(defaultValue = "0") int pageNo){
//        return hoaDonAdminService.getPageGiaoHang(idHoaDon,pageNo,PAGE_SIZE);
//    }

    @PostMapping
    public ResponseEntity<?> createPendingInvoice(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails){
            Integer nhanVienId = customUserDetails.getId();
            try {
                HoaDon hoaDon = hoaDonAdminService.createPendingInvoice();
                LichSuHoaDon lichSuHoaDon = lichSuHoaDonAdminServices.createLSHDWithPendingInvoice(hoaDon.getId(),nhanVienId);
                return ResponseEntity.ok(Map.of(
                        "message", "Hóa đơn chờ rỗng đã được tạo thành công",
                        "messageLichSu", "Lich su duoc tao thanh cong",
                        "hoaDonId", hoaDon.getId(),
                        "status", hoaDon.getTrangThaiThanhToan(),
                        "lichSuHoaDonId", lichSuHoaDon.getId()
                ));
            }catch (Exception e){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi server khi tạo hóa đơn chờ: "+e.getMessage());
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không thể xác định thông tin nhân viên");
    }

    //api them san pham ( tao hdct )
    @PostMapping("/{idHoaDon}/them-san-pham")
    public ResponseEntity<?> createHoaDonChiTiet(@PathVariable Integer idHoaDon,@RequestBody ChiTietHoaDonAdminRequest request){
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
                                           @RequestBody CthdUpdateSoLuongAdminRequest request){
        Integer soLuong = request.getSoLuong();
        hoaDonChiTietAdminServices.updateSoLuong(idHoaDon,idCthd,request);
        hoaDonAdminService.updateTongTien(idHoaDon);
        return ResponseEntity.ok("Sửa số lượng thành công");
    }

    @DeleteMapping("/hdct/{hdctId}")
    public ResponseEntity<?> deleteChiTietHoaDon(@PathVariable Integer hdctId){
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
    public ResponseEntity<?> hoaDonSoftDelete (@PathVariable Integer id){
        try {
            hoaDonAdminService.hoaDonSoftDelete(id);
            return ResponseEntity.ok("Xóa thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Xóa thất bại: " + e.getMessage());
        }
    }

    @DeleteMapping("/hard-delete/{id}")
    public ResponseEntity<?> hoaDonHardDelete (@PathVariable Integer id){
        try {
            hoaDonAdminService.hoaDonHardDelete(id);
            return ResponseEntity.ok("Xóa thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Xóa thất bại: " + e.getMessage());
        }
    }

    //So hoa don cho xy ly
    @GetMapping("/count/pending")
    public ResponseEntity<Integer> countHoaDonPending () {
        Integer count = hoaDonAdminService.countHoaDonPending();
        return ResponseEntity.ok(count);

    }

    //Doanh thu thang
    @GetMapping("/doanh-thu-thang")
    public ResponseEntity<BigDecimal> doangThuThang () {
        BigDecimal doanhThu = hoaDonAdminService.doangThuThang();
        return ResponseEntity.ok(doanhThu);
    }

    //Khach hang
    @GetMapping ("/list-khach-hang")
    public ResponseEntity<Page<KhachHangGiamGiaResponse>> getAllKhachHang(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<KhachHangGiamGiaResponse> khachHangList = hoaDonAdminService.getAllKhachHang(search, page, size);
        return ResponseEntity.ok(khachHangList);
    }

    @PostMapping ("/add-khach-hang")
    public ResponseEntity<?> addKhachHang (@RequestBody KhachHang khachHang) {
        KhachHang saved = hoaDonAdminService.addKhachHang(khachHang);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{idHoaDon}/khach-hang")
    public ResponseEntity<?> selectKhachHang(@PathVariable Integer idHoaDon, @RequestBody SelectKhachHang selectKhachHang){
        try {
            hoaDonAdminService.selectKhachHang(idHoaDon,selectKhachHang.getKhachHangId());
            return ResponseEntity.ok("Chọn khach hang thành công");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Chon khach hang thất bại: " + e.getMessage());
        }
    }


    @PutMapping("/{idHoaDon}/thanh-toan")
    public ResponseEntity<?> thanhToan(@PathVariable Integer idHoaDon, @RequestBody ThanhToanAdminRequest thanhToanAdminRequest){
        ThanhToanAdminResponse response = hoaDonAdminService.xuLyThanhToan(idHoaDon, thanhToanAdminRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all-name-hoa-don")
    public ResponseEntity<?> getAllNameHoaDon(){
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

    @PutMapping("/update-invoice")
    public ResponseEntity<String> updateInvoice(@RequestBody InvoiceRequest request) {
        ShippingMethod method = ShippingMethod.fromCode(request.getShippingMethod());
        int distanceInKm = calculateDistanceFromAddress(request.getDiaChiGiaoHang()); // Hàm tính khoảng cách
        int shippingFee = method.calculateShippingFee(distanceInKm);

        // Lưu vào cơ sở dữ liệu hoặc trả về
        return ResponseEntity.ok("Invoice updated with shipping fee: " + shippingFee);
    }

    private int calculateDistanceFromAddress(String address) {
        // Logic tính khoảng cách (gọi API hoặc sử dụng logic hiện tại)
        return 5; // Ví dụ trả về 5km
    }



}
