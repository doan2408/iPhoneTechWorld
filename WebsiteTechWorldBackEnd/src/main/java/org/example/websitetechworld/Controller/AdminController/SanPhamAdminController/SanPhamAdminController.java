package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamHienThiAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.SanPhamAdminService;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class SanPhamAdminController {

    private final SanPhamAdminService adminService;
    private final SanPhamAdminService sanPhamAdminService;


    @GetMapping
    public ResponseEntity<Page<SanPhamHienThiAdminResponse>> getSanPham(@RequestParam(value = "page",defaultValue = "0") int page) {
        int pageSize = 5;
        return ResponseEntity.ok(adminService.getAllSanPham(page, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanPhamAdminResponse> getSanPhamById(@PathVariable("id") int id) {
        return ResponseEntity.ok(adminService.detailSanPhamAdmin(id));
    }

    @PostMapping
    public ResponseEntity<?> createSanPham( @RequestBody SanPhamAdminRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .toList());
        }
        try {
            SanPhamAdminResponse response = sanPhamAdminService.createSanPhamAdmin(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi server: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanPhamAdminResponse> updateSanPham(@PathVariable Integer id, @RequestBody SanPhamAdminRequest sanPhamAdminRequest) {
        SanPhamAdminResponse sanPham = adminService.updateSanPhamAdmin(id, sanPhamAdminRequest);
        return ResponseEntity.ok(sanPham);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SanPhamAdminResponse> deleteSanPham(@PathVariable Integer id) {
        SanPhamAdminResponse sanPhamAdminResponse = adminService.deleteSanPhamAdmin(id);
        return ResponseEntity.ok(sanPhamAdminResponse);
    }


}
