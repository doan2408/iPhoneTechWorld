package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse.XuLyChiTietResponse;
import org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse.ActionBeforeCaseResponse;
import org.example.websitetechworld.Entity.XuLySauBanHang;
import org.example.websitetechworld.Enum.ActionAfterCase;
import org.example.websitetechworld.Enum.CaseReason.CaseType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface XuLySauBanHangRepository extends JpaRepository<XuLySauBanHang, Integer> {
    XuLySauBanHang findByIdHoaDon_IdAndLoaiVuViec(Integer id, CaseType caseType);

    List<XuLySauBanHang> findByIdHoaDon_IdAndAndIdImeiDaBan_SoImei(Integer id,String soImei);

    @Query(value = """
        SELECT new org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse.ActionBeforeCaseResponse(
            MAX(xlbh.id)
             , hd.maHoaDon
             , kh.tenKhachHang
             , kh.sdt
             , hd.trangThaiDonHang
             , MAX(xlbh.thoiGianYeuCau)
             , SUM(cthd.donGia)
             , hd.id
             , (    SELECT x.hanhDongSauVuViec
                        FROM XuLySauBanHang x
                    WHERE x.idHoaDon = hd
                    ORDER BY x.thoiGianYeuCau DESC LIMIT 1)
        )
                FROM
                     XuLySauBanHang xlbh
                JOIN xlbh.idLyDo ldxl
                 JOIN xlbh.idHoaDon hd
                LEFT JOIN hd.idKhachHang kh
                 JOIN hd.chiTietHoaDons cthd
                WHERE ldxl.loaiVuViec <> 'CANCELLED'
                    AND (:search IS NULL OR hd.maHoaDon LIKE %:search% OR kh.tenKhachHang LIKE %:search%)
                    AND (:status IS NULL OR ldxl.loaiVuViec = :status)
                GROUP BY hd.id, hd.maHoaDon, kh.tenKhachHang, kh.sdt, hd.trangThaiDonHang
                ORDER BY
                    CASE WHEN :sortDir = 'ASC' THEN MAX(xlbh.thoiGianYeuCau) END ASC,
                    CASE WHEN :sortDir = 'DESC' THEN MAX(xlbh.thoiGianYeuCau) END DESC
    """)
    Page<ActionBeforeCaseResponse> getAllLyDoXuLy(@Param("search") String search,
                                                  @Param("status") CaseType status,
                                                  Pageable pageable, @Param("sortDir") String sortDir);

    List<XuLySauBanHang> findXuLySauBanHangByIdHoaDon_Id(Integer idHoaDon);

    @Query(value = """
        SELECT new org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse.XuLyChiTietResponse(
            xlbh.id,
            xlbh.idHoaDon.id,
            hd.maHoaDon,
            kh.tenKhachHang,
            imdb.soImei,
            xlbh.hanhDongSauVuViec,
            cthd.tenSanPham,
            spct.idRom.dungLuong,
            spct.idMau.tenMau,
            ld.tenLyDo,
            xlbh.loaiVuViec,
            cthd.donGia,
            CAST(0 AS bigdecimal),
            xlbh.urlHinh,
            xlbh.urlVideo
        )
            FROM XuLySauBanHang xlbh
                LEFT JOIN xlbh.idImeiDaBan imdb
                LEFT JOIN imdb.idHoaDonChiTiet cthd
                LEFT JOIN xlbh.idLyDo ld
                LEFT JOIN cthd.idHoaDon hd
                LEFT JOIN hd.idKhachHang kh
                LEFT JOIN cthd.idSanPhamChiTiet spct
            WHERE xlbh.idHoaDon.id = :idHoaDon
            GROUP BY
            imdb.soImei, xlbh.id,
            xlbh.idHoaDon.id,
            hd.maHoaDon,
            kh.tenKhachHang,
            xlbh.hanhDongSauVuViec,
            cthd.tenSanPham,
            spct.idRom.dungLuong,
            spct.idMau.tenMau,
            ld.tenLyDo,
            xlbh.loaiVuViec,
            cthd.donGia,
            xlbh.urlHinh,
            xlbh.urlVideo
    """)
    List<XuLyChiTietResponse> getAllCtXuLy(Integer idHoaDon);

    XuLySauBanHang findXuLySauBanHangByIdImeiDaBan_SoImeiAndIdHoaDon_Id(String soImei, Integer idHoaDon);

    List<XuLySauBanHang> findXuLySauBanHangByIdHoaDon_IdAndHanhDongSauVuViec(Integer idHoaDon, ActionAfterCase action);

    @Query(value = """
        SELECT COUNT(DISTINCT xl.id_hoa_don) AS count
        FROM xu_ly_sau_ban_hang xl
        WHERE xl.hanh_dong_sau_vu_viec = :hanhDongSauVuViec;
    """, nativeQuery = true)
    int countByHanhDongSauVuViec(String hanhDongSauVuViec);

    @Query(value = """
        SELECT COUNT(DISTINCT xl.id_hoa_don) AS count
        FROM xu_ly_sau_ban_hang xl
        WHERE xl.loai_vu_viec = :loaiVuViec;
    """, nativeQuery = true)
    int countByLoaiVuViec(String loaiVuViec);

    @Query(value = """
        SELECT COUNT(*) AS count
        FROM (
        SELECT xl.id_hoa_don
                FROM xu_ly_sau_ban_hang xl
                GROUP BY xl.id_hoa_don
                HAVING
                SUM(CASE WHEN xl.thoi_gian_xu_ly IS NULL THEN 1 ELSE 0 END) = 0
        AND SUM(CASE WHEN CAST(xl.thoi_gian_xu_ly AS DATE) = CAST(GETDATE() AS DATE) THEN 1 ELSE 0 END) > 0
        ) AS SUB;
    """, nativeQuery = true)
    int countByThoiGianXuLy();
}
    
    XuLySauBanHang findByIdImeiDaBan_IdAndIdHoaDon_IdAndHanhDongSauVuViec(
            Integer idImei, Integer idHoaDon, ActionAfterCase hanhDongSauVuViec
    );

}
