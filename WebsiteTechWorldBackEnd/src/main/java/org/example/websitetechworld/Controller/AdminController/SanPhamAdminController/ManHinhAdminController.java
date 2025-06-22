package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ManHinhAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.MauSacAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ManHinhAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.MauSacAdminResponse;
import org.example.websitetechworld.Entity.ManHinh;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.ManHinhAdminService;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/manHinh")
public class ManHinhAdminController {
    private final ManHinhAdminService manHinhAdminService;

    //phân trang, search, hiển thị
    @GetMapping
    public ResponseEntity<Page<ManHinhAdminResponse>> getAllManHinh(
            @PageableDefault(page = 0, size = 5) Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        Page<ManHinhAdminResponse> manHinhs = manHinhAdminService.getAllManHinh(pageable, keyword);
        return ResponseEntity.ok(manHinhs);
    }

    @GetMapping("/listManHinh")
    public ResponseEntity<List<ManHinhAdminResponse>> getAllManHinhList() {
        List<ManHinhAdminResponse> manHinhs = manHinhAdminService.getAllManHinhList();
        return ResponseEntity.ok(manHinhs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManHinhAdminResponse> detailManHinh(@PathVariable Integer id) {
        ManHinhAdminResponse response = manHinhAdminService.detailManHinh(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createManHinh(@RequestBody @Valid ManHinhAdminRequest manHinhAdminRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<Map<String, String>> fieldErrors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> Map.of("field", e.getField(),
                            "message", e.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(fieldErrors);
        }
        try {
            ManHinhAdminResponse response = manHinhAdminService.createManHinh(manHinhAdminRequest);
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
    public ResponseEntity<?> updateManHinh(@PathVariable Integer id, @RequestBody @Valid ManHinhAdminRequest manHinhAdminRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<Map<String, String>> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> Map.of("field", e.getField(),
                            "message", e.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            ManHinhAdminResponse response = manHinhAdminService.updateManHinh(id, manHinhAdminRequest);
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
    public ResponseEntity<ManHinhAdminResponse> deleteManHinh(@PathVariable Integer id) {
        ManHinhAdminResponse response = manHinhAdminService.deleteManHinh(id);

        return ResponseEntity.ok(response);
    }
}
