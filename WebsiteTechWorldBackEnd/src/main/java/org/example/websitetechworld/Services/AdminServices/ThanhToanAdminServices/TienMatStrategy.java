package org.example.websitetechworld.Services.AdminServices.ThanhToanAdminServices;

import org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest.ThanhToanAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ThanhToanAdminResponse;
import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.ChiTietThanhToan;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.PhuongThucThanhToan;
import org.example.websitetechworld.Enum.HoaDon.TrangThaiThanhToan;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Repository.ChiTietThanhToanRepository;
import org.example.websitetechworld.Repository.PhuongThucThanhToanRepository;
import org.example.websitetechworld.Services.AdminServices.GiaoHangAdminServces.GiaoHangAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.Imei.HoaDonChiTiet_ImeiAdminServices;
import org.example.websitetechworld.Services.ClientServices.DiemServices.LichSuDiemService;
import org.example.websitetechworld.Services.CommonSerivces.ThanhToanCommonServices.ThanhToanStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class TienMatStrategy implements ThanhToanStrategy {

    private final ChiTietThanhToanRepository chiTietThanhToanRepository;
    private final PhuongThucThanhToanRepository phuongThucThanhToanRepository;
    private final LichSuDiemService lichSuDiemService;
    private final GiaoHangAdminServices giaoHangAdminServices;
    private final HoaDonChiTiet_ImeiAdminServices hoaDonChiTiet_ImeiAdminServices;

    public TienMatStrategy(ChiTietThanhToanRepository chiTietThanhToanRepository, PhuongThucThanhToanRepository phuongThucThanhToanRepository, LichSuDiemService lichSuDiemService, GiaoHangAdminServices giaoHangAdminServices, HoaDonChiTiet_ImeiAdminServices hoaDonChiTiet_ImeiAdminServices) {
        this.chiTietThanhToanRepository = chiTietThanhToanRepository;
        this.phuongThucThanhToanRepository = phuongThucThanhToanRepository;
        this.lichSuDiemService = lichSuDiemService;
        this.giaoHangAdminServices = giaoHangAdminServices;
        this.hoaDonChiTiet_ImeiAdminServices = hoaDonChiTiet_ImeiAdminServices;
    }


    @Override
    public ThanhToanAdminResponse thanhToan(HoaDon hoaDon, ThanhToanAdminRequest request) {
        BigDecimal soTienKhachDua = request.getSoTienKhachDua();
        BigDecimal thanhTien = hoaDon.getThanhTien();

        if (soTienKhachDua == null || soTienKhachDua.compareTo(thanhTien) < 0) {
            throw new IllegalArgumentException("Số tiền khách đưa không đủ để thanh toán.");
        }

        BigDecimal tienThua = soTienKhachDua.subtract(thanhTien);

        if (hoaDon.getIsShipping() == null || !hoaDon.getIsShipping()){
            List<ChiTietHoaDon> danhSachChiTiet = giaoHangAdminServices.getHoaDonChiTietByMaHoaDon(hoaDon.getMaHoaDon());
            hoaDon.setTrangThaiThanhToan(TrangThaiThanhToan.COMPLETED);
            hoaDonChiTiet_ImeiAdminServices.updateImeiStautusFromHoaDon(danhSachChiTiet, TrangThaiImei.SOLD);
            lichSuDiemService.congDiemTuHoaDon(hoaDon.getId());
        }else {
            hoaDon.setTrangThaiThanhToan(TrangThaiThanhToan.PAID);
        }
        hoaDon.setNgayThanhToan(LocalDateTime.now());

        PhuongThucThanhToan phuongThucThanhToan = phuongThucThanhToanRepository.findOneByTenPhuongThuc(request.getHinhThucThanhToan());

        ChiTietThanhToan cttt = new ChiTietThanhToan();
        cttt.setIdHoaDon(hoaDon);
        cttt.setSoTienThanhToan(thanhTien);
        cttt.setIdPhuongThucThanhToan(phuongThucThanhToan);
        chiTietThanhToanRepository.save(cttt);

        return new ThanhToanAdminResponse("Thanh toán thành công", tienThua,null);
    }
}
