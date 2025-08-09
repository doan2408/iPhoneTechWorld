package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.KhuyenMai;
import org.example.websitetechworld.Entity.KhuyenMaiSanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhuyenMaiSanPhamChiTietRepository extends JpaRepository<KhuyenMaiSanPhamChiTiet, Integer> {
    List<KhuyenMaiSanPhamChiTiet> findAllByKhuyenMai(KhuyenMai khuyenMai);

    List<KhuyenMaiSanPhamChiTiet> findByKhuyenMai_Id(Integer khuyenMaiId);
}
