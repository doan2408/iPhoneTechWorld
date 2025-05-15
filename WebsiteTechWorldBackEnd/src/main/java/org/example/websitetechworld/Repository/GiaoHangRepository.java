package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.GiaoHang;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiaoHangRepository extends JpaRepository<GiaoHang, Integer> {
    List<GiaoHang> findByIdHoaDon_Id(Integer idHoaDon, Pageable pageable);
}
