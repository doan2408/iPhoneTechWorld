package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest.PinAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.PinAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.PinAdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/pin")
@Validated
public class PinAdminController {
    private final PinAdminService pinAdminService;

    @GetMapping
    public ResponseEntity<Page<PinAdminResponse>> getAllPin(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<PinAdminResponse> pins = pinAdminService.getAllPin(pageable);
        return ResponseEntity.ok(pins);
    }

    @GetMapping("/listPin")
    public ResponseEntity<List<PinAdminResponse>> getAllPinList() {
        List<PinAdminResponse> pins = pinAdminService.getAllPinList();
        return ResponseEntity.ok(pins);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PinAdminResponse>> searchPins(
            @RequestParam(required = false) String search,
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        Page<PinAdminResponse> pins = pinAdminService.searchPins(search, pageable);
        return ResponseEntity.ok(pins);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PinAdminResponse> detailPin(@PathVariable Integer id) {
        PinAdminResponse response = pinAdminService.detailPin(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PinAdminResponse> createPin(@Valid @RequestBody PinAdminRequest pinAdminRequest) {
        PinAdminResponse response = pinAdminService.createPin(pinAdminRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PinAdminResponse> updatePin(@PathVariable Integer id, @Valid @RequestBody PinAdminRequest pinAdminRequest) {
        PinAdminResponse response = pinAdminService.updatePin(id, pinAdminRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PinAdminResponse> deletePin(@PathVariable Integer id) {
        PinAdminResponse response = pinAdminService.deletePin(id);
        return ResponseEntity.ok(response);
    }
}