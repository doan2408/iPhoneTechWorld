package org.example.websitetechworld.Controller.ClientController.SanPhamClientController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse.ClientProductDetailResponse;
import org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse.ClientProductResponse;
import org.example.websitetechworld.Services.ClientServices.SanPhamClientServices.SanPhamClientService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
public class SanPhamClientController {
    private final SanPhamClientService sanPhamClientService;

    @GetMapping("/home")
    public ResponseEntity<?> getSanPham(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "tenSanPham", required = false) String tenSanPham,
                                        @RequestParam(value = "loai", required = false) String loai,
                                        @RequestParam(value = "giaMin", required = false) BigDecimal giaMin,
                                        @RequestParam(value = "giaMax", required = false) BigDecimal giaMax
    ) {
        int pageSize = 60;
        Page<ClientProductResponse> clientProductResponse = sanPhamClientService.getAllSanPhamHome(page, pageSize, tenSanPham, loai, giaMin, giaMax);
        return ResponseEntity.ok(clientProductResponse);
    }

    //sau khi click 1 san pham tu trang chu
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getSanPham(@PathVariable int id) {
        ClientProductDetailResponse clientProductDetailResponse = sanPhamClientService.getSanPhamDetail(id);
        return ResponseEntity.ok(clientProductDetailResponse);
    }

    @GetMapping("/{idsp}/mau/{idMau}/rom/{idRom}")
    public ResponseEntity<ClientProductDetailResponse> getChiTietBienThe(
            @PathVariable("idsp") Integer idsp,
            @PathVariable("idMau") Integer idMau,
            @PathVariable("idRom") Integer idRom
    ) {
        try {
            ClientProductDetailResponse response = sanPhamClientService.getChiTietBienThe(idsp, idMau, idRom);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("không có sản phẩm tương ứng");
        }
        return ResponseEntity.notFound().build();
    }
}
