package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.AdminResponse.DanhGiaSanPhamAdminResponse.DanhGiaSanPhamAdminResponse;
import org.example.websitetechworld.Entity.DanhGiaSanPham;
import org.example.websitetechworld.Enum.DanhGiaSanPham.TrangThaiDanhGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Repository
public interface DanhGiaSanPhamRepository extends JpaRepository<DanhGiaSanPham, Integer>{
    // Tìm đánh giá theo sản phẩm chi tiết
    List<DanhGiaSanPham> findByIdSanPhamChiTiet_Id(Integer idSanPhamChiTiet);

    @Query("""
    SELECT d FROM DanhGiaSanPham d
    LEFT JOIN FETCH d.mediaList m
    WHERE d.idSanPhamChiTiet.id = :idSanPhamChiTiet
    AND (:soSao IS NULL OR d.soSao = :soSao)
    AND (:hasMedia IS NULL OR SIZE(d.mediaList) > 0)
    AND (
         d.trangThaiDanhGia = 'APPROVED'
         OR (d.trangThaiDanhGia = 'PENDING_APPROVAL' AND d.idKhachHang.id = :idKhachHang)
    )
    """)
    List<DanhGiaSanPham> findBySanPhamChiTietAndFilters(
            @Param("idSanPhamChiTiet") Integer idSanPhamChiTiet,
            @Param("soSao") Integer soSao,
            @Param("hasMedia") Boolean hasMedia,
            @Param("idKhachHang") Integer idKhachHang);




    // Tìm đánh giá theo khách hàng
    List<DanhGiaSanPham> findByIdKhachHang_Id(Integer idKhachHang);

    // Tìm đánh giá theo trạng thái
    List<DanhGiaSanPham> findByTrangThaiDanhGia(TrangThaiDanhGia trangThai);

    // Tìm đánh giá theo số sao
    List<DanhGiaSanPham> findBySoSao(Integer soSao);

    // Tính điểm trung bình đánh giá của sản phẩm chi tiết
    @Query("SELECT AVG(d.soSao) FROM DanhGiaSanPham d WHERE d.idSanPhamChiTiet.id = ?1")
    Double tinhDiemTrungBinhSanPhamChiTiet(Integer idSanPhamChiTiet);

    // Tính điểm trung bình đánh giá của sản phẩm
    @Query("SELECT AVG(d.soSao) FROM DanhGiaSanPham d WHERE d.idSanPhamChiTiet.idSanPham.id = ?1")
    Double tinhDiemTrungBinhTheoSanPham(Integer idSanPham);

    // Đếm số lượng đánh giá theo từng số sao
    @Query("SELECT d.soSao, COUNT(d) FROM DanhGiaSanPham d WHERE d.idSanPhamChiTiet.id = ?1 GROUP BY d.soSao")
    List<Object[]> demSoLuongTheoSoSao(Integer idSanPhamChiTiet);

    // Tìm đánh giá có phản hồi
    @Query("SELECT d FROM DanhGiaSanPham d WHERE d.phanHoiList IS NOT EMPTY")
    List<DanhGiaSanPham> findDanhGiaCoPhanhoi();

    // Tìm đánh giá theo khoảng thời gian
    List<DanhGiaSanPham> findByNgayDanhGiaBetween(LocalDateTime tuNgay, LocalDateTime denNgay);


    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM DanhGiaSanPham d WHERE d.idChiTietHoaDon.id = :idChiTietHoaDon")
    boolean existsByChiTietHoaDonId(@Param("idChiTietHoaDon") Integer idChiTietHoaDon);




//    admin


