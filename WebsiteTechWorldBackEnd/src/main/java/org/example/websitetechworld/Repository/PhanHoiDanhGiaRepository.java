package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.PhanHoiDanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PhanHoiDanhGiaRepository extends JpaRepository<PhanHoiDanhGia, Integer> {
//    List<PhanHoiDanhGia> findByIdDanhGia(Integer idDanhGia);

    Optional<PhanHoiDanhGia> findByDanhGiaSanPham_IdDanhGia(Integer idDanhGia);

    @Query("SELECT p FROM PhanHoiDanhGia p WHERE p.danhGiaSanPham.idDanhGia = :idDanhGia")
    List<PhanHoiDanhGia> findListByIdDanhGia(@Param("idDanhGia") Integer idDanhGia);
}
