package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.KhuyenMai;
import org.example.websitetechworld.Enum.KhuyenMai.TrangThaiKhuyenMai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Integer> {
    boolean existsByMaKhuyenMaiAndIdNot(String maKhuyenMai, Integer id);

    @Query("SELECT k FROM KhuyenMai k " +
            "WHERE (:search IS NULL OR k.maKhuyenMai LIKE %:search% OR k.tenKhuyenMai LIKE %:search%) " +
            "AND (:trangThai IS NULL OR k.trangThai = :trangThai) "  +
            "AND (:ngayBatDau IS NULL OR k.ngayBatDau >= :ngayBatDau) " +
            "AND (:ngayKetThuc IS NULL OR k.ngayKetThuc <= :ngayKetThuc)")
    Page<KhuyenMai> findAll (@Param("search") String search,
                                       @Param("trangThai") TrangThaiKhuyenMai trangThai,
                                       @Param("ngayBatDau") LocalDateTime ngayBatDau,
                                       @Param("ngayKetThuc") LocalDateTime ngayKetThuc,
                                       Pageable pageable);
}
