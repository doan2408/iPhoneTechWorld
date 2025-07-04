package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamHienThiAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamBanHangAdminResponse;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
    ORDER BY sp.id_san_pham
""",
            countQuery = """
    SELECT COUNT(DISTINCT sp.id_san_pham)
    FROM san_pham AS sp
    JOIN model_san_pham AS msp ON msp.id_model_san_pham = sp.id_model_san_pham
    JOIN loai AS l ON l.id_loai = msp.id_loai
""",
            nativeQuery = true)
    Page<Object[]> getAllHienThi(Pageable pageable);



//    @Query(
//            value = """
//        SELECT
//            sp.id_san_pham,
//            sp.ma_san_pham,
//            sp.ten_san_pham,
//            sp.trang_thai,
//            l.ten_loai,
//            tong_sl.tong_so_luong,
//            ha.url
//        FROM san_pham sp
//        JOIN (
//            SELECT DISTINCT id_san_pham, id_loai
//            FROM san_pham_chi_tiet
//        ) spct ON sp.id_san_pham = spct.id_san_pham
//        JOIN loai l ON l.id_loai = spct.id_loai
//        OUTER APPLY (
//            SELECT SUM(spct2.so_luong) AS tong_so_luong
//            FROM san_pham_chi_tiet spct2
//            WHERE spct2.id_san_pham = sp.id_san_pham
//        ) tong_sl
//        OUTER APPLY (
//            SELECT TOP 1 ha.url
//            FROM san_pham_chi_tiet spct3
//            JOIN hinh_anh ha ON ha.id_san_pham_chi_tiet = spct3.id_san_pham_chi_tiet
//            WHERE spct3.id_san_pham = sp.id_san_pham
//            ORDER BY ha.id_hinh_anh
//        ) ha
//        """,
//            countQuery = """
//        SELECT COUNT(DISTINCT sp.id_san_pham)
//        FROM san_pham sp
//        JOIN san_pham_chi_tiet spct ON spct.id_san_pham = sp.id_san_pham
//        """,
//            nativeQuery = true
//    )
//    Page<Object[]> getAllHienThi(Pageable pageable);

    @Query(value = """
        SELECT DISTINCT
            LEFT(ten_san_pham, 
                CHARINDEX(' ', ten_san_pham + ' ', CHARINDEX(' ', ten_san_pham + ' ') + 1) - 1
            ) AS ten_dong_san_pham
        FROM san_pham
        """,
            countQuery = """
        SELECT COUNT(*) FROM (
            SELECT DISTINCT
                LEFT(ten_san_pham, 
                    CHARINDEX(' ', ten_san_pham + ' ', CHARINDEX(' ', ten_san_pham + ' ') + 1) - 1
                ) AS ten_dong_san_pham
            FROM san_pham
        ) AS temp
        """,
            nativeQuery = true)
    Page<String> findTenDongSanPham(Pageable pageable);


    Boolean existsByTenSanPham (String tenSanPham);

}
