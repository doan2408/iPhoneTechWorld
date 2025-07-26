package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.ClientResponse.LichSuDiemResponse.LichSuDiemResponse;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.LichSuDiem;
import org.example.websitetechworld.Enum.KhachHang.LoaiDiem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface LichSuDiemRepository extends JpaRepository<LichSuDiem, Integer> {
    @Query(value = "select lsd from LichSuDiem lsd " +
            "where lsd.viDiem.khachHang.id = :idKhachHang " +
            "and (:fromDate is null or lsd.thoiGian >= : fromDate) " +
            "and (:toDate is null or lsd.thoiGian <= : toDate) " +
            "order by lsd.thoiGian desc")
    Page<LichSuDiem> getLichSuDiem(@Param("idKhachHang") Integer idKhachHang,
                                   @Param("fromDate") OffsetDateTime fromDate,
                                   @Param("toDate") OffsetDateTime toDate,
                                   Pageable pageable);

    // Tìm lịch sử điểm cộng còn hạn sử dụng và chưa dùng hết
    @Query("""
    SELECT lsd FROM LichSuDiem lsd
    WHERE lsd.viDiem.khachHang.id = :idKhachHang
      AND lsd.loaiDiem = :loaiDiem
      AND (lsd.hanSuDung IS NULL OR lsd.hanSuDung > :now)
    ORDER BY lsd.thoiGian ASC
    """)
    List<LichSuDiem> findLichSuDiemChuaTru(
            @Param("idKhachHang") Integer idKhachHang,
            @Param("loaiDiem") LoaiDiem loaiDiem,
            @Param("now") OffsetDateTime now
    );

    boolean existsByHoaDon(HoaDon hoaDon);

}
