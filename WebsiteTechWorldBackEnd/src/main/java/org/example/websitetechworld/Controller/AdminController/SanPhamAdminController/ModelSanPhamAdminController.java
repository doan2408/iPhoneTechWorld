package org.example.websitetechworld.Controller.AdminController.SanPhamAdminController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.ModelSanPhamAdminResponse;
import org.example.websitetechworld.Services.AdminServices.SanPhamAdminServices.ModelSanPhamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/modelSanPham")
public class ModelSanPhamAdminController {
    private final ModelSanPhamService modelSanPhamService;


    @GetMapping("/listModelSanPham")
    public ResponseEntity<List<ModelSanPhamAdminResponse>> getAlllistResponse() {
        List<ModelSanPhamAdminResponse> list = modelSanPhamService.getAllListModelSanPhamAdmin();
        return ResponseEntity.ok(list);
    }
}
