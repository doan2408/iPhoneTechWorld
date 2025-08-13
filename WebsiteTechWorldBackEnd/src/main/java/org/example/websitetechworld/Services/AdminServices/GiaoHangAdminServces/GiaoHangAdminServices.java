
package org.example.websitetechworld.Services.AdminServices.GiaoHangAdminServces;

import org.example.websitetechworld.Dto.Request.AdminRequest.GiaoHangAdminRequest.AddGIaoHangAdminRequest;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang.GetAllGiaoHangResponseAdmin;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseGiaoHang.ViewGiaoHangAdminResponse;
import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.ChiTietThanhToan;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Enum.GiaoHang.TrangThaiGiaoHang;
import org.example.websitetechworld.Enum.HoaDon.TenHinhThuc;
import org.example.websitetechworld.Enum.HoaDon.TrangThaiThanhToan;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.example.websitetechworld.Repository.ChiTietHoaDonRepository;
import org.example.websitetechworld.Repository.HoaDonRepository;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.Imei.HoaDonChiTiet_ImeiAdminServices;
import org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.SanPham.HoaDonChiTiet_SanPhamAdminServices;
import org.example.websitetechworld.Services.ClientServices.GioHangClientService.GioHangClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GiaoHangAdminServices {

    private final HoaDonRepository hoaDonRepository;
    private final HoaDonChiTiet_ImeiAdminServices hoaDonChiTiet_ImeiAdminServices;
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    private final HoaDonChiTiet_SanPhamAdminServices hoaDonChiTiet_SanPhamAdminServices;
    private final GioHangClientService gioHangClientService;

    public GiaoHangAdminServices(HoaDonRepository hoaDonRepository, HoaDonChiTiet_ImeiAdminServices hoaDonChiTiet_ImeiAdminServices, ChiTietHoaDonRepository chiTietHoaDonRepository, HoaDonChiTiet_SanPhamAdminServices hoaDonChiTiet_SanPhamAdminServices, GioHangClientService gioHangClientService) {
        this.hoaDonRepository = hoaDonRepository;
        this.hoaDonChiTiet_ImeiAdminServices = hoaDonChiTiet_ImeiAdminServices;
        this.chiTietHoaDonRepository = chiTietHoaDonRepository;
        this.hoaDonChiTiet_SanPhamAdminServices = hoaDonChiTiet_SanPhamAdminServices;
        this.gioHangClientService = gioHangClientService;
    }

    public void updateStatus(Integer idHoaDon, TrangThaiGiaoHang newStatus) {
        Optional<HoaDon> optionalGiaoHang = hoaDonRepository.findById(idHoaDon);
        if (!optionalGiaoHang.isPresent()) {
            throw new IllegalArgumentException("Không tìm thấy đơn giao hàng với ID: " + idHoaDon);
        }
        HoaDon hoaDon = optionalGiaoHang.get();
        hoaDon.setTrangThaiDonHang(newStatus);
        List<ChiTietHoaDon> danhSachChiTiet = getHoaDonChiTietByMaHoaDon(hoaDon.getMaHoaDon());
        if (TrangThaiGiaoHang.DELIVERED.equals(hoaDon.getTrangThaiDonHang()) && TrangThaiThanhToan.PAID.equals(hoaDon.getTrangThaiThanhToan())){
            hoaDon.setTrangThaiThanhToan(TrangThaiThanhToan.COMPLETED);
            hoaDonChiTiet_ImeiAdminServices.updateImeiStautusFromHoaDon(danhSachChiTiet, TrangThaiImei.SOLD);
        }
        if (TrangThaiGiaoHang.CONFIRM.equals(newStatus)) {
            Optional<ChiTietThanhToan> chiTietThanhToanDauTien = hoaDon.getChiTietThanhToans()
                    .stream()
                    .findFirst();

            Optional<String> tenPhuongThuc = chiTietThanhToanDauTien
                    .map(ct -> ct.getIdPhuongThucThanhToan().getTenPhuongThuc().name());

            if ("COD".equals(tenPhuongThuc.orElse(""))) {
                hoaDonChiTiet_ImeiAdminServices.ganImeiChoHoaDon(danhSachChiTiet);
                hoaDonChiTiet_ImeiAdminServices.updateImeiStautusFromHoaDon(danhSachChiTiet, TrangThaiImei.RESERVED);
                hoaDonChiTiet_SanPhamAdminServices.updateSoLuongProdcut(danhSachChiTiet);
                for (ChiTietHoaDon chiTietHoaDon : danhSachChiTiet) {
                    gioHangClientService.xoaAllGioHang(chiTietHoaDon.getIdSanPhamChiTiet());
                }
            }
        }

        hoaDonRepository.save(hoaDon);
    }

    public List<ChiTietHoaDon> getHoaDonChiTietByMaHoaDon(String maHoaDon) {
        List<HoaDon> danhSachHoaDon = hoaDonRepository.findByMaHoaDon(maHoaDon);

        if (danhSachHoaDon.isEmpty()) {
            return Collections.emptyList();
        }

        HoaDon hoaDon = danhSachHoaDon.get(0);

        return chiTietHoaDonRepository.findByIdHoaDon_Id(hoaDon.getId());
    }

}

