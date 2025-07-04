package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamHienThiAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamBanHangAdminResponse;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    @Query(value = """
    SELECT
        sp.id_san_pham,
        sp.ma_san_pham,
        sp.ten_san_pham,
        sp.trang_thai,
        l.ten_loai,
        COALESCE(sl.tong_so_luong, 0) AS so_luong,
        ha_min.url AS url
    FROM san_pham AS sp
    JOIN model_san_pham AS msp ON msp.id_model_san_pham = sp.id_model_san_pham
    JOIN loai AS l ON l.id_loai = msp.id_loai
    LEFT JOIN (
        SELECT id_san_pham, SUM(so_luong) AS tong_so_luong
        FROM san_pham_chi_tiet
        GROUP BY id_san_pham
    ) AS sl ON sl.id_san_pham = sp.id_san_pham
    LEFT JOIN (
        SELECT spct.id_san_pham, MIN(ha.url) AS url
        FROM san_pham_chi_tiet AS spct
        JOIN hinh_anh AS ha ON ha.id_san_pham_chi_tiet = spct.id_san_pham_chi_tiet
        GROUP BY spct.id_san_pham
    ) AS ha_min ON ha_min.id_san_pham = sp.id_san_pham
    WHERE 
        (:keyword IS NULL OR :keyword = '' OR 
         LOWER(sp.ma_san_pham) LIKE LOWER(CONCAT(:keyword, '%')) OR 
         LOWER(sp.ten_san_pham) LIKE LOWER(CONCAT(:keyword, '%')))
      AND (:idLoai IS NULL OR l.id_loai = :idLoai)
      AND (:trangThai IS NULL OR :trangThai = '' OR sp.trang_thai = :trangThai)
    ORDER BY sp.id_san_pham
    """,
            countQuery = """
    SELECT COUNT(DISTINCT sp.id_san_pham)
    FROM san_pham AS sp
    JOIN model_san_pham AS msp ON msp.id_model_san_pham = sp.id_model_san_pham
    JOIN loai AS l ON l.id_loai = l.id_loai
    WHERE 
        (:keyword IS NULL OR :keyword = '' OR 
         LOWER(sp.ma_san_pham) LIKE LOWER(CONCAT(:keyword, '%')) OR 
         LOWER(sp.ten_san_pham) LIKE LOWER(CONCAT(:keyword, '%')))
      AND (:idLoai IS NULL OR l.id_loai = :idLoai)
      AND (:trangThai IS NULL OR :trangThai = '' OR sp.trang_thai = :trangThai)
    """,
            nativeQuery = true)
    Page<Object[]> getAllHienThi(
            @Param("keyword") String keyword,
            @Param("idLoai") Integer idLoai,
            @Param("trangThai") String trangThai,
            Pageable pageable);


    @Query(value = """
        SELECT DISTINCT
            CASE
                WHEN ten_san_pham LIKE N'% Pro Max' THEN REPLACE(ten_san_pham, N' Pro Max', N'')
                WHEN ten_san_pham LIKE N'% Pro' THEN REPLACE(ten_san_pham, N' Pro', N'')
                WHEN ten_san_pham LIKE N'% Plus' THEN REPLACE(ten_san_pham, N' Plus', N'')
                WHEN ten_san_pham LIKE N'% Mini' THEN REPLACE(ten_san_pham, N' Mini', N'')
                WHEN ten_san_pham LIKE N'% SE' THEN REPLACE(ten_san_pham, N' SE', N'')
                ELSE ten_san_pham
            END AS ten_dong_san_pham
        FROM san_pham
        """,
            countQuery = """
        SELECT COUNT(*) FROM (
            SELECT DISTINCT
                CASE
                    WHEN ten_san_pham LIKE N'% Pro Max' THEN REPLACE(ten_san_pham, N' Pro Max', N'')
                    WHEN ten_san_pham LIKE N'% Pro' THEN REPLACE(ten_san_pham, N' Pro', N'')
                    WHEN ten_san_pham LIKE N'% Plus' THEN REPLACE(ten_san_pham, N' Plus', N'')
                    WHEN ten_san_pham LIKE N'% Mini' THEN REPLACE(ten_san_pham, N' Mini', N'')
                    WHEN ten_san_pham LIKE N'% SE' THEN REPLACE(ten_san_pham, N' SE', N'')
                    ELSE ten_san_pham
                END AS ten_goc
            FROM san_pham
        ) AS temp
        """,
            nativeQuery = true)
    Page<String> findTenDongSanPham(Pageable pageable);


    Boolean existsByTenSanPham (String tenSanPham);

}
