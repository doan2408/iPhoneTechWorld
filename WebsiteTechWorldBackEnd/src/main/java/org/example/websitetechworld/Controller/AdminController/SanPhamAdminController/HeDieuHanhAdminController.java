package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HDHQuickCreateAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HeDieuHanhAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.HinhAnhAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.HeDieuHanhAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.HinhAnhAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ManHinhAdminResponse;
import org.example.websitetechworld.Entity.HeDieuHanh;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.HeDieuHanhAdminService;
import org.example.websitetechworld.exception.ValidationException;
import org.springdoc.core.service.GenericResponseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/heDieuHanh")
public class HeDieuHanhAdminController {

    private final HeDieuHanhAdminService heDieuHanhAdminService;
    private final GenericResponseService responseBuilder;

    //phân trang, search, hiển thị
    @GetMapping
    public ResponseEntity<Page<HeDieuHanhAdminResponse>> getAllHeDieuHanh(
            @PageableDefault(page = 0, size = 5) Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        Page<HeDieuHanhAdminResponse> heDieuHanhs = heDieuHanhAdminService.getAllHeDieuHanh(pageable, keyword);

        return ResponseEntity.ok(heDieuHanhs);
    }

    @GetMapping("/listHDH")
    public ResponseEntity<List<HeDieuHanhAdminResponse>> getAllHeDieuHanhList() {
        List<HeDieuHanhAdminResponse> heDieuHanhs = heDieuHanhAdminService.getAllHeDieuHanhList();
        return ResponseEntity.ok(heDieuHanhs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HeDieuHanhAdminResponse> detailHeDieuHanh(@PathVariable Integer id) {
        HeDieuHanhAdminResponse response = heDieuHanhAdminService.detailHeDieuHanh(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createHeDieuHanh(@RequestBody @Valid HeDieuHanhAdminRequest heDieuHanhAdminRequest) {
            HeDieuHanhAdminResponse response = heDieuHanhAdminService.createHeDieuHanh(heDieuHanhAdminRequest);
            return ResponseEntity.ok(response);
    }

    @PostMapping("/quick-hdh")
    public ResponseEntity<?> createHDHQuick (@RequestBody @Valid HDHQuickCreateAdminRequest request) {
        HeDieuHanhAdminResponse response = heDieuHanhAdminService.createHDHQuick(request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateHeDieuHanh(@PathVariable Integer id, @RequestBody @Valid HeDieuHanhAdminRequest heDieuHanhAdminRequest) {
            HeDieuHanhAdminResponse response = heDieuHanhAdminService.updateHeDieuHanh(id, heDieuHanhAdminRequest);
            return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HeDieuHanhAdminResponse> deleteHeDieuHanh(@PathVariable Integer id) {
        HeDieuHanhAdminResponse response = heDieuHanhAdminService.deleteHeDieuHanh(id);

        return ResponseEntity.ok(response);
    }
}
