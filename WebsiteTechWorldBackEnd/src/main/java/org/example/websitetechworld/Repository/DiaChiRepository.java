package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminDiaChiResponse;
import org.example.websitetechworld.Entity.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi, Integer> {
    @Query("select new org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminDiaChiResponse(" +
            "dc.id," +
            "dc.idKhachHang.tenKhachHang," +
            "dc.tenNguoiNhan," +
            "dc.sdtNguoiNhan," +
            "dc.soNha," +
            "dc.tenDuong," +
            "dc.xaPhuong," +
            "dc.quanHuyen," +
            "dc.tinhThanhPho," +
            "dc.diaChiChinh," +
            "dc.idKhachHang.id) " +
            "from DiaChi dc where dc.idKhachHang.id = :idKhachHang " +
            "order by dc.diaChiChinh desc ")
    List<AdminDiaChiResponse> getAllDiaChi(int idKhachHang);

    @Query("select new org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminDiaChiResponse(" +
            "dc.id," +
            "dc.idKhachHang.tenKhachHang," +
            "dc.tenNguoiNhan," +
            "dc.sdtNguoiNhan," +
            "dc.soNha," +
            "dc.tenDuong," +
            "dc.xaPhuong," +
            "dc.quanHuyen," +
            "dc.tinhThanhPho," +
            "dc.diaChiChinh," +
            "dc.idKhachHang.id) " +
            "from DiaChi dc where dc.id = :idDiaChi")
    AdminDiaChiResponse getDiaChi(int idDiaChi);

    @Modifying
    @Query("UPDATE DiaChi d SET d.diaChiChinh = false WHERE d.idKhachHang.id = :idKhachHang AND (:idDiaChi IS NULL OR d.id <> :idDiaChi)")
    void updateAllDiaChiPhu(@Param("idKhachHang") int idKhachHang, @Param("idDiaChi") Integer idDiaChi);

    @Query("SELECT d FROM DiaChi d WHERE d.idKhachHang.id = :clientId")
    List<DiaChi> findByIdKhachHang(@Param("clientId") Integer clientId);

    @Query("SELECT d FROM DiaChi d WHERE d.idKhachHang.id = :idKhachHang AND d.diaChiChinh = :diaChiChinh")
    List<DiaChi> findByIdKhachHangAndDiaChiChinh(@Param("idKhachHang") int idKhachHang, @Param("diaChiChinh") boolean diaChiChinh);


}
