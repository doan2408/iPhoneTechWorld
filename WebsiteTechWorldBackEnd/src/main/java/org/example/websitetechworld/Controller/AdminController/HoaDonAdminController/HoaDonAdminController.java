package org.example.websitetechworld.Controller.AdminController.HoaDonAdminController;

import org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest.ChiTietHoaDonAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.ChiTietHoaDonAdminRequest.CthdUpdateSoLuongAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.*;
import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.LichSuHoaDon;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDonAdminService;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.HoaDonChiTietAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.LichSuHoaDonAdminServices;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/hoa-don")
public class HoaDonAdminController {
    private final HoaDonAdminService hoaDonAdminService;
    private final LichSuHoaDonAdminServices lichSuHoaDonAdminServices;
    private final HoaDonChiTietAdminServices hoaDonChiTietAdminServices;

    private static final int PAGE_SIZE = 4;

    public HoaDonAdminController(HoaDonAdminService hoaDonAdminService, LichSuHoaDonAdminServices lichSuHoaDonAdminServices, HoaDonChiTietAdminServices hoaDonChiTietAdminServices) {
        this.hoaDonAdminService = hoaDonAdminService;
        this.lichSuHoaDonAdminServices = lichSuHoaDonAdminServices;
        this.hoaDonChiTietAdminServices = hoaDonChiTietAdminServices;
    }

    @GetMapping
    public List<GetAllHoaDonAdminResponse> getAll(@RequestParam(defaultValue = "0",value = "pageNo") int pageNo){
        return hoaDonAdminService.getPageHoaDon(pageNo,PAGE_SIZE);
    }
    @GetMapping("/{idHoaDon}")
    public HoaDonAdminResponse findById(@PathVariable("idHoaDon") int idHoaDon){
        return hoaDonAdminService.findById(idHoaDon);
    }

    @GetMapping("/{idHoaDon}/lich-su")
    public List<LichSuHoaDonAdminResponse> getPageLichSu(@PathVariable Integer idHoaDon, @RequestParam(defaultValue = "0") int pageNo){
        return hoaDonAdminService.getPageLichSuHoaDon(idHoaDon,pageNo,PAGE_SIZE);
    }
    @GetMapping("/{idHoaDon}/chi-tiet-thanh-toan")
    public List<ChiTietThanhToanAdminResponse> getPageChiTietThanhToan(@PathVariable Integer idHoaDon, @RequestParam(defaultValue = "0") int pageNo){
        return hoaDonAdminService.getPageChiTietThanhToan(idHoaDon,pageNo,PAGE_SIZE);
    }
    @GetMapping("/{idHoaDon}/xem-giao-hang")
    public List<GiaoHangAdminResponse> getPageGiaoHang(@PathVariable Integer idHoaDon, @RequestParam(defaultValue = "0") int pageNo){
        return hoaDonAdminService.getPageGiaoHang(idHoaDon,pageNo,PAGE_SIZE);
    }

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


}
