package org.example.websitetechworld.Controller.AdminController.TaiKhoanAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest.AdminStaffRequest;
import org.example.websitetechworld.Entity.NhanVien;
import org.example.websitetechworld.Services.AdminServices.TaiKhoanAdminServices.NhanVienAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/staff")
public class NhanVienAdminController {
    private final NhanVienAdminService nhanvienAdminService;
    private final NhanVienAdminService nhanVienAdminService;

    @GetMapping()
    public ResponseEntity<?> getStaff(@RequestParam(value = "page",defaultValue = "0") int page) {
        int pageSize = 10;
        return ResponseEntity.ok(nhanvienAdminService.getNhanVienList(page, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStaffDetail(@PathVariable int id) {
        return nhanvienAdminService.getStaffById(id).map(ResponseEntity :: ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStaff(@PathVariable("id") Integer id, @RequestBody AdminStaffRequest adminStaffRequest) {
        AdminStaffRequest nhanVienUpdate = nhanvienAdminService.updateStaff(id, adminStaffRequest);
        return ResponseEntity.ok(nhanVienUpdate);
    }


    @PostMapping()
    public ResponseEntity<?> addStaff(@RequestBody AdminStaffRequest adminStaffRequest) {
        AdminStaffRequest nhanVienAdd = nhanVienAdminService.createStaff(adminStaffRequest);
        return ResponseEntity.ok(nhanVienAdd);
    }
    
    //không nên xóa tk nhân viên
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStaff(@PathVariable Integer id) {
        nhanVienAdminService.deleteStaff(id);
        return ResponseEntity.ok().build();
    }
}
