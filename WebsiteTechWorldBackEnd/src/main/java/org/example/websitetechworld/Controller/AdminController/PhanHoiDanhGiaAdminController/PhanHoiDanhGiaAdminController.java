package org.example.websitetechworld.Controller.AdminController.PhanHoiDanhGiaAdminController;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.PhanHoiDanhGiaAdminRequest.PhanHoiDanhGiaAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.PhanHoiDanhGiaAdminResponse.PhanHoiDanhGiaAdminResponse;
import org.example.websitetechworld.Entity.PhanHoiDanhGia;
import org.example.websitetechworld.Services.AdminServices.PhanHoiAdminService.PhanHoiDanhGiaAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/phan-hoi-danh-gia")
public class PhanHoiDanhGiaAdminController {
    private final PhanHoiDanhGiaAdminService service;

    @PostMapping("/danh-gia/{idDanhGia}/phan-hoi")
    public ResponseEntity<PhanHoiDanhGiaAdminResponse> createPhanHoi(@PathVariable Integer idDanhGia, @RequestBody PhanHoiDanhGiaAdminRequest dto) {
        PhanHoiDanhGiaAdminResponse phanHoiDanhGiaAdminResponse = service.create( idDanhGia, dto);
        return ResponseEntity.ok(phanHoiDanhGiaAdminResponse);
    }

    @PutMapping("/{id}")
    public PhanHoiDanhGia updatePhanHoi(@PathVariable Integer id, @Valid @RequestBody PhanHoiDanhGiaAdminRequest dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/danh-gia/{idDanhGia}")
    public ResponseEntity<List<PhanHoiDanhGiaAdminResponse>> getPhanHoiByDanhGia(@PathVariable Integer idDanhGia) {
        return ResponseEntity.ok(service.getPhanHoiByDanhGia(idDanhGia));
    }

}
