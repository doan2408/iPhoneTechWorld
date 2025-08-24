package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.BaoHanh;
import org.example.websitetechworld.Enum.BaoHanh.TrangThaiBaoHanh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BaoHanhRepository extends JpaRepository<BaoHanh,Integer> {

    @Query("SELECT bh FROM BaoHanh bh WHERE " +
            "(:search IS NULL OR bh.idKhachHang.maKhachHang LIKE %:search% OR bh.idKhachHang.tenKhachHang LIKE %:search% " +
            "or bh.idKhachHang.sdt like %:search% or bh.idImeiDaBan.soImei like %:search%" +
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

    @Query(value = "select case when count(bh) > 0 then true else false end " +
            "from BaoHanh bh " +
            "where bh.idImeiDaBan.id = :idImeiDaBan " +
            "and bh.idLoaiBaoHanh.id = :idLoaiBaoHanh")
    boolean existsByBaoHanh(@Param("idImeiDaBan") Integer idImeiDaBan,
                            @Param("idLoaiBaoHanh") Integer idLoaiBaoHanh);

    @Query(value = "select bh from BaoHanh bh " +
            "where bh.trangThaiBaoHanh = :trangThaiBaoHanh")
    List<BaoHanh> baoHanhByStatus(TrangThaiBaoHanh trangThaiBaoHanh);
}
