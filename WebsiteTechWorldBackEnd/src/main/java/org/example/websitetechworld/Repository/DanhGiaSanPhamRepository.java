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
        dg.id_danh_gia AS idDanhGia,
        kh.ten_khach_hang AS tenKhachHang,
        spct.ma_san_pham_chi_tiet AS maSanPhamChiTiet,
        sp.ten_san_pham AS tenSanPham,
        dg.so_sao AS soSao,
        dg.noi_dung AS noiDung,
        dg.ngay_danh_gia AS ngayDanhGia,
        dg.trang_thai_danh_gia AS trangThaiDanhGia
    FROM danh_gia_san_pham dg
    JOIN khach_hang kh ON kh.id_khach_hang = dg.id_khach_hang
    JOIN san_pham_chi_tiet spct ON spct.id_san_pham_chi_tiet = dg.id_san_pham_chi_tiet
    JOIN san_pham sp ON sp.id_san_pham = spct.id_san_pham
    """,
            countQuery = "SELECT COUNT(*) FROM danh_gia_san_pham",
            nativeQuery = true)
    Page<Object[]> findAllDanhGia(Pageable pageable);

    @Query(value = "SELECT url_media FROM media_danh_gia WHERE id_danh_gia = :id AND loai_media = 'IMAGE'", nativeQuery = true)
    List<String> findAnhByDanhGiaId(@Param("id") Integer id);

    @Query(value = "SELECT url_media FROM media_danh_gia WHERE id_danh_gia = :id AND loai_media = 'VIDEO'", nativeQuery = true)
    List<String> findVideoByDanhGiaId(@Param("id") Integer id);


}

