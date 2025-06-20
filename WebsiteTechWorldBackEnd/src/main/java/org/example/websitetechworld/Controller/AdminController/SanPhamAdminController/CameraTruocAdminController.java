package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CameraTruocAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.CpuAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CameraTruocAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CpuAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ImeiAdminResponse;
import org.example.websitetechworld.Entity.CameraTruoc;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.CameraTruocAdminService;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cameraTruoc")
public class CameraTruocAdminController {
    private final CameraTruocAdminService cameraTruocAdminService;

    @GetMapping
    public ResponseEntity<Page<CameraTruocAdminResponse>> getAllCameraTruoc(
            @PageableDefault(page = 0, size = 5) Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        Page<CameraTruocAdminResponse> cameraTruocs = cameraTruocAdminService.getAllCameraTruoc(pageable, keyword);
        return ResponseEntity.ok(cameraTruocs);
    }

    @GetMapping("/listCameraTruoc")
    public ResponseEntity<List<CameraTruocAdminResponse>> getAllCameraTruocList() {
        List<CameraTruocAdminResponse> cameraTruocs = cameraTruocAdminService.getAllCameraTruocList();
        return ResponseEntity.ok(cameraTruocs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CameraTruocAdminResponse> detailCameraTruoc(@PathVariable Integer id) {
        CameraTruocAdminResponse response = cameraTruocAdminService.detailCameraTruoc(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createCameraTruoc(@RequestBody @Valid CameraTruocAdminRequest cameraTruocAdminRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<Map<String, String>> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> Map.of("field", e.getField(),
                            "message", e.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            CameraTruocAdminResponse response = cameraTruocAdminService.createCameraTruoc(cameraTruocAdminRequest);

            return ResponseEntity.ok(response);
        }
        catch (ValidationException e) {
            // Bắt riêng FieldException trả lỗi với field cụ thể
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrors());
        }
        catch (IllegalArgumentException e) {
            // Trả về lỗi với field = "other" để frontend biết là lỗi chung
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    List.of(Map.of("field", "other", "message", e.getMessage()))
            );
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    List.of(Map.of("field", "other", "message", "Lỗi hệ thống: " + e.getMessage()))
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCameraTruoc(@PathVariable Integer id, @RequestBody @Valid CameraTruocAdminRequest cameraTruocAdminRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<Map<String, String>> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> Map.of("field", e.getField(),
                            "message", e.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            CameraTruocAdminResponse response = cameraTruocAdminService.updateCameraTruoc(id, cameraTruocAdminRequest);

            return ResponseEntity.ok(response);
        }
        catch (ValidationException e) {
            // Bắt riêng FieldException trả lỗi với field cụ thể
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrors());
        }
        catch (IllegalArgumentException e) {
            // Trả về lỗi với field = "other" để frontend biết là lỗi chung
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    List.of(Map.of("field", "other", "message", e.getMessage()))
            );
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    List.of(Map.of("field", "other", "message", "Lỗi hệ thống: " + e.getMessage()))
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CameraTruocAdminResponse> deleteCameraTruoc(@PathVariable Integer id) {
        CameraTruocAdminResponse response = cameraTruocAdminService.deleteCameraTruoc(id);

        return ResponseEntity.ok(response);
    }
}
