package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {
    @Query("SELECT s FROM SanPhamChiTiet s " +
            "LEFT JOIN FETCH s.idSanPham " +
            "LEFT JOIN FETCH s.idMau " +
            "LEFT JOIN FETCH s.idRam " +
            "LEFT JOIN FETCH s.idRom " +
            "LEFT JOIN FETCH s.idManHinh " +
            "LEFT JOIN FETCH s.idHeDieuHanh " +
            "LEFT JOIN FETCH s.idPin " +
            "LEFT JOIN FETCH s.idCpu " +
            "LEFT JOIN FETCH s.idCameraTruoc " +
            "LEFT JOIN FETCH s.idCameraSau " +
            "LEFT JOIN FETCH s.idXuatXu " +
            "LEFT JOIN FETCH s.idLoai " +
            "WHERE s.id = :id")
    Optional<SanPhamChiTiet> findFullById(@Param("id") Integer id);

}
