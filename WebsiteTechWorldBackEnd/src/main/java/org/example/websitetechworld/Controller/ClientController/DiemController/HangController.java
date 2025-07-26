package org.example.websitetechworld.Controller.ClientController.DiemController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.ClientResponse.DiemResponse.HangClientResponse;
import org.example.websitetechworld.Services.ClientServices.DiemServices.HangServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client/hang")
public class HangController {
    private final HangServices hangServices;

    @GetMapping
    public ResponseEntity<?> getHang () {
        try {
            return ResponseEntity.ok(hangServices.tenHang());
        }
        catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/listHang")
    public ResponseEntity<?> getHangList () {
        try {
            List<HangClientResponse> hang = hangServices.getAllHang();
            return ResponseEntity.ok(hang);
        }
        catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/diemXetHang/{idViDiem}")
    public ResponseEntity<?> getDiemXetHang(@PathVariable int idViDiem) {
        try {
            BigDecimal diemXetHang = hangServices.diemXetHang(idViDiem);
            return ResponseEntity.ok(diemXetHang);
        }
        catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }
}
