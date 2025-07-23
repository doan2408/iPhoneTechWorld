package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.HangThanhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HangThanhVienRepository extends JpaRepository<HangThanhVien, Integer> {
    @Query(value = "select h.tenHang from HangThanhVien h " +
            "join h.khachHangs kh " +
            "where kh.id = :idKhacHang")
    String tenHangThanhVien(Integer idKhacHang);

    @Query(value = "from HangThanhVien ")
    List<HangThanhVien> getListHangThanhVien();
}
