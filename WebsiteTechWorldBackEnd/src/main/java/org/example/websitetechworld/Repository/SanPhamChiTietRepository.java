package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamChiTietResponse;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {
    @Query("SELECT s FROM SanPhamChiTiet s " +
            "LEFT JOIN FETCH s.idSanPham " +
            "LEFT JOIN FETCH s.idMau " +
            "LEFT JOIN FETCH s.idRam " +
            "LEFT JOIN FETCH s.idRom " +
            "LEFT JOIN FETCH s.idManHinh " +
            "LEFT JOIN FETCH s.idHeDieuHanh " +
            "LEFT JOIN FETCH s.idPin " +
            "LEFT JOIN FETCH s.idCpu " +
            "LEFT JOIN FETCH s.idCameraTruoc " +
            "LEFT JOIN FETCH s.idCameraSau " +
            "LEFT JOIN FETCH s.idXuatXu " +
            "LEFT JOIN FETCH s.idLoai " +
            "WHERE s.id = :id")
    Optional<SanPhamChiTiet> findFullById(@Param("id") Integer id);

    Page<SanPhamChiTiet> findByIdSanPham_TenSanPhamContainingAndIdSanPham_TrangThaiSanPham(String tenSanPham,TrangThaiSanPham trangThaiSanPham, Pageable pageable);

    Page<SanPhamChiTiet> findByIdSanPham_TrangThaiSanPham(TrangThaiSanPham trangThaiSanPham, Pageable pageable);
//    @Query("""
//        select new org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse(
//            sp.maSanPham,
//
//        ) from SanPhamChiTiet spct
//        join spct.idSanPham sp
//""")

    @Modifying
    @Transactional
    @Query("UPDATE SanPhamChiTiet spct SET spct.soLuong = spct.soLuong - :soLuongGiam WHERE spct.id = :idSanPhamChiTiet AND spct.soLuong >= :soLuongGiam")
    int giamSoLuongTon(@Param("idSanPhamChiTiet") Integer idSanPhamChiTiet, @Param("soLuongGiam") int soLuongGiam);

    @Modifying
    @Transactional
    @Query("UPDATE SanPhamChiTiet spct SET spct.soLuong = spct.soLuong + :soLuongTang WHERE spct.id = :idSanPhamChiTiet")
    int tangSoLuongTon(@Param("idSanPhamChiTiet") Integer idSanPhamChiTiet, @Param("soLuongTang") int soLuongTang);


}
