package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Entity.KhachHangGiamGia;
import org.example.websitetechworld.Entity.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface KhachHangGiamGiaRepository extends JpaRepository<KhachHangGiamGia, Integer> {

    @Query("select khgg from KhachHangGiamGia khgg where khgg.idPhieuGiamGia = :idPhieuGiamGia and khgg.isUser = :isUser")
    List<KhachHangGiamGia> findByIdPhieuGiamGiaAndIsUser(
            @Param("idPhieuGiamGia") PhieuGiamGia idPhieuGiamGia,
            @Param("isUser") Boolean isUser
    );

    void deleteByIdPhieuGiamGiaId(Integer idPhieuGiamGia);

    boolean existsByIdPhieuGiamGiaAndIsUser(PhieuGiamGia idPhieuGiamGia, Boolean isUser);

    List<KhachHangGiamGia> findByIdKhachHang_Id(Integer idKhachHangId);

    @Query("SELECT CASE " +
            "WHEN COUNT(k) > 0 THEN true " +
            "ELSE false END " +
            "FROM KhachHangGiamGia k " +
            "WHERE k.idKhachHang.id = :idKhachHang " +
            "AND k.idPhieuGiamGia.id = :idPhieuGiamGia " +
            "AND (k.isUser = false OR (k.isUser = true AND k.ngayCap > :motNamTruoc))")
    boolean checkSoLanDoi(
            @Param("idKhachHang") Integer idKhachHang,
            @Param("idPhieuGiamGia") Integer idPhieuGiamGia,
            @Param("motNamTruoc") LocalDate motNamTruoc
    );

    @Query("""
        SELECT khgg.idPhieuGiamGia 
        FROM KhachHangGiamGia khgg 
        WHERE khgg.idKhachHang.id = :idKhachHang
          AND khgg.isUser = false
          AND khgg.idPhieuGiamGia.trangThaiPhatHanh = 'ISSUED'
          AND (khgg.idPhieuGiamGia.ngayBatDau <= CURRENT_TIMESTAMP AND khgg.idPhieuGiamGia.ngayKetThuc >= CURRENT_TIMESTAMP)
          AND khgg.idPhieuGiamGia.trangThaiPhieuGiamGia = 'ACTIVE'
          AND khgg.idPhieuGiamGia.giaTriDonHangToiThieu <= :giaTriDonHangToiThieu
          AND (:timKiem IS NULL OR LOWER(khgg.idPhieuGiamGia.maGiamGia) LIKE LOWER(CONCAT('%', :timKiem, '%')))
        """)
    List<PhieuGiamGia> findPhieuGiamGiaCuaKhach(
            @Param("idKhachHang") Integer idKhachHang,
            @Param("giaTriDonHangToiThieu") BigDecimal giaTriDonHangToiThieu,
            @Param("timKiem") String timKiem
    );

    KhachHangGiamGia findByIdPhieuGiamGiaAndIdKhachHangAndIsUser(PhieuGiamGia idPhieuGiamGia, KhachHang idKhachHang, Boolean isUser);

    @Query("""
       SELECT k 
       FROM KhachHangGiamGia k 
       WHERE k.idKhachHang.id = :idKhachHang
         AND (:search IS NULL OR k.idPhieuGiamGia.maGiamGia LIKE %:search% OR k.idPhieuGiamGia.tenGiamGia LIKE %:search%)
       """)
    Page<KhachHangGiamGia> findVouchersByKhachHangIdAndTimKiem(@Param("idKhachHang") Integer idKhachHang,
                                                           @Param("search") String search,
                                                           Pageable pageable);
}
