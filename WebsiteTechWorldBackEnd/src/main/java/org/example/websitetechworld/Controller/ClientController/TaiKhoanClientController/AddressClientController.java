package org.example.websitetechworld.Controller.ClientController.TaiKhoanClientController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest.AdminDiaChiRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminDiaChiResponse;
import org.example.websitetechworld.Entity.DiaChi;
import org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices.DiaChiAdminService;
import org.example.websitetechworld.Services.LoginServices.CustomUserDetails;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client/address")
public class AddressClientController {
    private final DiaChiAdminService diaChiAdminService;

    //get DiaChi from idDiaChi
    @GetMapping("/{idDiaChi}")
    public ResponseEntity<?> getAllClientAddress(@PathVariable int idDiaChi) {
        return ResponseEntity.ok(diaChiAdminService.getDiaChiById(idDiaChi));
    }

    //update diaChi of client man Admin
    @PutMapping("/{idDiaChi}")
    public ResponseEntity<?> updateDiaChi(@PathVariable int idDiaChi, @RequestBody @Valid AdminDiaChiRequest adminDiaChiRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<Map<String, String>> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> Map.of("field", e.getField(),
                            "message", e.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok(diaChiAdminService.updateDiaChi(idDiaChi, adminDiaChiRequest));
        }catch (ValidationException e) {
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

    @PostMapping
    public ResponseEntity<?> addDiaChi(@RequestBody @Valid AdminDiaChiRequest adminDiaChiRequest, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            List<Map<String, String>> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> Map.of("field", e.getField(),
                            "message", e.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok(diaChiAdminService.addDiaChi(adminDiaChiRequest));
        }catch (ValidationException e) {
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

    //get all dia chi from idKhachHang
    @GetMapping("/addresses/{idKhachHang}")
    public ResponseEntity<?> getAllClientAddresses(@PathVariable int idKhachHang) {
        return ResponseEntity.ok(diaChiAdminService.getAllDiaChi(idKhachHang));
    }
    @GetMapping("/addresses_by_kh")
    public ResponseEntity<?> getAllClientAddresses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<AdminDiaChiResponse> listDiaChi = null;
        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            Integer userLoginId = customUserDetails.getId();
            listDiaChi = diaChiAdminService.getAllDiaChi(userLoginId);
        }
        return ResponseEntity.ok(listDiaChi);

    }
}
