package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.HangThanhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface HangThanhVienRepository extends JpaRepository<HangThanhVien, Integer> {
    @Query(value = "select h.tenHang from HangThanhVien h " +
            "join h.khachHangs kh " +
            "where kh.id = :idKhacHang")
    String tenHangThanhVien(Integer idKhacHang);

    @Query(value = "from HangThanhVien ")
    List<HangThanhVien> getListHangThanhVien();

    // tong diem xet hang
    @Query(value = "select sum(ls.so_diem) from lich_su_diem ls " +
            "where ls.loai_diem = 'CONG' " +
            "and ls.han_su_dung >= :now " +
            "and ls.id_vi_diem = :idViDiem", nativeQuery = true)
    BigDecimal diemXetHang(Integer idViDiem, OffsetDateTime now);

    // find hang by khoang diem
    @Query(value = "select h from HangThanhVien h " +
            "where :diem between h.diemTu and h.diemDen")
    HangThanhVien getHangThanhVien(@Param("diem") BigDecimal diemHienTai);
}
