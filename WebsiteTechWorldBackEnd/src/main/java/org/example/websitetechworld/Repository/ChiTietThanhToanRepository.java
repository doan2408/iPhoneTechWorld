package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.ChiTietThanhToan;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChiTietThanhToanRepository extends JpaRepository<ChiTietThanhToan, Integer> {
    List<ChiTietThanhToan> findByIdHoaDon_Id(Integer idHoaDon, Pageable pageable);

    List<ChiTietThanhToan> findByIdHoaDon_Id(Integer idHoaDon);

}
