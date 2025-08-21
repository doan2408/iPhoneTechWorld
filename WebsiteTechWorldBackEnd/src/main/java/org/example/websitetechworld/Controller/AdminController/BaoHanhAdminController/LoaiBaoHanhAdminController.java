package org.example.websitetechworld.Controller.AdminController.BaoHanhAdminController;

import jakarta.validation.Valid;
import org.example.websitetechworld.Dto.Request.AdminRequest.BaoHanhAdminRequest.LoaiBaoHanhRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.LoaiBaoHanhAdminResponse;
import org.example.websitetechworld.Services.AdminServices.BaoHanhAdminServices.LoaiBaoHanhAdminServices;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/loai-bao-hanh")
public class LoaiBaoHanhAdminController {
    private final LoaiBaoHanhAdminServices loaiBaoHanhAdminServices;

    public LoaiBaoHanhAdminController(LoaiBaoHanhAdminServices loaiBaoHanhAdminServices) {
        this.loaiBaoHanhAdminServices = loaiBaoHanhAdminServices;
    }

    @GetMapping("/list")
    public ResponseEntity<List<LoaiBaoHanhAdminResponse>> loaiBaoHanhList() {
        return ResponseEntity.ok(loaiBaoHanhAdminServices.loaiBaoHanhList());
    }

    @GetMapping
    public ResponseEntity<Page<LoaiBaoHanhAdminResponse>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(loaiBaoHanhAdminServices.getAllLoaiBaoHanh(search, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoaiBaoHanhAdminResponse> geLoaiBaoHanh(@PathVariable int id) {
        return ResponseEntity.ok(loaiBaoHanhAdminServices.getLoaiBaoHanh(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid LoaiBaoHanhRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<Map<String, String>> errors = bindingResult.getFieldErrors()
                    .stream().map(e -> Map.of("field", e.getField(),
                            "message", e.getDefaultMessage()))
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            LoaiBaoHanhAdminResponse baoHanhAdminResponse = loaiBaoHanhAdminServices.addWarrantyType(request);
            return ResponseEntity.ok(baoHanhAdminResponse);
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrors());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestBody @Valid LoaiBaoHanhRequest request, @PathVariable int id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<Map<String, String>> errors = bindingResult.getFieldErrors()
                    .stream().map(e -> Map.of("field", e.getField(),
                            "message", e.getDefaultMessage()))
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            LoaiBaoHanhAdminResponse loaiBaoHanhAdminResponse = loaiBaoHanhAdminServices.updateLoaiBaoHanh(id, request);
            return ResponseEntity.ok(loaiBaoHanhAdminResponse);
        }
        catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrors());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
