package org.example.websitetechworld.Services.AdminServices.HoaDonAdminServices.SanPham;

import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.example.websitetechworld.Repository.SanPhamChiTietRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HoaDonChiTiet_SanPhamAdminServices {

    private final SanPhamChiTietRepository sanPhamChiTietRepository;

    public HoaDonChiTiet_SanPhamAdminServices(SanPhamChiTietRepository sanPhamChiTietRepository) {
        this.sanPhamChiTietRepository = sanPhamChiTietRepository;
    }

    public void updateSoLuongProdcut(List<ChiTietHoaDon> chiTietHoaDons){
        List<SanPhamChiTiet> listSanPhamChiTiet = new ArrayList<>();
        for (ChiTietHoaDon chiTietHoaDon : chiTietHoaDons){
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(chiTietHoaDon.getIdSanPhamChiTiet().getId()).orElseThrow(
                    ()-> new IllegalArgumentException("San pham chi tiet nay khong ton tai")
            );
            sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - chiTietHoaDon.getSoLuong());
            listSanPhamChiTiet.add(sanPhamChiTiet);
        }
        sanPhamChiTietRepository.saveAll(listSanPhamChiTiet);

    }
}
