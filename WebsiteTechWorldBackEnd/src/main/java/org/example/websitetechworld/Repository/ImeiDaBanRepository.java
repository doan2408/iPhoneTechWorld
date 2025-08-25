package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ImeiDaBangAdminResponse;
import org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ImeiTrangHoaDonResponse;
import org.example.websitetechworld.Entity.ImeiDaBan;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiBaoHanh;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
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
        LEFT JOIN HoaDon hd ON hd.id = cthd.idHoaDon.id
        LEFT JOIN SanPhamChiTiet  spct ON spct.id = cthd.idSanPhamChiTiet.id
        LEFT JOIN MauSac ms ON ms.id = spct.idMau.id
        LEFT JOIN Rom rom ON rom.id = spct.idRom.id
        LEFT JOIN XuLySauBanHang xlbh ON xlbh.idImeiDaBan.id = imbd.id
        WHERE cthd.id = :ctHoaDonId AND ( xlbh.idHoaDon.id IS NULL OR xlbh.hanhDongSauVuViec = 'CANCEL')
        ORDER BY imbd.id DESC
    """)
    List<ImeiTrangHoaDonResponse> imeiTrongHdct(Integer ctHoaDonId);

    boolean existsBySoImeiAndTrangThai(String soImei, TrangThaiImei trangThaiImei);

    ImeiDaBan findByIdHoaDonChiTiet_IdAndSoImei(Integer idHoaDonChiTiet_Id, String soImei);

    @Query(value = """
    select new org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon.ImeiDaBangAdminResponse (
    imdb.id,
    imdb.soImei,
    imdb.trangThai,
    mdsp.tenModel,
    xx.maXuatXu
    )
    from ImeiDaBan imdb
    join imdb.idHoaDonChiTiet.idSanPhamChiTiet.idSanPham.idModelSanPham mdsp 
    join mdsp.idXuatXu xx 
    where imdb.idHoaDonChiTiet.idHoaDon.idKhachHang.id = :idKhachHang 
    and imdb.trangThai != :trangThaiImeiDaBan 
    order by imdb.id desc
""")
    List<ImeiDaBangAdminResponse> imeiDaBanListByKhachHang(@Param("idKhachHang") Integer idKhachHang,
                                                           @Param("trangThaiImeiDaBan") TrangThaiImei trangThaiImeiDaBan);

    @Query(value = """
    select md.id_model_san_pham from model_san_pham md
    join xuat_xu xx on xx.id_xuat_xu = md.id_xuat_xu
    join san_pham sp on sp.id_model_san_pham = md.id_model_san_pham
    join san_pham_chi_tiet spct on spct.id_san_pham = sp.id_san_pham
    join chi_tiet_hoa_don cthd on cthd.id_san_pham_chi_tiet = spct.id_san_pham_chi_tiet
    join imei_da_ban iDaBan on iDaBan.id_chi_tiet_hoa_don = cthd.id_chi_tiet_hoa_don
    where iDaBan.id_imei_da_ban = ?1
""", nativeQuery = true)
    Integer idModelByidImeiDaBan(Integer idImeiDaBan);

    ImeiDaBan findBySoImei(String soImei);
           
    @Query(value = "select i from ImeiDaBan i " +
            "where i.idHoaDonChiTiet.idHoaDon.idKhachHang.id = :idKhachHang " +
            "order by i.id desc")
    List<ImeiDaBan> imeiDaBanListByKhachHang(@Param("idKhachHang") Integer idKhachHang);

    @Query("""
        SELECT DISTINCT imdb
        FROM ImeiDaBan imdb
        LEFT JOIN FETCH imdb.idBaoHanh bh
        WHERE imdb.trangThai <> 'RETURNED'
          AND imdb.soImei = :soImei
    """)
    ImeiDaBan findBySoImeiWithValidWarranty(String soImei);
}
