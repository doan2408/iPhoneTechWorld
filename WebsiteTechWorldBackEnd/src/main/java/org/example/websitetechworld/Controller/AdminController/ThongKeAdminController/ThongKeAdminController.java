package org.example.websitetechworld.Controller.AdminController.ThongKeAdminController;

import org.example.websitetechworld.Dto.Response.AdminResponse.ThongKeAdminResponse.DashboardAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.ThongKeAdminResponse.ThongKeDonhangAdminResponse;
import org.example.websitetechworld.Services.AdminServices.ThongkeAdminService.ThongKeAdminService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping ("/admin/thong-ke")

public class ThongKeAdminController {

    private final ThongKeAdminService thongKeAdminService;

    public ThongKeAdminController(ThongKeAdminService thongKeAdminService) {
        this.thongKeAdminService = thongKeAdminService;
    }

//    @GetMapping("/doanh-thu-thang")
//    public BigDecimal doanhThuThang () {
//
//        return dashboardAdminService.getDoanhThuThang();
//    }
//
//    @GetMapping("/tong-so-don-hang")
//    public Integer tongSoDonhang () {
//
//        return dashboardAdminService.getTongSoDonHang();
//    }
//
//    @GetMapping("/tong-so-san-pham")
//    public Integer tongSoSanPham () {
//
//        return dashboardAdminService.getTongSoSanPham();
//    }
//
//    @GetMapping("/tong-so-khach-hang")
//    public Integer tongSoKhachHang () {
//
//        return dashboardAdminService.getTongSoKhachHang();
//    }

    @GetMapping("/tong-quan")
    public DashboardAdminResponse tongQuan(@RequestParam String startDate,
                                           @RequestParam String endDate) {
        return thongKeAdminService.getDashboardAdminResponse(startDate, endDate);
    }


    @GetMapping("/san-pham-ban-chay")
    public List<Map<String, Object>> sanPhamBanChay () {

        return thongKeAdminService.getSanPhamBanChay();
    }

    @GetMapping("/don-hang-moi-nhat")
    public List<Map<String, Object>> donHangMoiNhat () {

        return thongKeAdminService.getDonHangMoiNhat();
    }


    @GetMapping("/doanh-thu-theo-ngay")
    public List<Map<String, Object>> doanhThuTheoNgay () {

        return thongKeAdminService.doanhThuTheoNgay();
    }

    @GetMapping("/doanh-thu-theo-thang")
    public List<Map<String, Object>> doanhThuTheoThang () {

        return thongKeAdminService.doanhThuTheoThang();
    }
    @GetMapping("/don-huy-theo-thang")
    public List<Map<String, Object>> donHuyTheoThang () {

        return thongKeAdminService.donHuyTheoThang();
    }

    @GetMapping("/doanh-thu-theo-khach-hang")
    public List<Map<String, Object>> thongKeTheoKhachHang () {

        return thongKeAdminService.doanhThuTheoKhachHang();
    }


    @GetMapping("/top-khach-hang-mua-nhieu")
    public List<Map<String, Object>> topKhachHangMuaNhieu () {

        return thongKeAdminService.topKhachHangMuaNhieu();
    }

    @GetMapping("/khach-hang-trung-thanh")
    public List<Map<String, Object>> khachHangTrungThanh () {

        return thongKeAdminService.khachHangTrungThanh();
    }

    @GetMapping("/khach-hang-hang-cao")
    public List<Map<String, Object>> khachHangHangCao () {

        return thongKeAdminService.khachHangHangCao();
    }


    @GetMapping("/san-pham-ton-kho")
    public List<Map<String, Object>> sanPhamTonKhoNhieu () {

        return thongKeAdminService.sanPhamTonKhoNhieu();
    }

    @GetMapping("/san-pham-sap-het-hang")
    public List<Map<String, Object>> sanPhamSapHetHang () {

        return thongKeAdminService.sanPhamSapHetHang();
    }

    @GetMapping("/don-hang")
    public ThongKeDonhangAdminResponse thongKeDonHang () {

        return thongKeAdminService.thongKeDonhangAdminResponse();
    }
    @GetMapping("/top-san-pham-ban-chay")
    public List<Map<String, Object>> getSanPhamBanChayTheoNgay(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate")   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return thongKeAdminService.getSanPhamBanChayTheoNgay(startDate, endDate);
    }
}
