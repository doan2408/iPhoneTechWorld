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
import java.util.Collection;
import java.util.List;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Integer> {
    @Query("SELECT k FROM KhuyenMai k WHERE " +
            "(:search IS NULL OR k.maKhuyenMai LIKE %:search% OR k.tenKhuyenMai LIKE %:search%) AND " +
            "(:trangThai IS NULL OR k.trangThai = :trangThai) AND " +
            "(:ngayBatDau IS NULL OR k.ngayBatDau >= :ngayBatDau) AND " +
            "(:ngayKetThuc IS NULL OR k.ngayKetThuc <= :ngayKetThuc)")
    Page<KhuyenMai> findAll(
            @Param("search") String search,
            @Param("trangThai") TrangThaiKhuyenMai trangThai,
            @Param("ngayBatDau") LocalDateTime ngayBatDau,
            @Param("ngayKetThuc") LocalDateTime ngayKetThuc,
            Pageable pageable);

    boolean existsByMaKhuyenMai(String maKhuyenMai);

    List<KhuyenMai> findAllByTrangThaiIn(Collection<TrangThaiKhuyenMai> trangThais);

    @Query("""
        SELECT COUNT(*)
        FROM KhuyenMai km 
        WHERE (km.ngayBatDau > :now)
           OR (km.ngayKetThuc > :now)
    """)
    long existsKhuyenMaiCanCapNhatTrongNgay(LocalDateTime now);

    @Query("""
        SELECT COUNT(*)
        FROM KhuyenMai km 
        WHERE (km.ngayBatDau > :now AND km.ngayBatDau <= :nowPlusOneHour)
           OR (km.ngayKetThuc > :now AND km.ngayKetThuc <= :nowPlusOneHour)
    """)
    long existsKhuyenMaiCanCapNhatTrongGio(@Param("now") LocalDateTime now, @Param("nowPlusOneHour") LocalDateTime nowPlusOneHour);

    @Query("""
        SELECT COUNT(*)
        FROM KhuyenMai km 
        WHERE (km.ngayBatDau > :now AND km.ngayBatDau <= :nowPlusOneMinute)
           OR (km.ngayKetThuc > :now AND km.ngayKetThuc <= :nowPlusOneMinute)
    """)
    long existsKhuyenMaiCanCapNhatTrongPhut(@Param("now") LocalDateTime now, @Param("nowPlusOneMinute") LocalDateTime nowPlusOneMinute);

}
