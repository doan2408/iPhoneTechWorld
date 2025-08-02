package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamChiTietAdminRepuest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamChiTietResponse;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.SanPhamChiTietAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getAllSanPhamChiTiet(@RequestParam(defaultValue = "0", value = "pageNo") int pageNo,@RequestParam(defaultValue = "5",value = "pageSize") int pageSize){
        return ResponseEntity.ok(sanPhamChiTietAdminService.getAllSanPhamChiTiet(pageNo,pageSize));
    }

    @GetMapping("/check-duplicate-variant")
    public ResponseEntity<?> checkDuplicateVariant(
            @RequestParam Integer idSp,
            @RequestParam Integer idMau,
            @RequestParam Integer idRom,
            @RequestParam (required = false) Integer idLoai,
            @RequestParam Integer idNhaCungCap
    ) {
        boolean exists = sanPhamChiTietAdminService.existsVariantInLoai(idSp, idMau, idRom, idLoai, idNhaCungCap);
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
