package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.CommonResponse.ActionBeforeCaseResponse;
import org.example.websitetechworld.Entity.XuLySauBanHang;
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
        SELECT new org.example.websitetechworld.Dto.Response.CommonResponse.ActionBeforeCaseResponse(
            xlbh.id
             , hd.maHoaDon
             , kh.tenKhachHang
             , kh.sdt
             , hd.trangThaiDonHang
             , MAX(xlbh.thoiGianYeuCau)
             , SUM(cthd.donGia)
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
}
