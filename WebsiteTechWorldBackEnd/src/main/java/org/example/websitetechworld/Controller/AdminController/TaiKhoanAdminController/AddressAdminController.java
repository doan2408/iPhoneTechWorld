package org.example.websitetechworld.Controller.AdminController.TaiKhoanAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.DiaChi;
import org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices.DiaChiAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/address")
public class AddressAdminController {
    private final DiaChiAdminService diaChiAdminService;

    @GetMapping("/{clientId}")
    public ResponseEntity<List<?>> getAddressesByClientId(@PathVariable("clientId") Integer clientId) {
        List<DiaChi> addresses = diaChiAdminService.getAddressesByClientId(clientId);
        return ResponseEntity.ok(addresses);
    }
}
