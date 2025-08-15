package org.example.websitetechworld.Repository;

import jakarta.validation.constraints.Size;
import org.example.websitetechworld.Entity.PhieuGiamGia;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
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
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Integer> {

    @Query("""
        SELECT p FROM PhieuGiamGia p
        WHERE (:search IS NULL OR p.maGiamGia LIKE %:search% OR p.tenGiamGia LIKE %:search%)
          AND (:trangThai IS NULL OR p.trangThaiPhieuGiamGia = :trangThai)
          AND (:ngayBatDau IS NULL OR p.ngayBatDau >= :ngayBatDau)
          AND (:ngayKetThuc IS NULL OR p.ngayKetThuc <= :ngayKetThuc)
    """)
    Page<PhieuGiamGia> findAll (
            @Param("search") String search,
            @Param("trangThai") TrangThaiPGG trangThai,
            @Param("ngayBatDau") LocalDateTime ngayBatDau,
            @Param("ngayKetThuc") LocalDateTime ngayKetThuc,
            Pageable pageable
    );

    @Query("""
    SELECT p
    FROM PhieuGiamGia p
    WHERE p.soDiemCanDeDoi = :soDiemCanDeDoi
      AND p.giaTriDonHangToiThieu <= :giaTriDonHangToiThieu
      AND p.trangThaiPhatHanh = :trangThaiPhatHanh
      AND p.trangThaiPhieuGiamGia = :trangThaiPhieuGiamGia
      AND LOWER(p.maGiamGia) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<PhieuGiamGia> timPhieuGiamGiaCongKhai (
            @Param("soDiemCanDeDoi") BigDecimal soDiemCanDeDoi,
            @Param("giaTriDonHangToiThieu") BigDecimal giaTriDonHangToiThieu,
            @Param("trangThaiPhatHanh") TrangThaiPhatHanh trangThaiPhatHanh,
            @Param("trangThaiPhieuGiamGia") TrangThaiPGG trangThaiPhieuGiamGia,
            @Param("keyword") String keyword
    );

    @Query("""
        SELECT p FROM PhieuGiamGia p
        WHERE (:search IS NULL OR p.maGiamGia LIKE %:search% OR p.tenGiamGia LIKE %:search%)
          AND (:trangThai IS NULL OR p.trangThaiPhieuGiamGia = :trangThai)
          AND (:ngayBatDau IS NULL OR p.ngayBatDau >= :ngayBatDau)
          AND (:ngayKetThuc IS NULL OR p.ngayKetThuc <= :ngayKetThuc)
          AND (:hangToiThieu IS NULL OR p.hangToiThieu = :hangToiThieu)
          AND (p.trangThaiPhatHanh = 'ISSUED')
    """)
    Page<PhieuGiamGia> getDoiVoucher (
            @Param("search") String search,
            @Param("trangThai") TrangThaiPGG trangThai,
            @Param("ngayBatDau") LocalDate ngayBatDau,
            @Param("ngayKetThuc") LocalDate ngayKetThuc,
            @Param("hangToiThieu") HangKhachHang hangToiThieu,
            Pageable pageable
    );

    @Query("""
        SELECT COUNT(*)
        FROM PhieuGiamGia p 
        WHERE (p.ngayBatDau > :now)
           OR (p.ngayKetThuc > :now)
    """)
    long existsPhieuCanCapNhatTrongNgay(LocalDateTime now);

    @Query("""
        SELECT COUNT(*)
        FROM PhieuGiamGia p 
        WHERE (p.ngayBatDau > :now AND p.ngayBatDau <= :nowPlusOneHour)
           OR (p.ngayKetThuc > :now AND p.ngayKetThuc <= :nowPlusOneHour)
    """)
    long existsPhieuCanCapNhatTrongGio(@Param("now") LocalDateTime now, @Param("nowPlusOneHour") LocalDateTime nowPlusOneHour);

    @Query("""
        SELECT COUNT(*)
        FROM PhieuGiamGia p 
        WHERE (p.ngayBatDau > :now AND p.ngayBatDau <= :nowPlusOneMinute)
           OR (p.ngayKetThuc > :now AND p.ngayKetThuc <= :nowPlusOneMinute)
    """)
    long existsPhieuCanCapNhatTrongPhut(@Param("now") LocalDateTime now, @Param("nowPlusOneMinute") LocalDateTime nowPlusOneMinute);

    List<PhieuGiamGia> findAllByTrangThaiPhieuGiamGiaIn(Collection<TrangThaiPGG> trangThaiPhieuGiamGias);

    boolean existsByMaGiamGia(@Size(max = 50) String maGiamGia);
}
