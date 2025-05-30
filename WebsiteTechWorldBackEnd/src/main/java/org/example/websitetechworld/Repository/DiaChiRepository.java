package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminDiaChiResponse;
import org.example.websitetechworld.Entity.DiaChi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
            "dc.diaChiChinh) " +
            "from DiaChi dc where dc.idKhachHang.id = :idKhachHang")
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
            "dc.diaChiChinh) " +
            "from DiaChi dc where dc.id = :idDiaChi")
    AdminDiaChiResponse getDiaChi(int idDiaChi);


}
