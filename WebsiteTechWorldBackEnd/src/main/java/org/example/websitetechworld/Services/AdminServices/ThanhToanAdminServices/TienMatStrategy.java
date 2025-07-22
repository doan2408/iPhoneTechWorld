package org.example.websitetechworld.Services.AdminServices.ThanhToanAdminServices;

import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.ThanhToanAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ThanhToanAdminResponse;
import org.example.websitetechworld.Entity.ChiTietThanhToan;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.PhuongThucThanhToan;
import org.example.websitetechworld.Enum.HoaDon.TrangThaiThanhToan;
import org.example.websitetechworld.Repository.ChiTietThanhToanRepository;
import org.example.websitetechworld.Repository.PhuongThucThanhToanRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class TienMatStrategy implements ThanhToanStrategy{

    private final ChiTietThanhToanRepository chiTietThanhToanRepository;
    private final PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    public TienMatStrategy(ChiTietThanhToanRepository chiTietThanhToanRepository, PhuongThucThanhToanRepository phuongThucThanhToanRepository) {
        this.chiTietThanhToanRepository = chiTietThanhToanRepository;
        this.phuongThucThanhToanRepository = phuongThucThanhToanRepository;
    }


    @Override
    public ThanhToanAdminResponse thanhToan(HoaDon hoaDon, ThanhToanAdminRequest request) {
        BigDecimal soTienKhachDua = request.getSoTienKhachDua();
        BigDecimal thanhTien = hoaDon.getThanhTien();

        if (soTienKhachDua == null || soTienKhachDua.compareTo(thanhTien) < 0) {
            throw new IllegalArgumentException("Số tiền khách đưa không đủ để thanh toán.");
        }

        BigDecimal tienThua = soTienKhachDua.subtract(thanhTien);

        hoaDon.setTrangThaiThanhToan(TrangThaiThanhToan.PAID);
        hoaDon.setNgayThanhToan(LocalDate.now());

        PhuongThucThanhToan phuongThucThanhToan = phuongThucThanhToanRepository.findOneByTenPhuongThuc(request.getHinhThucThanhToan());

        ChiTietThanhToan cttt = new ChiTietThanhToan();
        cttt.setIdHoaDon(hoaDon);
        cttt.setSoTienThanhToan(thanhTien);
        cttt.setIdPhuongThucThanhToan(phuongThucThanhToan);
        chiTietThanhToanRepository.save(cttt);

        return new ThanhToanAdminResponse("Thanh toán thành công", tienThua);
    }
}
