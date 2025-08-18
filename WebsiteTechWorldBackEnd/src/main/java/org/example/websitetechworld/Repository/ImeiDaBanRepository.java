package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ImeiTrangHoaDonResponse;
import org.example.websitetechworld.Entity.Imei;
import org.example.websitetechworld.Entity.ImeiDaBan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImeiDaBanRepository extends JpaRepository<ImeiDaBan, Integer> {

    List<ImeiDaBan> findByIdHoaDonChiTiet_Id(Integer idHoaDonChiTiet_Id);

    List<ImeiDaBan> findByIdHoaDonChiTiet_IdAndSoImeiIn(Integer hdctId, List<String> soImeis);

    @Query(value = """
        SELECT new
        org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ImeiTrangHoaDonResponse(
            imbd.id,
            imbd.soImei,
            imbd.trangThai,
            spct.giaBan,
            cthd.id,
            spct.id,
            cthd.tenSanPham,
            rom.dungLuong,
            ms.tenMau
        )
        FROM ImeiDaBan imbd 
        LEFT JOIN ChiTietHoaDon  cthd ON cthd.id = imbd.idHoaDonChiTiet.id
        LEFT JOIN SanPhamChiTiet  spct ON spct.id = cthd.idSanPhamChiTiet.id
        LEFT JOIN MauSac ms ON ms.id = spct.idMau.id
        LEFT JOIN Rom rom ON rom.id = spct.idRom.id
        WHERE cthd.idHoaDon.id = :hoaDonId
        ORDER BY imbd.id DESC 
    """)
    Page<ImeiTrangHoaDonResponse> imeiTrongHdct(Integer hoaDonId, Pageable pageable);

    @Query(value = """
        SELECT new
        org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ImeiTrangHoaDonResponse(
            imbd.id,
            imbd.soImei,
            imbd.trangThai,
            spct.giaBan,
            cthd.id,
            spct.id,
            cthd.tenSanPham,
            rom.dungLuong,
            ms.tenMau
        )
        FROM ImeiDaBan imbd 
        LEFT JOIN ChiTietHoaDon  cthd ON cthd.id = imbd.idHoaDonChiTiet.id
        LEFT JOIN SanPhamChiTiet  spct ON spct.id = cthd.idSanPhamChiTiet.id
        LEFT JOIN MauSac ms ON ms.id = spct.idMau.id
        LEFT JOIN Rom rom ON rom.id = spct.idRom.id
        WHERE cthd.idHoaDon.id = :hoaDonId
        ORDER BY imbd.id DESC 
    """)
    List<ImeiTrangHoaDonResponse> imeiTrongHdct(Integer hoaDonId);

    boolean existsBySoImei(String soImei);

    ImeiDaBan findByIdHoaDonChiTiet_IdAndSoImei(Integer idHoaDonChiTiet_Id, String soImei);
}
