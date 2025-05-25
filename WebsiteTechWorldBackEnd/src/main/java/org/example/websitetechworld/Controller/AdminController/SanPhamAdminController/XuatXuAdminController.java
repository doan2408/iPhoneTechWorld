package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.XuatXuAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.XuatXuAdminResponse;
import org.example.websitetechworld.Entity.XuatXu;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.XuatXuAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
    @RequestMapping("/admin/xuatXu")
public class XuatXuAdminController {
    private final XuatXuAdminService xuatXuAdminService;

    @GetMapping
    public ResponseEntity<Page<XuatXuAdminResponse>> getXuatXuResponse(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<XuatXuAdminResponse> xuatXus = xuatXuAdminService.getAllXuatXu(pageable);
        return ResponseEntity.ok(xuatXus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<XuatXuAdminResponse> detailXuatXu(@PathVariable Integer id) {
        XuatXuAdminResponse detail = xuatXuAdminService.detailXuatXu(id);
        return ResponseEntity.ok(detail);
    }

    @PostMapping
    public ResponseEntity<XuatXuAdminResponse> createXuatXu(@RequestBody XuatXuAdminRequest xuatXuAdminRequest) {
        XuatXuAdminResponse save = xuatXuAdminService.createXuatXu(xuatXuAdminRequest);
        return ResponseEntity.ok(save);
    }

    @PutMapping("/{id}")
    public ResponseEntity<XuatXuAdminResponse> updateXuatXu(@PathVariable Integer id, @RequestBody XuatXuAdminRequest xuatXuAdminRequest) {
        XuatXuAdminResponse update = xuatXuAdminService.updateXuatXu(id, xuatXuAdminRequest);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<XuatXuAdminResponse> deleteXuatXu(@PathVariable Integer id) {
        XuatXuAdminResponse delete = xuatXuAdminService.deleteXuatXu(id);
        return ResponseEntity.ok(delete);
    }
}
