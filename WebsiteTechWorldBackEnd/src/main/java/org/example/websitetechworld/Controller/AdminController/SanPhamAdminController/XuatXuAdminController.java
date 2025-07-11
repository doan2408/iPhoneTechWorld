package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.XuatXuAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.XuatXuQuickCreateAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.XuatXuAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.XuatXuAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/listXuatXu")
    public ResponseEntity<List<XuatXuAdminResponse>> getAllListXuatXu() {
        List<XuatXuAdminResponse> list = xuatXuAdminService.getAllXuatXuList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<XuatXuAdminResponse>> searchXuatXu(
            @RequestParam(required = false) String search,
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<XuatXuAdminResponse> xuatXus = xuatXuAdminService.searchXuatXu(search, pageable);
        return ResponseEntity.ok(xuatXus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<XuatXuAdminResponse> detailXuatXu(@PathVariable Integer id) {
        XuatXuAdminResponse detail = xuatXuAdminService.detailXuatXu(id);
        return ResponseEntity.ok(detail);
    }

    @PostMapping
    public ResponseEntity<?> createXuatXu(@Valid @RequestBody XuatXuAdminRequest xuatXuAdminRequest) {
        XuatXuAdminResponse save = xuatXuAdminService.createXuatXu(xuatXuAdminRequest);
        return ResponseEntity.ok(save);
    }

    @PostMapping("/quick-xuatXu")
    public ResponseEntity<?> createQuickXuatXu(@Valid @RequestBody XuatXuQuickCreateAdminRequest xuatXuAdminRequest) {
        XuatXuAdminResponse save = xuatXuAdminService.createQuickXuatXu(xuatXuAdminRequest);
        return ResponseEntity.ok(save);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateXuatXu(
            @PathVariable Integer id,
            @Valid @RequestBody XuatXuAdminRequest xuatXuAdminRequest
    ) {
        XuatXuAdminResponse update = xuatXuAdminService.updateXuatXu(id, xuatXuAdminRequest);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<XuatXuAdminResponse> deleteXuatXu(@PathVariable Integer id) {
        XuatXuAdminResponse delete = xuatXuAdminService.deleteXuatXu(id);
        return ResponseEntity.ok(delete);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<XuatXuAdminResponse> deleteXuatXu2(@PathVariable Integer id) {
//        XuatXuAdminResponse delete = xuatXuAdminService.deleteXuatXu(id);
//        return ResponseEntity.ok(delete);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<XuatXuAdminResponse> deleteXuatXu3(@PathVariable Integer id) {
//        XuatXuAdminResponse delete = xuatXuAdminService.deleteXuatXu(id);
//        return ResponseEntity.ok(delete);
//    }



}