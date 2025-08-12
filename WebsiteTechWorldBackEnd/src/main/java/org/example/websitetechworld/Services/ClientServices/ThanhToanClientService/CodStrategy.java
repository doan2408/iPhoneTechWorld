package org.example.websitetechworld.Services.ClientServices.ThanhToanClientService;

import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.ThanhToanAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ThanhToanAdminResponse;
import org.example.websitetechworld.Entity.ChiTietThanhToan;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.PhuongThucThanhToan;
import org.example.websitetechworld.Enum.HoaDon.TrangThaiThanhToan;
import org.example.websitetechworld.Repository.ChiTietThanhToanRepository;
import org.example.websitetechworld.Repository.PhuongThucThanhToanRepository;
import org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices.ThanhToanStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class CodStrategy implements ThanhToanStrategy {

    private final ChiTietThanhToanRepository chiTietThanhToanRepository;
    private final PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    public CodStrategy(ChiTietThanhToanRepository chiTietThanhToanRepository, PhuongThucThanhToanRepository phuongThucThanhToanRepository) {
        this.chiTietThanhToanRepository = chiTietThanhToanRepository;
        this.phuongThucThanhToanRepository = phuongThucThanhToanRepository;
    }

    @Override
    public ThanhToanAdminResponse thanhToan(HoaDon hoaDon, ThanhToanAdminRequest request) {

        if (hoaDon == null) {
            throw new IllegalArgumentException("Hóa đơn không hợp lệ.");
        }
        if (hoaDon.getThanhTien() == null || hoaDon.getThanhTien().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Tổng tiền hóa đơn không hợp lệ.");
        }

        PhuongThucThanhToan phuongThucThanhToan = phuongThucThanhToanRepository
                .findOneByTenPhuongThuc(request.getHinhThucThanhToan());
        if (phuongThucThanhToan == null) {
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ: " +
                    request.getHinhThucThanhToan());
        }

        hoaDon.setTrangThaiThanhToan(TrangThaiThanhToan.PENDING);
        hoaDon.setNgayThanhToan(LocalDateTime.now());

        ChiTietThanhToan cttt = new ChiTietThanhToan();
        cttt.setIdHoaDon(hoaDon);
        cttt.setSoTienThanhToan(hoaDon.getThanhTien());
        cttt.setIdPhuongThucThanhToan(phuongThucThanhToan);
        chiTietThanhToanRepository.save(cttt);
        return new ThanhToanAdminResponse("Đặt hàng thành công",hoaDon.getThanhTien(),null);
    }
}
