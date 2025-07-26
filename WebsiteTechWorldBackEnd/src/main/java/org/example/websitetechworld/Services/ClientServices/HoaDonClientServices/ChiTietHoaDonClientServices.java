package org.example.websitetechworld.Services.ClientServices.HoaDonClientServices;

import org.example.websitetechworld.Dto.Request.ClientRequest.HoaDon.RequestThanhToanTongHop;
import org.example.websitetechworld.Dto.Request.ClientRequest.SanPhamClientRequest.ChiTietSanPhamRequest;
import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.example.websitetechworld.Repository.ChiTietHoaDonRepository;
import org.example.websitetechworld.Repository.SanPhamChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChiTietHoaDonClientServices {
    private final ChiTietHoaDonRepository chiTietHoaDonRepository;
    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    public ChiTietHoaDonClientServices(ChiTietHoaDonRepository chiTietHoaDonRepository, SanPhamChiTietRepository sanPhamChiTietRepository) {
        this.chiTietHoaDonRepository = chiTietHoaDonRepository;
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
    }

    public List<ChiTietHoaDon> createInvoiceDetail(HoaDon hoaDon, RequestThanhToanTongHop requestThanhToanTongHop){
        List<ChiTietHoaDon> chiTietHoaDons = new ArrayList<>();
        for (ChiTietSanPhamRequest pr : requestThanhToanTongHop.getSanPhamRequests()) {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(pr.getIdSanPham())
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sản phẩm có ID: " + pr.getIdSanPham()));

            ChiTietHoaDon chiTietHoaDon = new ChiTietHoaDon();
            chiTietHoaDon.setIdHoaDon(hoaDon);
            chiTietHoaDon.setTenSanPham(sanPhamChiTiet.getIdSanPham().getTenSanPham());
            chiTietHoaDon.setMoTa("Sản phẩm điện thoại chính hãng.");
            chiTietHoaDon.setIdSanPhamChiTiet(sanPhamChiTiet);
            chiTietHoaDon.setSoLuong(pr.getSoLuong());
            chiTietHoaDon.setDonGia(sanPhamChiTiet.getGiaBan());

            chiTietHoaDonRepository.save(chiTietHoaDon);
            chiTietHoaDons.add(chiTietHoaDon);
        }
        return chiTietHoaDons;
    }
}
