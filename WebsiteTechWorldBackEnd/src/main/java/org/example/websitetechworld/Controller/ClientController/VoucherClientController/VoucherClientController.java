package org.example.websitetechworld.Controller.ClientController.VoucherClientController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhieuGiamGiaAdminResponse.PhieuGiamGiaAdminResponse;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;
import org.example.websitetechworld.Services.AdminServices.PhieuGiamGiaAdminServices.PhieuGiamGiaAdminService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/client/voucher")
@RequiredArgsConstructor
public class VoucherClientController {
    private final PhieuGiamGiaAdminService phieuGiamGiaAdminService;

    // get voucher when client redeeming
    @GetMapping
    public ResponseEntity<Page<PhieuGiamGiaAdminResponse>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) TrangThaiPGG trangThai,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayBatDau,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayKetThuc,
            @RequestParam(required = false) HangKhachHang hangToiThieu,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Page<PhieuGiamGiaAdminResponse> result = phieuGiamGiaAdminService.getVoucherRedeeming(
                search, trangThai, ngayBatDau, ngayKetThuc, hangToiThieu, page, size, sortBy, direction);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaAdminResponse> getPhieuGiamGia(@PathVariable int id) {
        PhieuGiamGiaAdminResponse response = phieuGiamGiaAdminService.layPhieuGiamGiaTheoId(id);
        return ResponseEntity.ok(response);
    }


}
