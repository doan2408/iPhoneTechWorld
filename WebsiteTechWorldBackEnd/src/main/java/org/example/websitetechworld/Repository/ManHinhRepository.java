package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.ManHinh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManHinhRepository extends JpaRepository<ManHinh, Integer> {
    @Query("SELECT mh FROM ManHinh mh WHERE " +
            "LOWER(mh.tenManHinh) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(mh.loaiManHinh) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(mh.kichThuoc) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(mh.tanSoQuet) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(mh.doSang) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(mh.chatLieuKinh) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ManHinh> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    //check trung toan bo
    @Query("""
            SELECT COUNT(m) FROM ManHinh m WHERE
            m.tenManHinh = :tenManHinh
            AND m.kichThuoc = :kichThuoc
            AND m.loaiManHinh = :loaiManHinh
            AND m.doPhanGiai = :doPhanGiai
            AND m.tanSoQuet = :tanSoQuet
            AND m.doSang = :doSang
            AND m.chatLieuKinh = :chatLieuKinh
            """)
    Integer countCheckTrung(@Param("tenManHinh") String tenManHinh,
                            @Param("kichThuoc") String kichThuoc,
                            @Param("loaiManHinh") String loaiManHinh,
                            @Param("doPhanGiai") String doPhanGiai,
                            @Param("tanSoQuet") String tanSoQuet,
                            @Param("doSang") String doSang,
                            @Param("chatLieuKinh") String chatLieuKinh);
}
