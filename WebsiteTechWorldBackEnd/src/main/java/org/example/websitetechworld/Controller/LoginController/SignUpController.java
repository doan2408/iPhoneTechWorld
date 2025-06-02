package org.example.websitetechworld.Controller.LoginController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.ClientRequest.TaiKhoanClientRequest.ClientRequest;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Services.ClientServices.TaiKhoanClientServices.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class SignUpController {
    private final ClientService clientService;

    //dang ki tai khoan (Client)
    @PostMapping("/register")
    public ResponseEntity<?> signUp(@RequestBody KhachHang khachHangRequest) {
        return ResponseEntity.ok(clientService.addClient(khachHangRequest));
    }

}
