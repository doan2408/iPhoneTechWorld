package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamHienThiAdminResponse;
import org.example.websitetechworld.Entity.SanPham;
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
                spct.gia_ban,
                l.ten_loai,
                SUM(spct.so_luong) AS so_luong,
                ha.url
            FROM san_pham_chi_tiet as spct
            JOIN san_pham as sp ON sp.id_san_pham = spct.id_san_pham
            JOIN loai as l ON l.id_loai = spct.id_loai
            JOIN hinh_anh as ha ON ha.id_hinh_anh = spct.id_san_pham_chi_tiet
            where 
                1 = 1
            GROUP BY
                sp.id_san_pham,
                sp.ma_san_pham,
                sp.ten_san_pham,
                l.ten_loai,
                sp.trang_thai,
                spct.gia_ban,
                ha.url
        """, nativeQuery = true)
    Page<Object[]> getAllHienThi(Pageable pageable);


    Boolean existsByTenSanPham (String tenSanPham);
}
