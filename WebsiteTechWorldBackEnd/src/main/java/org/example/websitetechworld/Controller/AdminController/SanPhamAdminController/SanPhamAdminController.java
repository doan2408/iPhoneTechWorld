package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.SanPhamAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamAdminUpdateResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamHienThiAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamBanHangAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.SanPhamAdminService;
import org.example.websitetechworld.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class SanPhamAdminController {

//    private final SanPhamAdminService adminService;
    private final SanPhamAdminService sanPhamAdminService;


    @GetMapping
    public ResponseEntity<Page<SanPhamHienThiAdminResponse>> getAllSanPham(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<SanPhamHienThiAdminResponse> result = sanPhamAdminService.getAllSanPham(page, size);
        return ResponseEntity.ok(result);
    }

//    @GetMapping
//    public ResponseEntity<Page<SanPhamHienThiAdminResponse>> getSanPham(@RequestParam(value = "page",defaultValue = "0") int page) {
//        int pageSize = 5;
//        return ResponseEntity.ok(adminService.getAllSanPham(page, pageSize));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<SanPhamAdminUpdateResponse> getSanPhamById(@PathVariable("id") int id) {
        return ResponseEntity.ok(sanPhamAdminService.getSanPhamById(id));
    }

    @GetMapping("/viewSanPham/{id}")
    public ResponseEntity<SanPhamAdminResponse> getViewSanPham(@PathVariable("id") int id) {
        return ResponseEntity.ok( sanPhamAdminService.getViewSanPham(id));
    }

    @PostMapping
    public ResponseEntity<?> createSanPham(@Valid @RequestBody SanPhamAdminRequest request) {
            SanPhamAdminResponse response = sanPhamAdminService.createSanPhamAdmin(request);
            return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanPhamAdminResponse> updateSanPham(@PathVariable Integer id, @RequestBody SanPhamAdminRequest sanPhamAdminRequest) {
        SanPhamAdminResponse sanPham = sanPhamAdminService.updateSanPhamAdmin(id, sanPhamAdminRequest);
        return ResponseEntity.ok(sanPham);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SanPhamAdminResponse> deleteSanPham(@PathVariable Integer id) {
        SanPhamAdminResponse sanPhamAdminResponse = sanPhamAdminService.deleteSanPhamAdmin(id);
        return ResponseEntity.ok(sanPhamAdminResponse);
    }

    // ham lay ten san pham
    @GetMapping("/ten-san-pham")
    public ResponseEntity<Page<SanPhamBanHangAdminResponse>> getTenSanPham( @RequestParam(value = "tenSanPham") String tenSanPham ,@RequestParam(value = "pageNo",defaultValue = "5") int pageNo, @RequestParam(value = "pageSize",defaultValue = "0") int pageSize) {
        return ResponseEntity.ok(sanPhamAdminService.getProductNames(tenSanPham,pageNo, pageSize));
    }
    @GetMapping("/category")
    public ResponseEntity<?> loadCategorey (@RequestParam(value = "pageNo", defaultValue = "0") int pageNo,@RequestParam(value = "pageSize", defaultValue = "5") int pageSize){
        return ResponseEntity.ok(sanPhamAdminService.getProductNameCategory(pageNo,pageSize));

    }

}
