package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.GioHang;
import org.example.websitetechworld.Entity.GioHangChiTiet;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet, Integer> {
    void deleteByIdSanPhamChiTiet_Id(Integer idSanPhamChiTietId);

    GioHangChiTiet findByIdGioHangAndIdSanPhamChiTiet(GioHang idGioHang, SanPhamChiTiet idSanPhamChiTiet);
}
