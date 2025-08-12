package org.example.websitetechworld.Services.ClientServices.ThanhToanClientService;

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
public class NganHangStrategy implements ThanhToanStrategy {

    private final ChiTietThanhToanRepository chiTietThanhToanRepository;
    private final PhuongThucThanhToanRepository phuongThucThanhToanRepository;
    private final LichSuDiemService lichSuDiemService;

    public NganHangStrategy(ChiTietThanhToanRepository chiTietThanhToanRepository, PhuongThucThanhToanRepository phuongThucThanhToanRepository, LichSuDiemService lichSuDiemService) {
        this.chiTietThanhToanRepository = chiTietThanhToanRepository;
        this.phuongThucThanhToanRepository = phuongThucThanhToanRepository;
        this.lichSuDiemService = lichSuDiemService;
    }

    @Override
    public ThanhToanAdminResponse thanhToan(HoaDon hoaDon, ThanhToanAdminRequest request) {

        if (hoaDon == null) {
            throw new IllegalArgumentException("Hóa đơn không hợp lệ.");
        }
        if (hoaDon.getTrangThaiThanhToan() == TrangThaiThanhToan.PAID) {
            throw new IllegalArgumentException("Hóa đơn đã được thanh toán.");
        }

        PhuongThucThanhToan phuongThucThanhToan = phuongThucThanhToanRepository
                .findOneByTenPhuongThuc(request.getHinhThucThanhToan());
        if (phuongThucThanhToan == null) {
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ: " +
                    request.getHinhThucThanhToan());
        }

        if (hoaDon.getIsShipping() == null || !hoaDon.getIsShipping()){
            hoaDon.setTrangThaiThanhToan(TrangThaiThanhToan.COMPLETED);
        }else {
            hoaDon.setTrangThaiThanhToan(TrangThaiThanhToan.PAID);
        }
        hoaDon.setNgayThanhToan(LocalDateTime.now());


        ChiTietThanhToan cttt = new ChiTietThanhToan();
        cttt.setIdHoaDon(hoaDon);
        cttt.setSoTienThanhToan(hoaDon.getThanhTien());
        cttt.setIdPhuongThucThanhToan(phuongThucThanhToan);

        chiTietThanhToanRepository.save(cttt);

        return new ThanhToanAdminResponse("Đặt hàng thành công",hoaDon.getThanhTien(),null);
    }
}
