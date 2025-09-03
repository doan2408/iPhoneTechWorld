package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamChiTietAdminRepuest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamChiTietResponse;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.SanPhamChiTietAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/sanPhamChiTiet")
public class SanPhamChiTietAdminController {

    private final SanPhamChiTietAdminService sanPhamChiTietAdminService;

    @GetMapping("/{id}")
    public ResponseEntity<SanPhamChiTietResponse> getSanPhamChiTiet(@PathVariable("id") Integer id) {
        SanPhamChiTiet entity = sanPhamChiTietAdminService.getChiTietById(id);

        if (entity == null) {
            return ResponseEntity.notFound().build();
        }

        SanPhamChiTietResponse response = sanPhamChiTietAdminService.getSanPhamChiTiet(entity);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SanPhamChiTietResponse> createSPCT(@RequestBody SanPhamChiTietAdminRepuest sanPhamChiTietAdminRepuest) {
        SanPhamChiTietResponse response = sanPhamChiTietAdminService.createSanPhamChiTiet(sanPhamChiTietAdminRepuest);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanPhamChiTietResponse> updateSPCT(@PathVariable Integer id, @RequestBody SanPhamChiTietAdminRepuest sanPhamChiTietAdminRepuest) {
        SanPhamChiTietResponse response = sanPhamChiTietAdminService.updateSanPhamChiTiet(id, sanPhamChiTietAdminRepuest);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllSanPhamChiTiet(
            @RequestParam(defaultValue = "0", value = "pageNo") int pageNo,
            @RequestParam(defaultValue = "5",value = "pageSize") int pageSize,
            @RequestParam(defaultValue = "0", value = "selectedIdKhachHang") int selectedIdKhachHang){
        return ResponseEntity.ok(sanPhamChiTietAdminService.getAllSanPhamChiTiet(pageNo, pageSize, selectedIdKhachHang));
    }

    @GetMapping("/check-duplicate-variant")
    public ResponseEntity<?> checkDuplicateVariant(
            @RequestParam (required = false) Integer idSp,
            @RequestParam Integer idMau,
            @RequestParam Integer idRom,
            @RequestParam (required = false) Integer idLoai,
            @RequestParam (required = false) Integer idNCC
    ) {
        boolean exists = sanPhamChiTietAdminService.existsVariantInLoai(idSp, idMau, idRom, idLoai, idNCC);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

//    @GetMapping("/check-duplicate-variant-gia")
//    public ResponseEntity<?> checkDuplicateVariantGia(
//            @RequestParam (required = false) Integer idSp,
//            @RequestParam Integer idMau,
//            @RequestParam Integer idRom,
//            @RequestParam (required = false) Integer idLoai
//    ) {
//        boolean exists = sanPhamChiTietAdminService.existsVariantGia(idSp, idMau, idRom, idLoai);
//        return ResponseEntity.ok(Map.of("exists", exists));
//    }

    @GetMapping("/check-duplicate-variant-gia")
    public ResponseEntity<?> checkDuplicateVariantGia(
            @RequestParam(required = false) Integer idSp,
            @RequestParam Integer idMau,
            @RequestParam Integer idRom,
            @RequestParam(required = false) Integer idLoai) {
        try {
            // Kiểm tra tham số đầu vào
            if (idSp == null || idMau == null || idRom == null || idLoai == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Thiếu tham số bắt buộc: idSp, idMau, idRom, idLoai không được null");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Gọi service để kiểm tra biến thể
            Map<String, Object> result = sanPhamChiTietAdminService.existsVariantGia(idSp, idMau, idRom, idLoai);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Lỗi khi kiểm tra biến thể: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
