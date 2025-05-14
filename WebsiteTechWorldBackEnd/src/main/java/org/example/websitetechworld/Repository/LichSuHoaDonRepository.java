package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.LichSuHoaDon;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LichSuHoaDonRepository extends JpaRepository<LichSuHoaDon,Integer> {
    List<LichSuHoaDon> findByIdHoaDon_Id(Integer idHoaDon, Pageable pageable);
}
