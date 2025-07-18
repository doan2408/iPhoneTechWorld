package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.ViDiem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ViDiemRepository extends JpaRepository<ViDiem, Integer> {
    @Query(value = "select v from ViDiem v " +
            "where v.khachHang.id = :idKhachHang")
    Optional<ViDiem> findByIdKhachHang(@Param("idKhachHang") Integer idKhachHang);
}
