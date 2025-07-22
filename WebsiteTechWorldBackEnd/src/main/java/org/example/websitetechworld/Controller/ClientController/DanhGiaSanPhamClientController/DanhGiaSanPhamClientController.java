package org.example.websitetechworld.Controller.ClientController.DanhGiaSanPhamClientController;

import jakarta.validation.Valid;
import org.example.websitetechworld.Dto.Request.ClientRequest.DanhGiaSanPhamClientRequest.DanhGiaSanPhamClientRequest;
import org.example.websitetechworld.Dto.Response.ClientResponse.DanhGiaSanPhamClientResponse.DanhGiaSanPhamClientResponse;
import org.example.websitetechworld.Enum.DanhGiaSanPham.TrangThaiDanhGia;
import org.example.websitetechworld.Services.ClientServices.DanhGiaSanPhamClientService.DanhGiaSanPhamClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/client/danh-gia-san-pham")
public class DanhGiaSanPhamClientController {
    @Autowired
    private DanhGiaSanPhamClientService danhGiaService;

    // ===== CRUD Operations =====

    @PostMapping
    public ResponseEntity<DanhGiaSanPhamClientResponse> taoMoiDanhGia(
            @Valid @RequestBody DanhGiaSanPhamClientRequest request) {

        DanhGiaSanPhamClientResponse response = danhGiaService.taoMoiDanhGia(request);
        return ResponseEntity.ok(response);

    }

    @PutMapping("/{id}")
    public ResponseEntity<DanhGiaSanPhamClientResponse> capNhatDanhGia(
            @PathVariable Integer id,
            @Valid @RequestBody DanhGiaSanPhamClientRequest request) {

        DanhGiaSanPhamClientResponse response = danhGiaService.capNhatDanhGia(id, request);
        return ResponseEntity.ok(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> xoaDanhGia(@PathVariable Integer id) {

        danhGiaService.xoaDanhGia(id);
        return ResponseEntity.ok("Xóa đánh giá thành công");

    }

    @GetMapping("/{id}")
    public ResponseEntity<DanhGiaSanPhamClientResponse> layDanhGiaTheoId(@PathVariable Integer id) {

        DanhGiaSanPhamClientResponse response = danhGiaService.layDanhGiaTheoId(id);
        return ResponseEntity.ok(response);

    }

    @GetMapping
    public ResponseEntity<List<DanhGiaSanPhamClientResponse>> layTatCaDanhGia() {

        List<DanhGiaSanPhamClientResponse> responses = danhGiaService.layTatCaDanhGia();
        return ResponseEntity.ok(responses);

    }

    // ===== Search Operations =====

//    @GetMapping("/san-pham/{idSanPhamChiTiet}")
//    public ResponseEntity<List<DanhGiaSanPhamClientResponse>> layDanhGiaTheoSanPham(
//            @PathVariable Integer idSanPhamChiTiet) {
//
//        List<DanhGiaSanPhamClientResponse> responses = danhGiaService.layDanhGiaTheoSanPham(idSanPhamChiTiet);
//        return ResponseEntity.ok(responses);
//
//    }

    @GetMapping("/san-pham/{idSanPhamChiTiet}")
    public ResponseEntity<List<DanhGiaSanPhamClientResponse>> layDanhGiaTheoSanPham(
            @PathVariable Integer idSanPhamChiTiet,
            @RequestParam(required = false) Integer soSao,
            @RequestParam(required = false) Boolean hasMedia
    ) {
        List<DanhGiaSanPhamClientResponse> responses =
                danhGiaService.layDanhGiaTheoSanPham(idSanPhamChiTiet, soSao, hasMedia);
        return ResponseEntity.ok(responses);
    }


    @GetMapping("/khach-hang/{idKhachHang}")
    public ResponseEntity<List<DanhGiaSanPhamClientResponse>> layDanhGiaTheoKhachHang(
            @PathVariable Integer idKhachHang) {

        List<DanhGiaSanPhamClientResponse> responses = danhGiaService.layDanhGiaTheoKhachHang(idKhachHang);
        return ResponseEntity.ok(responses);

    }

    @GetMapping("/trang-thai/{trangThai}")
    public ResponseEntity<List<DanhGiaSanPhamClientResponse>> layDanhGiaTheoTrangThai(
            @PathVariable TrangThaiDanhGia trangThai) {

        List<DanhGiaSanPhamClientResponse> responses = danhGiaService.layDanhGiaTheoTrangThai(trangThai);
        return ResponseEntity.ok(responses);

    }

    @GetMapping("/so-sao/{soSao}")
    public ResponseEntity<List<DanhGiaSanPhamClientResponse>> layDanhGiaTheoSoSao(
            @PathVariable Integer soSao) {

        if (soSao < 1 || soSao > 5) {
            return ResponseEntity.badRequest().build();
        }
        List<DanhGiaSanPhamClientResponse> responses = danhGiaService.layDanhGiaTheoSoSao(soSao);
        return ResponseEntity.ok(responses);

    }

    @GetMapping("/theo-thoi-gian")
    public ResponseEntity<List<DanhGiaSanPhamClientResponse>> layDanhGiaTheoKhoangThoiGian(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime tuNgay,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime denNgay) {

        List<DanhGiaSanPhamClientResponse> responses = danhGiaService.layDanhGiaTheoKhoangThoiGian(tuNgay, denNgay);
        return ResponseEntity.ok(responses);

    }

    // ===== Statistics Operations =====

    @GetMapping("/thong-ke/diem-trung-binh/{idSanPhamChiTiet}")
    public ResponseEntity<Double> layDiemTrungBinhSanPham(@PathVariable Integer idSanPhamChiTiet) {

        Double diemTrungBinh = danhGiaService.tinhDiemTrungBinhSanPham(idSanPhamChiTiet);
        return ResponseEntity.ok(diemTrungBinh);
    }

    @GetMapping("/thong-ke/so-sao/{idSanPhamChiTiet}")
    public ResponseEntity<Map<Integer, Long>> thongKeSoSaoSanPham(@PathVariable Integer idSanPhamChiTiet) {

        Map<Integer, Long> thongKe = danhGiaService.thongKeSoSaoSanPham(idSanPhamChiTiet);
        return ResponseEntity.ok(thongKe);

    }

    @GetMapping("/co-phan-hoi")
    public ResponseEntity<List<DanhGiaSanPhamClientResponse>> layDanhGiaCoPhanhoi() {

        List<DanhGiaSanPhamClientResponse> responses = danhGiaService.layDanhGiaCoPhanhoi();
        return ResponseEntity.ok(responses);

    }

    // ===== Pagination Operation =====

    @GetMapping("/phan-trang")
    public ResponseEntity<Page<DanhGiaSanPhamClientResponse>> layDanhGiaTheoTrang(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ngayDanhGia") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Page<DanhGiaSanPhamClientResponse> responses = danhGiaService.layDanhGiaTheoTrang(page, size, sortBy, sortDir);
        return ResponseEntity.ok(responses);

    }
}
