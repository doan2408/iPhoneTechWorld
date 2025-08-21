package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.KhuyenMaiSanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhuyenMaiSanPhamChiTietRepo extends JpaRepository<KhuyenMaiSanPhamChiTiet, Integer> {
    List<KhuyenMaiSanPhamChiTiet> findByIdKhuyenMai_Id(Integer idKhuyenMaiId);

    List<KhuyenMaiSanPhamChiTiet> findByIdSanPhamChiTiet_Id(Integer idSanPhamChiTietId);
}
