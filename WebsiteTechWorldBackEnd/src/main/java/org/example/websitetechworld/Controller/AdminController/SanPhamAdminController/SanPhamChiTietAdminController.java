package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamChiTietResponse;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.SanPhamChiTietAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/sanPhamChiTiet")
public class SanPhamChiTietAdminController {

    private final SanPhamChiTietAdminService sanPhamChiTietAdminService;

    @GetMapping("/{id}")
    public ResponseEntity<SanPhamChiTietResponse> getSanPhamChiTiet(@PathVariable("id") Integer id) {
        // Lấy entity từ service (bạn có thể thay đổi logic lấy dữ liệu từ repository nếu cần)
        SanPhamChiTiet entity = sanPhamChiTietAdminService.getChiTietById(id);

        // Kiểm tra nếu không tìm thấy sản phẩm chi tiết
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }

        // Chuyển entity thành DTO và trả về response
        SanPhamChiTietResponse response = sanPhamChiTietAdminService.getSanPhamChiTiet(entity);
        return ResponseEntity.ok(response);
    }
}
