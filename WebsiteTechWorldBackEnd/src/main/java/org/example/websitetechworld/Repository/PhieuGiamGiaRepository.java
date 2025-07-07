package org.example.websitetechworld.Repository;

import jakarta.validation.constraints.Size;
import org.example.websitetechworld.Entity.PhieuGiamGia;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPhatHanh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Integer> {

    @Query("""
        SELECT p FROM PhieuGiamGia p
        WHERE (:search IS NULL OR p.maGiamGia LIKE %:search% OR p.tenKhuyenMai LIKE %:search%)
          AND (:trangThai IS NULL OR p.trangThaiPhieuGiamGia = :trangThai)
          AND (:ngayBatDau IS NULL OR p.ngayBatDau >= :ngayBatDau)
          AND (:ngayKetThuc IS NULL OR p.ngayKetThuc <= :ngayKetThuc)
    """)
    Page<PhieuGiamGia> findAll (
            @Param("search") String search,
            @Param("trangThai") TrangThaiPGG trangThai,
            @Param("ngayBatDau") LocalDate ngayBatDau,
            @Param("ngayKetThuc") LocalDate ngayKetThuc,
            Pageable pageable
    );

    @Query("""
    SELECT p 
    FROM PhieuGiamGia p
    WHERE p.soDiemCanDeDoi = :soDiemCanDeDoi
      AND p.giaTriDonHangToiThieu <= :giaTriDonHangToiThieu
      AND p.trangThaiPhatHanh = :trangThaiPhatHanh
      AND p.congKhai = :congKhai
      AND p.trangThaiPhieuGiamGia = :trangThaiPhieuGiamGia
      AND LOWER(p.maGiamGia) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<PhieuGiamGia> timPhieuGiamGiaCongKhai (
            @Param("soDiemCanDeDoi") BigDecimal soDiemCanDeDoi,
            @Param("giaTriDonHangToiThieu") BigDecimal giaTriDonHangToiThieu,
            @Param("trangThaiPhatHanh") TrangThaiPhatHanh trangThaiPhatHanh,
            @Param("congKhai") Boolean congKhai,
            @Param("trangThaiPhieuGiamGia") TrangThaiPGG trangThaiPhieuGiamGia,
            @Param("keyword") String keyword
    );

}
