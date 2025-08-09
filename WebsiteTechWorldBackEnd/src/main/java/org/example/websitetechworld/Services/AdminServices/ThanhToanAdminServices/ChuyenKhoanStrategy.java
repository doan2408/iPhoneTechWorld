package org.example.websitetechworld.Services.AdminServices.ThanhToanAdminServices;

import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.ThanhToanAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ThanhToanAdminResponse;
import org.example.websitetechworld.Entity.ChiTietThanhToan;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.PhuongThucThanhToan;
import org.example.websitetechworld.Enum.HoaDon.TrangThaiThanhToan;
import org.example.websitetechworld.Repository.ChiTietThanhToanRepository;
import org.example.websitetechworld.Repository.PhuongThucThanhToanRepository;
import org.example.websitetechworld.Services.ClientServices.DiemServices.LichSuDiemService;
import org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices.ThanhToanStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class ChuyenKhoanStrategy implements ThanhToanStrategy {

    private final ChiTietThanhToanRepository chiTietThanhToanRepository;
    private final PhuongThucThanhToanRepository phuongThucThanhToanRepository;
    private final LichSuDiemService lichSuDiemService;

    public ChuyenKhoanStrategy(ChiTietThanhToanRepository chiTietThanhToanRepository, PhuongThucThanhToanRepository phuongThucThanhToanRepository, LichSuDiemService lichSuDiemService) {
        this.chiTietThanhToanRepository = chiTietThanhToanRepository;
        this.phuongThucThanhToanRepository = phuongThucThanhToanRepository;
        this.lichSuDiemService = lichSuDiemService;
    }

    @Override
    public ThanhToanAdminResponse thanhToan(HoaDon hoaDon, ThanhToanAdminRequest request) {

        if (hoaDon == null) {
            throw new IllegalArgumentException("Hóa đơn không hợp lệ.");
        }
        if (hoaDon.getThanhTien() == null || hoaDon.getThanhTien().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Tổng tiền hóa đơn không hợp lệ.");
        }
        if (hoaDon.getTrangThaiThanhToan() == TrangThaiThanhToan.PAID) {
            throw new IllegalArgumentException("Hóa đơn đã được thanh toán.");
        }

        BigDecimal soTienThanhToan = request.getSoTienKhachDua();
        if (soTienThanhToan == null || soTienThanhToan.compareTo(hoaDon.getThanhTien()) < 0) {
            throw new IllegalArgumentException("Số tiền thanh toán không đủ.");
        }

        PhuongThucThanhToan phuongThucThanhToan = phuongThucThanhToanRepository
                .findOneByTenPhuongThuc(request.getHinhThucThanhToan());
        if (phuongThucThanhToan == null) {
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ: " +
                    request.getHinhThucThanhToan());
        }

        if (hoaDon.getIsShipping() == null || !hoaDon.getIsShipping()){
            hoaDon.setTrangThaiThanhToan(TrangThaiThanhToan.COMPLETED);
            lichSuDiemService.congDiemTuHoaDon(hoaDon.getId());
        }else {
            hoaDon.setTrangThaiThanhToan(TrangThaiThanhToan.PAID);
        }
        hoaDon.setNgayThanhToan(LocalDateTime.now());


        ChiTietThanhToan cttt = new ChiTietThanhToan();
        cttt.setIdHoaDon(hoaDon);
        cttt.setSoTienThanhToan(hoaDon.getThanhTien());
        cttt.setIdPhuongThucThanhToan(phuongThucThanhToan);

        chiTietThanhToanRepository.save(cttt);
        return new ThanhToanAdminResponse("Thanh toán thành công",hoaDon.getThanhTien(),null);
    }
}
