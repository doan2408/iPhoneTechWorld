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
    @Query("""
            select new org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamHienThiAdminResponse(
                sp.id,
                sp.maSanPham,
                sp.tenSanPham,
                loai.tenLoai,
                spct.soLuong,
                spct.giaBan,
                hinhAnh.url
            ) from SanPhamChiTiet spct
            join spct.idLoai loai
            join spct.idSanPham sp
            join spct.hinhAnhs hinhAnh
            group by 
                sp.id,
                sp.maSanPham,
                sp.tenSanPham,
                loai.tenLoai,
                spct.soLuong,
                spct.giaBan,
                hinhAnh.url
            """)
    Page<SanPhamHienThiAdminResponse> getAllHienThi(Pageable pageable);

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




}
