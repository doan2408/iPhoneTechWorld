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
    AND (:hasMedia IS NULL 
         OR (SIZE(d.mediaList) > 0))
    AND d.trangThaiDanhGia = 'APPROVED'
""")
    List<DanhGiaSanPham> findBySanPhamAndFilters(@Param("idSanPhamChiTiet") Integer idSanPhamChiTiet,
                                                 @Param("soSao") Integer soSao,
                                                 @Param("hasMedia") Boolean hasMedia);


    // Tìm đánh giá theo khách hàng
    List<DanhGiaSanPham> findByIdKhachHang_Id(Integer idKhachHang);

    // Tìm đánh giá theo trạng thái
    List<DanhGiaSanPham> findByTrangThaiDanhGia(TrangThaiDanhGia trangThai);

    // Tìm đánh giá theo số sao
    List<DanhGiaSanPham> findBySoSao(Integer soSao);

    // Tính điểm trung bình đánh giá của sản phẩm
    @Query("SELECT AVG(d.soSao) FROM DanhGiaSanPham d WHERE d.idSanPhamChiTiet.id = ?1")
    Double tinhDiemTrungBinhSanPham(Integer idSanPhamChiTiet);

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


    @Query(value = """
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
        SELECT STRING_AGG(m.url_media, ', ')
        FROM media_danh_gia m
        WHERE m.id_danh_gia = dg.id_danh_gia AND m.loai_media = 'IMAGE'
    ) AS anh_url,
    (
        SELECT STRING_AGG(m.url_media, ', ')
        FROM media_danh_gia m
        WHERE m.id_danh_gia = dg.id_danh_gia AND m.loai_media = 'VIDEO'
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
""",
            nativeQuery = true)
    Page<Object[]> findAllDanhGia(Pageable pageable);

    @Query(value = "SELECT url_media FROM media_danh_gia WHERE id_danh_gia = :id AND loai_media = 'IMAGE'", nativeQuery = true)
    List<String> findAnhByDanhGiaId(@Param("id") Integer id);

    @Query(value = "SELECT url_media FROM media_danh_gia WHERE id_danh_gia = :id AND loai_media = 'VIDEO'", nativeQuery = true)
    List<String> findVideoByDanhGiaId(@Param("id") Integer id);


}

