package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse.XuLyChiTietResponse;
import org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse.ActionBeforeCaseResponse;
import org.example.websitetechworld.Entity.XuLySauBanHang;
import org.example.websitetechworld.Enum.ActionAfterCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XuLySauBanHangRepository extends JpaRepository<XuLySauBanHang, Integer> {
    XuLySauBanHang findByIdHoaDon_Id(Integer id);

    @Query(value = """
        SELECT new org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse.ActionBeforeCaseResponse(
            xlbh.id
             , hd.maHoaDon
             , kh.tenKhachHang
             , kh.sdt
             , hd.trangThaiDonHang
             , MAX(xlbh.thoiGianYeuCau)
             , SUM(cthd.donGia)
             , hd.id
        )
                FROM
                     XuLySauBanHang xlbh
                JOIN xlbh.idLyDo ldxl
                 JOIN xlbh.idHoaDon hd
                 JOIN hd.idKhachHang kh
                 JOIN hd.chiTietHoaDons cthd
                WHERE ldxl.loaiVuViec <> 'CANCELLED'
                GROUP BY hd.id, hd.maHoaDon, kh.tenKhachHang, kh.sdt, hd.trangThaiDonHang, xlbh.id
                ORDER BY MAX(xlbh.thoiGianYeuCau)  ASC
    """)
    Page<ActionBeforeCaseResponse> getAllLyDoXuLy(Pageable pageable);

    List<XuLySauBanHang> findXuLySauBanHangByIdHoaDon_Id(Integer idHoaDon);

    @Query(value = """
        SELECT new org.example.websitetechworld.Dto.Response.CommonResponse.AfterBeforeCaseResponse.XuLyChiTietResponse(
            xlbh.id,
            xlbh.idHoaDon.id,
            hd.maHoaDon,
            hd.idKhachHang.tenKhachHang,
            imdb.soImei,
            xlbh.hanhDongSauVuViec,
            cthd.tenSanPham,
            spct.idRom.dungLuong,
            spct.idMau.tenMau,
            CAST(0 AS bigdecimal)
        )
            FROM XuLySauBanHang xlbh
                LEFT JOIN xlbh.idHoaDon hd
                LEFT JOIN xlbh.idImeiDaBan imdb
                LEFT JOIN xlbh.idLyDo ld
                LEFT JOIN hd.chiTietHoaDons cthd
                LEFT JOIN cthd.idSanPhamChiTiet spct
            WHERE xlbh.idHoaDon.id = :idHoaDon
    """)
    List<XuLyChiTietResponse> getAllCtXuLy(Integer idHoaDon);

    XuLySauBanHang findXuLySauBanHangByIdImeiDaBan_SoImei(String soImei);

    List<XuLySauBanHang> findXuLySauBanHangByIdHoaDon_IdAndHanhDongSauVuViec(Integer idHoaDon, ActionAfterCase action);

}
