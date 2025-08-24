package org.example.websitetechworld.Controller.ClientController.GioHangClientController;

import org.example.websitetechworld.Dto.Request.ClientRequest.GioHangClientRequest.GioHangClientRequest;
import org.example.websitetechworld.Dto.Response.ClientResponse.GioHangClientResponse.GioHangClientResponse;
import org.example.websitetechworld.Services.ClientServices.GioHangClientService.GioHangClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/gio-hang")
public class GioHangClientController {
    private final GioHangClientService gioHangClientService;

    public GioHangClientController(GioHangClientService gioHangClientService) {
        this.gioHangClientService = gioHangClientService;
    }

    @GetMapping ("/{idKhachHang}")
    public ResponseEntity<GioHangClientResponse> getCart(@PathVariable Integer idKhachHang) {
        return ResponseEntity.ok(gioHangClientService.layGioHang(idKhachHang));
    }

    @PostMapping
    public ResponseEntity<GioHangClientResponse> addToCart(@RequestBody GioHangClientRequest request) {
        return ResponseEntity.ok(gioHangClientService.themSanPhamVaoGio(request));
    }

    @PutMapping ("/{idGioHangChiTiet}")
    public ResponseEntity<GioHangClientResponse> updateQuantity(@PathVariable Integer idGioHangChiTiet,
                                                          @RequestParam Integer soLuong) {
        return ResponseEntity.ok(gioHangClientService.suaSoLuongSanPham(idGioHangChiTiet, soLuong));
    }

    @DeleteMapping ("/remove/{idGioHangChiTiet}")
    public ResponseEntity<Void> removeItem(@PathVariable Integer idGioHangChiTiet) {
        gioHangClientService.xoaSanPhamKhoiGio(idGioHangChiTiet);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping ("/clear/{idKhachHang}")
    public ResponseEntity<Void> clearCart(@PathVariable Integer idKhachHang) {
        gioHangClientService.xoaGioHang(idKhachHang);
        return ResponseEntity.ok().build();
    }

    @GetMapping ("/{idKhachHang}/count")
    public ResponseEntity<?> cartCount (@PathVariable Integer idKhachHang) {
        return ResponseEntity.ok(gioHangClientService.layGioHang(idKhachHang).getItems().size());
    }
}