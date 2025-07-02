package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ModelSanPhamAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ModelSanPhamAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ModelSanPhamHienThiAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.ModelSanPhamService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/modelSanPham")
public class ModelSanPhamAdminController {
    private final ModelSanPhamService modelSanPhamService;


    @GetMapping("/listModelSanPham")
    public ResponseEntity<List<ModelSanPhamAdminResponse>> getAlllistResponse() {
        List<ModelSanPhamAdminResponse> list = modelSanPhamService.getAllListModelSanPhamAdmin();
        return ResponseEntity.ok(list);
    }

    @GetMapping
    public ResponseEntity<Page<ModelSanPhamHienThiAdminResponse>> getAllPage(
           @RequestParam(defaultValue = "0") int page,
           @RequestParam(defaultValue = "5") int size
            ) {
        Page<ModelSanPhamHienThiAdminResponse> responses = modelSanPhamService.getAllPageModelSanPhamAdmin(page,size);

        return ResponseEntity.ok(responses);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ModelSanPhamAdminResponse> createModelSanPham(@RequestBody ModelSanPhamAdminRequest request) {
        ModelSanPhamAdminResponse response = modelSanPhamService.createModelSanPham(request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")

    @PutMapping("/{id}")
    public ResponseEntity<ModelSanPhamAdminResponse> updateModelSanPham(@PathVariable Integer id,@RequestBody ModelSanPhamAdminRequest request) {
        ModelSanPhamAdminResponse response = modelSanPhamService.updateModelSanPham(id, request);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModelSanPham(@PathVariable Integer id) {
        modelSanPhamService.deleteModelSanPham(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModelSanPhamAdminResponse> findByIdModelSanPham(@PathVariable Integer id) {
        ModelSanPhamAdminResponse response =  modelSanPhamService.findByIdModelSanPham(id);
        return ResponseEntity.ok(response);
    }
}
