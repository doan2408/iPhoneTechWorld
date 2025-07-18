package org.example.websitetechworld.Controller.ClientController.DiemController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.ClientResponse.LichSuDiemResponse.LichSuDiemResponse;
import org.example.websitetechworld.Services.ClientServices.DiemServices.LichSuDiemService;
import org.example.websitetechworld.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.time.Instant;
import java.time.OffsetDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/client/lichSuDiem")
public class LichSuDiemController {

    private final LichSuDiemService lichSuDiemService;

    @GetMapping
    public ResponseEntity<Page<LichSuDiemResponse>> getLichSuDiem(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime toDate,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LichSuDiemResponse> result =  lichSuDiemService.getLichSuDiemByKhachHang(fromDate, toDate, pageable);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{idPhieuGiamGia}")
    public ResponseEntity<?> doiVoucher(
            @PathVariable Integer idPhieuGiamGia
    ) {

        try {
            return ResponseEntity.ok(lichSuDiemService.doiDiemNhanVoucher(idPhieuGiamGia));
        }
        catch (ValidationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getErrors());
        }
    }
}
