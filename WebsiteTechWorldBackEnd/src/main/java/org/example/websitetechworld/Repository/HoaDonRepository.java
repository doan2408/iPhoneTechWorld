package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Enum.HoaDon.TrangThaiThanhToan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    Page<HoaDon> findByIsDeleteFalseOrIsDeleteIsNull(Pageable pageable);
    Integer countByTrangThaiThanhToan(TrangThaiThanhToan trangThaiThanhToan);

    @Query (value = """
                    SELECT
                        SUM(thanh_tien) AS tong_doanh_thu
                    FROM
                        hoa_don
                    WHERE
                        is_delete = 0
                        AND trang_thai_thanh_toan = 'COMPLETED'
                        AND ngay_thanh_toan IS NOT NULL
                    """, nativeQuery = true)
    BigDecimal doanhThuThang();
}
