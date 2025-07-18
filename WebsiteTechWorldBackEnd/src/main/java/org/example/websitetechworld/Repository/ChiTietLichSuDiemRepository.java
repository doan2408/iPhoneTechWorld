package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.ChiTietLichSuDiem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ChiTietLichSuDiemRepository extends JpaRepository<ChiTietLichSuDiem, Integer> {
    // Tổng số điểm đã trừ từ 1 bản ghi cộng
    @Query("""
    SELECT COALESCE(SUM(ct.soDiemDaTru), 0)
    FROM ChiTietLichSuDiem ct
    WHERE ct.lichSuDiem.id = :idLichSuDiem
""")
    BigDecimal tongDiemDaTruTheoLichSu(@Param("idLichSuDiem") Integer idLichSuDiem);
}
