package org.example.websitetechworld.Controller.ClientController.SanPhamClientController;

import lombok.RequiredArgsConstructor;
import org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse.*;
import org.example.websitetechworld.Entity.CameraSau;
import org.example.websitetechworld.Services.ClientServices.SanPhamClientServices.SanPhamClientService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client/home")
public class SanPhamClientController {
    private final SanPhamClientService sanPhamClientService;

    @GetMapping()
    public ResponseEntity<?> getSanPham(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "tenSanPham", required = false) String tenSanPham,
                                        @RequestParam(value = "idLoai", required = false) Integer idLoai,
                                        @RequestParam(value = "giaMin", required = false) BigDecimal giaMin,
                                        @RequestParam(value = "giaMax", required = false) BigDecimal giaMax,
                                        @RequestParam(value = "sort", required = false) String sort
    ) {
        System.out.println("Loai: " + idLoai);
        int pageSize = 16;
        Page<ClientProductResponse> clientProductResponse = sanPhamClientService.getAllSanPhamHome(page, pageSize, tenSanPham, idLoai, giaMin, giaMax, sort);
        return ResponseEntity.ok(clientProductResponse);
    }

    //sau khi click 1 san pham tu trang chu
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getSanPham(@PathVariable int id) {
        ClientProductDetailResponse clientProductDetailResponse = sanPhamClientService.getSanPhamDetail(id);
        return ResponseEntity.ok(clientProductDetailResponse);
    }

    @GetMapping("/{selectedIdKhachHang}/{idsp}/mau/{idMau}/rom/{idRom}")
    public ResponseEntity<ClientProductDetailResponse> getChiTietBienThe(
            @PathVariable("idsp") Integer idsp,
            @PathVariable("idMau") Integer idMau,
            @PathVariable("idRom") Integer idRom,
            @PathVariable("selectedIdKhachHang") Integer selectedIdKhachHang
    ) {
        try {
            ClientProductDetailResponse response = sanPhamClientService.getChiTietBienThe(idsp, idMau, idRom, selectedIdKhachHang);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.out.println("không có sản phẩm tương ứng");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/thongSo/{idSp}/rom/{idRom}")
    public ResponseEntity<ThongSoResponse> getThongSo(@PathVariable("idSp") Integer idSp, @PathVariable("idRom") Integer idRom) {
        try {
            ThongSoResponse response = sanPhamClientService.getThongSo(idSp, idRom);
            return ResponseEntity.ok(response);
        }
        catch (RuntimeException e) {
            System.out.println("Không có thông số");
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/anh/{idSp}/mau/{idMau}")
    public ResponseEntity<List<String>> getListAnhByMau(@PathVariable("idSp") Integer idSp, @PathVariable("idMau") Integer idMau) {
        try {
            List<String> listAnh = sanPhamClientService.getListAnhByMau(idSp, idMau);
            return ResponseEntity.ok(listAnh);
        }
        catch (RuntimeException e) {
            System.out.println("did not find images");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/loai")
    public ResponseEntity<?> getLoai() {
        return ResponseEntity.ok(sanPhamClientService.getLoaiClientResponses());
    }
}