    @Query(
            value = """
        SELECT
            dg.id_danh_gia,
            kh.ten_khach_hang,
            spct.ma_san_pham_chi_tiet,
            sp.ten_san_pham,
            ms.ten_mau,
            r.dung_luong_rom,
            ra.dung_luong_ram,
            dg.so_sao,
            dg.noi_dung,
            (
                SELECT STUFF((
                    SELECT ', ' + m.url_media
                    FROM media_danh_gia m
                    WHERE m.id_danh_gia = dg.id_danh_gia AND m.loai_media = 'IMAGE'
                    FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'), 1, 2, '')
            ) AS anh_url,
            (
                SELECT STUFF((
                    SELECT ', ' + m.url_media
                    FROM media_danh_gia m
                    WHERE m.id_danh_gia = dg.id_danh_gia AND m.loai_media = 'VIDEO'
                    FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'), 1, 2, '')
            ) AS video_url,
            phdg.noi_dung AS noi_dung_phan_hoi,
            dg.ngay_danh_gia,
            dg.trang_thai_danh_gia
        FROM danh_gia_san_pham AS dg
        JOIN khach_hang AS kh ON kh.id_khach_hang = dg.id_khach_hang
        JOIN san_pham_chi_tiet AS spct ON spct.id_san_pham_chi_tiet = dg.id_san_pham_chi_tiet
        JOIN san_pham AS sp ON sp.id_san_pham = spct.id_san_pham
        JOIN mau_sac AS ms ON ms.id_mau_sac = spct.id_mau
        JOIN rom AS r ON r.id_rom = spct.id_rom
        JOIN model_san_pham AS msp ON msp.id_model_san_pham = sp.id_model_san_pham
        JOIN ram AS ra ON ra.id_ram = msp.id_ram
        LEFT JOIN phan_hoi_danh_gia AS phdg ON phdg.id_danh_gia = dg.id_danh_gia
        WHERE (:soSao IS NULL OR dg.so_sao = :soSao)
        AND (:trangThai IS NULL OR dg.trang_thai_danh_gia = :trangThai)
        ORDER BY dg.ngay_danh_gia DESC
    """,
            countQuery = """
        SELECT COUNT(*)
        FROM danh_gia_san_pham AS dg
        JOIN khach_hang AS kh ON kh.id_khach_hang = dg.id_khach_hang
        JOIN san_pham_chi_tiet AS spct ON spct.id_san_pham_chi_tiet = dg.id_san_pham_chi_tiet
        JOIN san_pham AS sp ON sp.id_san_pham = spct.id_san_pham
        JOIN mau_sac AS ms ON ms.id_mau_sac = spct.id_mau
        JOIN rom AS r ON r.id_rom = spct.id_rom
        JOIN model_san_pham AS msp ON msp.id_model_san_pham = sp.id_model_san_pham
        JOIN ram AS ra ON ra.id_ram = msp.id_ram
        LEFT JOIN phan_hoi_danh_gia AS phdg ON phdg.id_danh_gia = dg.id_danh_gia
        WHERE (:soSao IS NULL OR dg.so_sao = :soSao)
        AND (:trangThai IS NULL OR dg.trang_thai_danh_gia = :trangThai)
    """,
            nativeQuery = true
    )
    Page<Object[]> findAllDanhGia(
            @Param("soSao") Integer soSao,
            @Param("trangThai") String trangThai,
            Pageable pageable
    );

//
//    @Query(value = "SELECT url_media FROM media_danh_gia WHERE id_danh_gia = :id AND loai_media = 'IMAGE'", nativeQuery = true)
//    List<String> findAnhByDanhGiaId(@Param("id") Integer id);
//
//    @Query(value = "SELECT url_media FROM media_danh_gia WHERE id_danh_gia = :id AND loai_media = 'VIDEO'", nativeQuery = true)
//    List<String> findVideoByDanhGiaId(@Param("id") Integer id);


    @Query(value = """

            SELECT\s
                dg.id_danh_gia,
                cthd.id_hoa_don,
                cthd.id_san_pham_chi_tiet,
                cthd.id_chi_tiet_hoa_don,
                dg.id_khach_hang,
                dg.so_sao,
                dg.noi_dung,
                dg.trang_thai_danh_gia,
                m.id_media,
                m.loai_media,
                m.url_media,
                m.ngay_upload,
                m.trang_thai_media_danh_gia
            FROM danh_gia_san_pham dg
            JOIN chi_tiet_hoa_don cthd ON cthd.id_chi_tiet_hoa_don = dg.id_chi_tiet_hoa_don
            LEFT JOIN media_danh_gia m ON m.id_danh_gia = dg.id_danh_gia
            WHERE cthd.id_hoa_don = :idHoaDon
        """, nativeQuery = true)
    List<Object[]> getDanhGiaByHoaDon(@Param("idHoaDon") Integer idHoaDon);


    @Query(value = """
    SELECT 
        CASE 
            WHEN COUNT(cthd.id_chi_tiet_hoa_don) = 
                 SUM(CASE 
                        WHEN dgsp.id_danh_gia IS NOT NULL AND dgsp.id_khach_hang = :idKhachHang 
                        THEN 1 ELSE 0 
                     END)
            THEN CAST(1 AS BIT) 
            ELSE CAST(0 AS BIT) 
        END AS da_danh_gia
    FROM chi_tiet_hoa_don cthd
    LEFT JOIN danh_gia_san_pham dgsp 
        ON dgsp.id_chi_tiet_hoa_don = cthd.id_chi_tiet_hoa_don 
    WHERE cthd.id_hoa_don = :idHoaDon
    """, nativeQuery = true)
    boolean checkDaDanhGia(@Param("idHoaDon") Integer idHoaDon,
                           @Param("idKhachHang") Integer idKhachHang);


    @Query(value = """
    SELECT 
        CASE 
            WHEN COUNT(cthd.id_chi_tiet_hoa_don) = 
                 SUM(CASE 
                        WHEN dgsp.id_danh_gia IS NOT NULL AND dgsp.id_khach_hang = :idKhachHang 
                        THEN 1 ELSE 0 
                     END)
            THEN CAST(1 AS BIT) 
            ELSE CAST(0 AS BIT) 
        END AS da_danh_gia,
        CASE 
            WHEN COUNT(phdg.id_phan_hoi) > 0 
            THEN CAST(1 AS BIT) 
            ELSE CAST(0 AS BIT) 
        END AS co_phan_hoi
    FROM chi_tiet_hoa_don cthd
    LEFT JOIN danh_gia_san_pham dgsp 
        ON dgsp.id_chi_tiet_hoa_don = cthd.id_chi_tiet_hoa_don 
    LEFT JOIN phan_hoi_danh_gia phdg 
        ON phdg.id_danh_gia = dgsp.id_danh_gia
    WHERE cthd.id_hoa_don = :idHoaDon
    """, nativeQuery = true)
    Map<String, Boolean> checkDaDanhGiaVaPhanHoi(@Param("idHoaDon") Integer idHoaDon,
                                                 @Param("idKhachHang") Integer idKhachHang);


}

