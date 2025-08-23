package org.example.websitetechworld.Controller.AdminController.BaoHanhAdminController;

import jakarta.validation.Valid;
import org.example.websitetechworld.Dto.Request.AdminRequest.BaoHanhAdminRequest.BaoHanhRequest;
import org.example.websitetechworld.Dto.Request.AdminRequest.BaoHanhAdminRequest.YeuCauBaoHanhAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse.BaoHanhAdminResponse;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiBaoHanh;
import org.example.websitetechworld.Repository.BaoHanhRepository;
import org.example.websitetechworld.Services.AdminServices.BaoHanhAdminServices.BaoHanhAdminServices;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/bao-hanh")
public class BaoHanhAdminController {
    private final BaoHanhAdminServices baoHanhAdminServices;

    public BaoHanhAdminController(BaoHanhAdminServices baoHanhAdminServices) {
        this.baoHanhAdminServices = baoHanhAdminServices;
    }

    @GetMapping
    public ResponseEntity<Page<BaoHanhAdminResponse>> getAll (
            @RequestParam(required = false) String search,
            @RequestParam(required = false) TrangThaiBaoHanh trangThai,
            @RequestParam(required = false) LocalDate ngayBatDau,
            @RequestParam(required = false) LocalDate ngayKetThuc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(baoHanhAdminServices.getAllBaoHanh(search, trangThai, ngayBatDau, ngayKetThuc, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBaoHanh(@PathVariable int id) {
        return ResponseEntity.ok(baoHanhAdminServices.getBaoHanh(id));
    }

    @PostMapping
    public ResponseEntity<?> create (@RequestBody @Valid BaoHanhRequest request, BindingResult bindingResult) {
        System.out.println("id khachHang: " + request.getIdKhachHang());
        System.out.println("id loaibaohanh: " + request.getIdLoaiBaoHanh());
        System.out.println("id imeiDaBan: " + request.getIdImeiDaBan());
        if (bindingResult.hasErrors()) {
            List<Map<String, String>> errors = bindingResult.getFieldErrors()
                    .stream().map(e -> Map.of("field", e.getField(),
                            "message", e.getDefaultMessage()))
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            BaoHanhAdminResponse baoHanhAdminResponse = baoHanhAdminServices.addWarranty(request);
            return ResponseEntity.ok(baoHanhAdminResponse);
        }
        catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrors());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update (@RequestBody @Valid BaoHanhRequest request, @PathVariable int id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<Map<String, String>> errors = bindingResult.getFieldErrors()
                    .stream().map(e -> Map.of("field", e.getField(),
                            "message", e.getDefaultMessage()))
                    .toList();
            return ResponseEntity.badRequest().body(errors);
        }
        try {
            BaoHanhAdminResponse baoHanhAdminResponse = baoHanhAdminServices.updateWarranty(id, request);
            return ResponseEntity.ok(baoHanhAdminResponse);
        }
        catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrors());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/check-bao-hanh/{soImei}")
    public ResponseEntity<?> checkedWarranty (@PathVariable String soImei) {
        return ResponseEntity.ok(baoHanhAdminServices.checkWarranty(soImei));
    }

    @PostMapping("/create-request-warranty")
    public ResponseEntity<?> creaetRequestWarranty(@RequestBody YeuCauBaoHanhAdminRequest request){
        baoHanhAdminServices.createRequestWarranty(request);
        return ResponseEntity.ok("Tao yeu cau bao hanh thanh cong");
    }
}
