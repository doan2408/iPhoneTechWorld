package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.HeDieuHanh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HeDieuHanhRepository extends JpaRepository<HeDieuHanh, Integer> {
    @Query("SELECT hdh FROM HeDieuHanh hdh WHERE " +
            "LOWER(hdh.phienBan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(hdh.nhaPhatTrien) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(hdh.giaoDienNguoiDung) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<HeDieuHanh> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
            SELECT COUNT(h) FROM HeDieuHanh h WHERE
            h.phienBan = :phienBan
""")
    Integer countCheckTrung(@Param("phienBan") String phienBan);
}
