package org.example.websitetechworld.Controller.AdminController.TaiKhoanAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices.NhanVienAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/staff")
public class NhanVienAdminController {
    private final NhanVienAdminService nhanvienAdminService;

    @GetMapping()
    public ResponseEntity<?> getStaff(@RequestParam(value = "page",defaultValue = "0") int page) {
        int pageSize = 10;
        return ResponseEntity.ok(nhanvienAdminService.getNhanVienList(page, pageSize));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getStaffDetail(@PathVariable int id) {
        return  nhanvienAdminService.getStaffById(id).map(ResponseEntity :: ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
