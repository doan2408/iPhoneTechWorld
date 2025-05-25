package org.example.websitetechworld.Controller.ClientController.TaiKhoanClientController;


import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.ClientResponse.TaiKhoanClientReponse;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Services.ClientServices.TaiKhoanClientServices.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;

    @GetMapping("/{id}")
    public ResponseEntity<?> hienThiClient(@PathVariable int id) {
        TaiKhoanClientReponse khachHangResponse = clientService.hienThi(id).orElse(null);
        return ResponseEntity.ok(khachHangResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody KhachHang khachHangRequest) {
        KhachHang clientUpdate = clientService.updateClient(id, khachHangRequest);
        return ResponseEntity.ok(clientUpdate);
    }
}
