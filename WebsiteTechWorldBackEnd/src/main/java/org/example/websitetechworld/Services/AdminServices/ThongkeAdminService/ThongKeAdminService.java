package org.example.websitetechworld.Services.AdminServices.ThongkeAdminService;

import org.example.websitetechworld.Dto.Response.AdminResponse.ThongKeAdminResponse.DashboardAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.ThongKeAdminResponse.ThongKeDonhangAdminResponse;
import org.example.websitetechworld.Repository.ThongKeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ThongKeAdminService {

    private final ThongKeRepository thongKeRepository;

    public ThongKeAdminService(ThongKeRepository thongKeRepository, ModelMapper modelMapper) {
        this.thongKeRepository = thongKeRepository;

    }

//    public BigDecimal getDoanhThuThang () {
//
//        BigDecimal doanhThu = thongKeRepository.doanhThuThang();
//        return doanhThu != null ? doanhThu : BigDecimal.ZERO;
//    }
//
//    public int getTongSoDonHang () {
//
//        Integer count = thongKeRepository.dashboardSoDonHang();
//        return count != null ? count : 0;
//    }
//
//    public int getTongSoSanPham () {
//
//        Integer count = thongKeRepository.dashboardSoSanPham();
//        return count != null ? count : 0;
//    }
//
//    public int getTongSoKhachHang () {
//
//        Integer count = thongKeRepository.dashboardSoKhachHang();
//        return count != null ? count : 0;
//    }

    public DashboardAdminResponse getDashboardAdminResponse () {

        DashboardAdminResponse response = new DashboardAdminResponse();
        response.setDoanhThuThang(thongKeRepository.doanhThuThang());
        response.setTongSoDonhang(thongKeRepository.dashboardSoDonHang());
        response.setTongSoSanPham(thongKeRepository.dashboardSoSanPham());
        response.setTongSoKhachHang(thongKeRepository.dashboardSoKhachHang());

        return response;
    }

    public List<Map<String, Object>> getSanPhamBanChay () {

        return thongKeRepository.sanPhamBanChay();
    }

    public List<Map<String, Object>> getDonHangMoiNhat () {

        return thongKeRepository.donHangMoiNhat();
    }


    public List<Map<String, Object>> doanhThuTheoNgay () {

        return thongKeRepository.thongKeDoanhThuTheoNgay();
    }

    public List<Map<String, Object>> doanhThuTheoThang () {

        return thongKeRepository.thongKeDoanhThuTheoThang();
    }
    public List<Map<String, Object>> donHuyTheoThang () {

        return thongKeRepository.thongKeDonHuyTheoThang();
    }

    public List<Map<String, Object>> doanhThuTheoKhachHang () {

        return thongKeRepository.thongKeTheoKhachHang();
    }


    public List<Map<String, Object>> topKhachHangMuaNhieu () {

        return thongKeRepository.topKhachHangMuaNhieu();
    }

    public List<Map<String, Object>> khachHangTrungThanh () {

        return thongKeRepository.khachHangTrungThanh();
    }

    public List<Map<String, Object>> khachHangHangCao () {

        return thongKeRepository.khachHangHangCao();
    }




    public List<Map<String, Object>> sanPhamTonKhoNhieu () {

        return thongKeRepository.sanPhamTonKhoNhieu();
    }

    public List<Map<String, Object>> sanPhamSapHetHang () {

        return thongKeRepository.sanPhamSapHetHang();
    }
    public List<Map<String, Object>> getSanPhamBanChayTheoNgay(LocalDate startDate, LocalDate endDate) {
        return thongKeRepository.sanPhamBanChayTheoNgay(startDate, endDate);
    }


//    public Integer tongSoDonHang () {
//
//        return thongKeRepository.tongSoDonHang();
//    }
//
//    public Integer donHangChuaXuLy () {
//
//        return thongKeRepository.donHangChuaXuLy();
//    }
//
//    public Integer donHangDaThanhToan () {
//
//        return thongKeRepository.donHangDaThanhToan();
//    }
//
//    public Integer donHangBiHuy () {
//
//        return thongKeRepository.donHangBiHuy();
//    }
//
//    public Integer donHangDaHoanTien () {
//
//        return thongKeRepository.donHangDaHoanTien();
//    }
//
//    public Integer donHangDaHoanTat () {
//
//        return thongKeRepository.donHangDaHoanTat();
//    }

    public ThongKeDonhangAdminResponse thongKeDonhangAdminResponse () {

        ThongKeDonhangAdminResponse response = new ThongKeDonhangAdminResponse();
        response.setTongSoDonHang(thongKeRepository.tongSoDonHang());
        response.setDonHangChuaXuLy(thongKeRepository.donHangChuaXuLy());
        response.setDonHangDaThanhToan(thongKeRepository.donHangDaThanhToan());
        response.setDonHangBiHuy(thongKeRepository.donHangBiHuy());
        response.setDonHangDaHoanTien(thongKeRepository.donHangDaHoanTien());
        response.setDonHangDaHoanTat(thongKeRepository.donHangDaHoanTat());

        return response;
    }
}
