package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.BaoHanh;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiBaoHanh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface BaoHanhRepository extends JpaRepository<BaoHanh,Integer> {

    @Query("SELECT bh FROM BaoHanh bh WHERE " +
            "(:search IS NULL OR bh.idKhachHang.maKhachHang LIKE %:search% OR bh.idKhachHang.tenKhachHang LIKE %:search%" +
            "  OR bh.idKhachHang.sdt LIKE %:search%  OR bh.idLoaiBaoHanh.tenLoaiBaoHanh LIKE %:search%) AND " +
            "(:trangThai IS NULL OR bh.trangThaiBaoHanh = :trangThai) AND " +
            "(:ngayBatDau IS NULL OR bh.ngayBatDau >= :ngayBatDau) AND " +
            "(:ngayKetThuc IS NULL OR bh.ngayKetThuc <= :ngayKetThuc)")
    Page<BaoHanh> findAll(
            @Param("search") String search,
            @Param("trangThai") TrangThaiBaoHanh trangThai,
            @Param("ngayBatDau") LocalDate ngayBatDau,
            @Param("ngayKetThuc") LocalDate ngayKetThuc,
            Pageable pageable);
}
