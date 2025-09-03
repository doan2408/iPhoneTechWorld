package org.example.websitetechworld.Services.AdminServices.ThongkeAdminService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.websitetechworld.Dto.Response.AdminResponse.ThongKeAdminResponse.DashboardAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.ThongKeAdminResponse.ThongKeDonhangAdminResponse;
import org.example.websitetechworld.Repository.ThongKeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    public DashboardAdminResponse getDashboardAdminResponse(String startDate, String endDate) {

        DashboardAdminResponse response = new DashboardAdminResponse();
        response.setDoanhThuThang(thongKeRepository.doanhThuTheoKhoang(startDate, endDate));
        response.setTongSoDonhang(thongKeRepository.soDonHangTheoKhoang(startDate, endDate));
        response.setTongSoSanPham(thongKeRepository.soSanPhamTheoKhoang(startDate, endDate));
        response.setTongSoKhachHang(thongKeRepository.tongSoKhachHang());

        return response;
    }

    public List<Map<String, Object>> getSanPhamBanChay() {

        return thongKeRepository.sanPhamBanChay();
    }

    public List<Map<String, Object>> getDonHangMoiNhat() {

        return thongKeRepository.donHangMoiNhat();
    }


    public List<Map<String, Object>> doanhThuTheoNgay() {

        return thongKeRepository.thongKeDoanhThuTheoNgay();
    }

    public List<Map<String, Object>> doanhThuTheoThang() {

        return thongKeRepository.thongKeDoanhThuTheoThang();
    }

    public List<Map<String, Object>> donHuyTheoThang() {

        return thongKeRepository.thongKeDonHuyTheoThang();
    }

    public List<Map<String, Object>> doanhThuTheoKhachHang() {

        return thongKeRepository.thongKeTheoKhachHang();
    }


    public List<Map<String, Object>> topKhachHangMuaNhieu() {

        return thongKeRepository.topKhachHangMuaNhieu();
    }

    public List<Map<String, Object>> khachHangTrungThanh() {

        return thongKeRepository.khachHangTrungThanh();
    }

    public List<Map<String, Object>> khachHangHangCao() {

        return thongKeRepository.khachHangHangCao();
    }


    public List<Map<String, Object>> sanPhamTonKhoNhieu() {

        return thongKeRepository.sanPhamTonKhoNhieu();
    }

    public List<Map<String, Object>> sanPhamSapHetHang() {

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


    public ThongKeDonhangAdminResponse thongKeDonhangAdminResponse() {

        ThongKeDonhangAdminResponse response = new ThongKeDonhangAdminResponse();
        response.setTongSoDonHang(thongKeRepository.tongSoDonHang());
        response.setDonHangChuaXuLy(thongKeRepository.donHangChuaXuLy());
        response.setDonHangDaThanhToan(thongKeRepository.donHangDaThanhToan());
        response.setDonHangBiHuy(thongKeRepository.donHangBiHuy());
        response.setDonHangDaHoanTien(thongKeRepository.donHangDaHoanTien());
        response.setDonHangDaHoanTat(thongKeRepository.donHangDaHoanTat());

        return response;
    }

    public void exportDashboardExcel(HttpServletResponse response, String startDate, String endDate) throws IOException {
        // Lấy dữ liệu tổng quan từ repository
        DashboardAdminResponse dashboard = getDashboardAdminResponse(startDate, endDate);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Tổng quan");

        // Tạo header
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Nội Dung");
        headerRow.createCell(1).setCellValue("Giá trị");

        // Thêm dữ liệu
        int rowNum = 1;

        Row row1 = sheet.createRow(rowNum++);
        row1.createCell(0).setCellValue("Tổng doanh thu");
        row1.createCell(1).setCellValue(dashboard.getDoanhThuThang().doubleValue());

        Row row2 = sheet.createRow(rowNum++);
        row2.createCell(0).setCellValue("Tổng số đơn hàng");
        row2.createCell(1).setCellValue(dashboard.getTongSoDonhang());

        Row row3 = sheet.createRow(rowNum++);
        row3.createCell(0).setCellValue("Tổng số sản phẩm");
        row3.createCell(1).setCellValue(dashboard.getTongSoSanPham());

        Row row4 = sheet.createRow(rowNum++);
        row4.createCell(0).setCellValue("Tổng số khách hàng");
        row4.createCell(1).setCellValue(dashboard.getTongSoKhachHang());

        // Auto-size cột
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        // Thiết lập response
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=dashboard-tong-quan.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    //Xuất doanh thu theo tháng
    public void exportDoanhThuTheoThangExcel(HttpServletResponse response) throws IOException {
        List<Map<String, Object>> doanhThuList = thongKeRepository.thongKeDoanhThuTheoThang();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Doanh thu theo tháng");

        // Header
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Tháng");
        headerRow.createCell(1).setCellValue("Doanh thu");

        int rowNum = 1;
        for (Map<String, Object> item : doanhThuList) {
            Row row = sheet.createRow(rowNum++);

            Object thang = item.get("thang");
            Object doanhThu = item.get("tong_doanh_thu");

            row.createCell(0).setCellValue(thang != null ? thang.toString() : "");
            row.createCell(1).setCellValue(doanhThu != null ? Double.parseDouble(doanhThu.toString()) : 0);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"doanh-thu-theo-thang.xlsx\"");

        try (ServletOutputStream out = response.getOutputStream()) {
            workbook.write(out);
            workbook.close(); // Đóng workbook sau khi ghi xong
        }

    }


    // Xuất khách hàng trung thành
    public void exportKhachHangTrungThanhExcel(HttpServletResponse response) throws IOException {
        List<Map<String, Object>> khachHangList = thongKeRepository.khachHangTrungThanh();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Khách hàng trung thành");

        // Header
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Tên khách hàng");
        headerRow.createCell(1).setCellValue("Số đơn hàng");
        headerRow.createCell(2).setCellValue("Tổng doanh thu (VNĐ)");

        int rowNum = 1;
        for (Map<String, Object> item : khachHangList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(item.get("ten_khach_hang") != null ? item.get("ten_khach_hang").toString() : "");
            row.createCell(1).setCellValue(item.get("so_lan_mua") != null ? Integer.parseInt(item.get("so_lan_mua").toString()) : 0);
            row.createCell(2).setCellValue(item.get("tong_doanh_thu") != null ? Double.parseDouble(item.get("tong_doanh_thu").toString()) : 0);
        }

        // Auto-size columns
        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        // Response setup
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=khach-hang-trung-thanh.xlsx");

        // Xuất file
        try (ServletOutputStream out = response.getOutputStream()) {
            workbook.write(out);
            workbook.close();
        }
    }

}
