package org.example.websitetechworld.Controller.AdminController.TaiKhoanAdminController;

import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.CheckValidation.CreateGroups;
import org.example.websitetechworld.CheckValidation.UpdateGroups;
import org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest.AdminStaffRequest;
import org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices.NhanVienAdminService;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/staff")
public class NhanVienAdminController {
    private final NhanVienAdminService nhanVienAdminService;

    @GetMapping()
    public ResponseEntity<?> getStaff(@RequestParam(value = "page",defaultValue = "0") int page,
                                      @RequestParam(value = "keyword", required = false) String keyword) {
        int pageSize = 10;
        return ResponseEntity.ok(nhanVienAdminService.getNhanVienList(page, pageSize, keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStaffDetail(@PathVariable int id) {
        return nhanVienAdminService.getStaffById(id).map(ResponseEntity :: ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStaff(@PathVariable("id") Integer id, @RequestBody @Validated({Default.class,UpdateGroups.class}) AdminStaffRequest adminStaffRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<Map<String, String>> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> Map.of("field", e.getField(),
                            "message", e.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            AdminStaffRequest nhanVienUpdate = nhanVienAdminService.updateStaff(id, adminStaffRequest);
            return ResponseEntity.ok(nhanVienUpdate);
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    List.of(Map.of("field", "other", "message", "Lỗi hệ thống: " + e.getMessage()))
            );
        }
    }


    @PostMapping()
    public ResponseEntity<?> addStaff(@RequestBody @Validated({Default.class,CreateGroups.class}) AdminStaffRequest adminStaffRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<Map<String, String>> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> Map.of("field", e.getField(),
                            "message", e.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            AdminStaffRequest nhanVienAdd = nhanVienAdminService.createStaff(adminStaffRequest);
            return ResponseEntity.ok(nhanVienAdd);
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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    List.of(Map.of("field", "other", "message", "Lỗi hệ thống: " + e.getMessage()))
            );
        }
    }

    //không nên xóa tk nhân viên
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable Integer id) {
        nhanVienAdminService.deleteStaff(id);
        return ResponseEntity.ok().build();
    }
}
