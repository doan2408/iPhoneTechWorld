package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.KhachHangGiamGia;
import org.example.websitetechworld.Entity.PhieuGiamGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KhachHangGiamGiaRepository extends JpaRepository<KhachHangGiamGia, Integer> {

    List<KhachHangGiamGia> findByIdPhieuGiamGiaAndIsUser(PhieuGiamGia idPhieuGiamGia, Boolean isUser);

    @Query("SELECT khgg.idKhachHang.id FROM KhachHangGiamGia khgg WHERE khgg.idPhieuGiamGia = :phieuGiamGia AND khgg.isUser = false")
    List<Integer> findKhachHangIdsByPhieuGiamGiaId(PhieuGiamGia phieuGiamGia);

    void deleteByIdPhieuGiamGiaId(Integer idPhieuGiamGia);

    default Integer getKhachHangId(KhachHangGiamGia khachHangGiamGia) {
        return khachHangGiamGia.getIdKhachHang().getId();
    }
}
