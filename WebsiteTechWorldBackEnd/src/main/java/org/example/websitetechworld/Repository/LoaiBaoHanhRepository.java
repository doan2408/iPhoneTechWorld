package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.LoaiBaoHanh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoaiBaoHanhRepository extends JpaRepository<LoaiBaoHanh,Integer> {

    @Query("SELECT lbh FROM LoaiBaoHanh lbh WHERE :search IS NULL OR lbh.tenLoaiBaoHanh LIKE %:search% ")
    Page<LoaiBaoHanh> findAll(@Param("search") String search, Pageable pageable);

    boolean existsByTenLoaiBaoHanh(String tenLoaiBaoHanh);

    @Query("select case when count(l) > 0 then true else false end " +
            "from LoaiBaoHanh l " +
            "where l.tenLoaiBaoHanh = :tenLoai " +
            "and l.idModelSanPham.idModelSanPham = :idModel")
    boolean existsByTenLoaiBaoHanhAndIdModel(@Param("tenLoai") String tenLoaiBaoHanh, @Param("idModel") Integer idModel);

    @Query(value = "select lbh from LoaiBaoHanh lbh " +
            "where lbh.trangThai = :trangThai " +
            "and lbh.idModelSanPham.idModelSanPham = :idModel")
    List<LoaiBaoHanh> findByIdModelAndTrangThai(@Param("trangThai") Boolean trangThai, @Param("idModel") Integer idModel);
}
