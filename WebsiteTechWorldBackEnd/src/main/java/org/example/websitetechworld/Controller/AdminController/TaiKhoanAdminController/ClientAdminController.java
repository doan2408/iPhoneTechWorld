package org.example.websitetechworld.Controller.AdminController.TaiKhoanAdminController;


import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices.ClientAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/client")
public class ClientAdminController {

    private final ClientAdminService clientAdminService;

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
    public ResponseEntity<?> addClient(@RequestBody KhachHang khachHang) {
        return ResponseEntity.ok(clientAdminService.addClient(khachHang));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCLient(@PathVariable int id, @RequestBody KhachHang khachHangRequest) {
        return ResponseEntity.ok(clientAdminService.updateClient(id, khachHangRequest));
    }
}
