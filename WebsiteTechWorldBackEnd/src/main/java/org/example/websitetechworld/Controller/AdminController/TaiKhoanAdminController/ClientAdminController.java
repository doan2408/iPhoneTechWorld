package org.example.websitetechworld.Controller.AdminController.TaiKhoanAdminController;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest.AdminClientRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest.AdminDiaChiRequest;
import org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices.ClientAdminService;
import org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices.DiaChiAdminService;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/client")
public class ClientAdminController {

    private final ClientAdminService clientAdminService;
    private final DiaChiAdminService diaChiAdminService;

    @GetMapping
    public ResponseEntity<?> getAllClients(@RequestParam(value = "page",defaultValue = "0") int page) {
        int pageSize = 10;
        return ResponseEntity.ok(clientAdminService.getAllClient(page, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable int id) {
        return clientAdminService.getClientById(id)
                .map(ResponseEntity :: ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> addClient(@RequestBody @Valid AdminClientRequest khachHang, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<Map<String, String>> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> Map.of("field", e.getField(),
                            "message", e.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok(clientAdminService.addClient(khachHang));
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
    public ResponseEntity<?> updateCLient(@PathVariable int id, @RequestBody  @Valid AdminClientRequest khachHangRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<Map<String, String>> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(e -> Map.of("field", e.getField(),
                            "message", e.getDefaultMessage()))
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            return ResponseEntity.ok(clientAdminService.updateClient(id, khachHangRequest));
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

    //get dia chi from idKhachHang
    @GetMapping("/addresses/{idKhachHang}")
    public ResponseEntity<?> getAllClientAddresses(@PathVariable int idKhachHang) {
        return ResponseEntity.ok(diaChiAdminService.getAllDiaChi(idKhachHang));
    }

    //get DiaChi from idDiaChi
    @GetMapping("/address/{idDiaChi}")
    public ResponseEntity<?> getAllClientAddress(@PathVariable int idDiaChi) {
        return ResponseEntity.ok(diaChiAdminService.getDiaChiById(idDiaChi));
    }

    //update diaChi of client man Admin
    @PutMapping("/address/{idDiaChi}")
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

}
