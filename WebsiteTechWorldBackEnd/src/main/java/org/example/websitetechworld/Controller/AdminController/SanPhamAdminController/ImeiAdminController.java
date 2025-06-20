package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.ImeiAdminRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.LoaiAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.CpuAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ImeiAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.LoaiAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.ImeiAdminService;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/imei")
public class ImeiAdminController {
    private final ImeiAdminService imeiAdminService;

    @GetMapping
    public ResponseEntity<Page<ImeiAdminResponse>> getAllImei(
            @PageableDefault(page = 0, size = 5) Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword
    ) {
        Page<ImeiAdminResponse> imeis = imeiAdminService.getAllImei(pageable, keyword);
        return ResponseEntity.ok(imeis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImeiAdminResponse> detailImei(@PathVariable Integer id) {
        ImeiAdminResponse response = imeiAdminService.detailImei(id);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createImei(@RequestBody @Valid ImeiAdminRequest imeiAdminRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<Map<String, String>> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> Map.of("field", e.getField(),
                            "message", e.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            ImeiAdminResponse response = imeiAdminService.createImei(imeiAdminRequest);

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
    public ResponseEntity<?> updateImei(@PathVariable Integer id, @RequestBody @Valid ImeiAdminRequest imeiAdminRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<Map<String, String>> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> Map.of("field", e.getField(),
                            "message", e.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            ImeiAdminResponse response = imeiAdminService.updateImei(id, imeiAdminRequest);
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
    public ResponseEntity<ImeiAdminResponse> deleteImei(@PathVariable Integer id) {
        ImeiAdminResponse response = imeiAdminService.deleteImei(id);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/check-imeis")
    public ResponseEntity<Map<String, List<String>>> checkImeiDuplicates(@RequestBody Map<String, List<String>> request) {
        List<String> imeis = request.get("imeis");
        List<String> duplicates = imeiAdminService.checkImeiDuplicates(imeis);
        Map<String, List<String>> response = new HashMap<>();
        response.put("duplicates", duplicates);
        return ResponseEntity.ok(response);
    }


}
