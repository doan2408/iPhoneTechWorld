package org.example.websitetechworld.Controller.ClientController.PhieuGiamGiaClientController;

import org.example.websitetechworld.Dto.Request.AdminRequest.PhieuGiamGiaAdminRequest.PhieuGiamGiaAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.PhieuGiamGiaAdminResponse;
import org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices.PhieuGiamGiaAdminService;
import org.example.websitetechworld.Services.ClientServices.PhieuGiamGiaClientService.PhieuGiamGiaClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/hoa-don/phieu-giam-gia")
public class PhieuGiamGiaClientController {

    private final PhieuGiamGiaClientService phieuGiamGiaClientService;

    public PhieuGiamGiaClientController(PhieuGiamGiaClientService phieuGiamGiaClientService) {
        this.phieuGiamGiaClientService = phieuGiamGiaClientService;
    }

    @GetMapping("/list-phieu-giam-gia")
    public ResponseEntity<List<PhieuGiamGiaAdminResponse>> getAllPhieuGiamGia (
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "0") BigDecimal giaTriDonHangToiThieu,
            @RequestParam(required = false) Integer idKhachHang) {
        List<PhieuGiamGiaAdminResponse> phieuGiamGias = phieuGiamGiaClientService.layDanhSachPhieuGiamGiaCuaKhach(search, idKhachHang, giaTriDonHangToiThieu);
        return ResponseEntity.ok(phieuGiamGias);
    }

    @PutMapping("/{idHoaDon}/phieu-giam-gia")
    public ResponseEntity<?> phieuGiamGia (@PathVariable Integer idHoaDon, @RequestBody PhieuGiamGiaAdminRequest phieuGiamGiaAdminRequest) {
        phieuGiamGiaClientService.xuLyPhieuGiamGia(idHoaDon, phieuGiamGiaAdminRequest);
        return ResponseEntity.ok("Thêm phiếu giảm giá cho hóa đơn thành công");
    }
}
