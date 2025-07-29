package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.NhaCungCap;
import org.example.websitetechworld.Entity.NhaCungCapSp;
import org.example.websitetechworld.Entity.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface NhaCungCapSpRepository extends JpaRepository<NhaCungCapSp, Integer> {

    // Trong NhaCungCapSpRepository
    @Query("SELECT ncs FROM NhaCungCapSp ncs WHERE ncs.sanPham.id = :sanPhamId")
    Optional<NhaCungCapSp> getNhaCungCapSp(@Param("sanPhamId") Integer sanPhamId);

    // Method để tìm tất cả quan hệ theo SanPham - sử dụng List để tránh NonUniqueResultException
    List<NhaCungCapSp> findAllBySanPham(SanPham sanPham);

    // Method để tìm tất cả quan hệ theo NhaCungCap và SanPham - sử dụng List
    List<NhaCungCapSp> findAllByNhaCungCapAndSanPham(NhaCungCap nhaCungCap, SanPham sanPham);

    // Method để tìm tất cả quan hệ theo SanPham ID
    List<NhaCungCapSp> findBySanPham_Id(Integer sanPhamId);

    // Method để kiểm tra tồn tại quan hệ
    boolean existsByNhaCungCapAndSanPham(NhaCungCap nhaCungCap, SanPham sanPham);
}
