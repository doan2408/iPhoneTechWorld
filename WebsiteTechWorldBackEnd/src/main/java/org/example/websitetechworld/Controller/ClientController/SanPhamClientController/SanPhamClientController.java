package org.example.websitetechworld.Controller.ClientController.SanPhamClientController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Services.ClientServices.SanPhamClientServices.SanPhamClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class SanPhamClientController {

    private final SanPhamClientService clientService;

    @GetMapping("/home")
    public ResponseEntity<?> getSanPham() {
        return ResponseEntity.ok(clientService.getAllSanPham());
    }
}
