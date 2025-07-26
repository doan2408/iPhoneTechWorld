package org.example.websitetechworld.Controller.AdminController.DanhGiaSanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.PhanHoiDanhGiaAdminRequest.PhanHoiDanhGiaAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.DanhGiaSanPhamAdminResponse.DanhGiaSanPhamAdminResponse;
import org.example.websitetechworld.Services.AdminServices.DanhGiaSanPhamAdminService.DanhGiaSanPhamAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/danh-gia-san-pham")
public class DanhGiaSanPhamAdminController {
    private final DanhGiaSanPhamAdminService danhGiaSanPhamAdminService;

    @GetMapping
    public ResponseEntity<Page<DanhGiaSanPhamAdminResponse>> getAllDanhGiaAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DanhGiaSanPhamAdminResponse> result = danhGiaSanPhamAdminService.layTatCaDanhGiaAdmin(pageable);
        return ResponseEntity.ok(result);
    }

    // Phê duyệt đánh giá
    @PutMapping("/{id}/phe-duyet")
    public ResponseEntity<String> pheDuyetDanhGia(@PathVariable Integer id) {
        try {
            danhGiaSanPhamAdminService.pheDuyetDanhGia(id);
            return ResponseEntity.ok("Phê duyệt thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Từ chối đánh giá
    @PutMapping("/{id}/tu-choi")
    public ResponseEntity<String> tuChoiDanhGia(@PathVariable Integer id) {
        try {
            danhGiaSanPhamAdminService.tuChoiDanhGia(id);
            return ResponseEntity.ok("Từ chối thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Xóa đánh giá
    @DeleteMapping("/{id}")
    public ResponseEntity<String> xoaDanhGia(@PathVariable Integer id) {
        try {
            danhGiaSanPhamAdminService.xoaDanhGia(id);
            return ResponseEntity.ok("Xóa thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Phản hồi đánh giá
    @PostMapping("/{id}/phan-hoi")
    public ResponseEntity<String> phanHoiDanhGia(@PathVariable Integer id, @RequestBody PhanHoiDanhGiaAdminRequest request) {
        try {
            danhGiaSanPhamAdminService.phanHoiDanhGia(id, Map.of("noiDungPhanHoi", request.getNoiDungPhanHoi()));
            return ResponseEntity.ok("Phản hồi thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
