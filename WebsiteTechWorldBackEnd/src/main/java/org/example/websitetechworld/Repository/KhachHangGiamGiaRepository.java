package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Entity.KhachHangGiamGia;
import org.example.websitetechworld.Entity.PhieuGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KhachHangGiamGiaRepository extends JpaRepository<KhachHangGiamGia, Integer> {

    @Query("select khgg from KhachHangGiamGia khgg where khgg.idPhieuGiamGia = :idPhieuGiamGia and khgg.isUser = :isUser")
    List<KhachHangGiamGia> findByIdPhieuGiamGiaAndIsUser(
            @Param("idPhieuGiamGia") PhieuGiamGia idPhieuGiamGia,
            @Param("isUser") Boolean isUser
    );


    void deleteByIdPhieuGiamGiaId(Integer idPhieuGiamGia);
    boolean existsByIdPhieuGiamGiaAndIsUser(PhieuGiamGia idPhieuGiamGia, Boolean isUser);
    boolean existsByIdPhieuGiamGiaAndIdKhachHang(PhieuGiamGia idPhieuGiamGia, KhachHang idKhachHang);


    List<KhachHangGiamGia> findByIdKhachHang_Id(Integer idKhachHangId);

    void deleteByIdPhieuGiamGiaAndIsUser(PhieuGiamGia idPhieuGiamGia, Boolean isUser);

    @Query("SELECT CASE " +
            "WHEN COUNT(k) > 0 THEN true " +
            "ELSE false END " +
            "FROM KhachHangGiamGia k " +
            "WHERE k.idKhachHang.id = :idKhachHang " +
            "AND k.idPhieuGiamGia.id = :idPhieuGiamGia " +
            "AND k.trangThai = 1 " +
            "AND MONTH(k.ngayCap) = :thang " +
            "AND YEAR(k.ngayCap) = :nam")
    boolean checkSoLanDoiTrong1Thang(
            @Param("idKhachHang") Integer idKhachHang,
            @Param("idPhieuGiamGia") Integer idPhieuGiamGia,
            @Param("thang") int thang,
            @Param("nam") int nam
    );

    KhachHangGiamGia findByIdPhieuGiamGiaAndIdKhachHang(PhieuGiamGia idPhieuGiamGia, KhachHang idKhachHang);

    KhachHangGiamGia findByIdPhieuGiamGiaAndIdKhachHangAndIsUser(PhieuGiamGia idPhieuGiamGia, KhachHang idKhachHang, Boolean isUser);
}
