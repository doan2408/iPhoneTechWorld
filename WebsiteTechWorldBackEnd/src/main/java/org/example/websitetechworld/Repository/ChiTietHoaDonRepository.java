package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon,Integer> {
    List<ChiTietHoaDon> findByIdHoaDon_Id(Integer idHoaDon);
    ChiTietHoaDon findByIdHoaDon_IdAndIdSanPhamChiTiet_Id(Integer idHoaDon, Integer idSanPhamChiTiet);
}
