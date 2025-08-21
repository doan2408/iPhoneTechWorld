package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.ChiTietHoaDon;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Enum.HoaDon.TrangThaiThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon,Integer> {
    List<ChiTietHoaDon> findByIdHoaDon_Id(Integer idHoaDon);
    ChiTietHoaDon findByIdHoaDon_IdAndIdSanPhamChiTiet_Id(Integer idHoaDon, Integer idSanPhamChiTiet);

    boolean existsByIdHoaDon(HoaDon idHoaDon);

    @Query("SELECT hdct FROM ChiTietHoaDon hdct JOIN hdct.imeiDaBans i WHERE i.soImei = :soImei")
    ChiTietHoaDon findByImei(@Param("soImei") String soImei);
}
