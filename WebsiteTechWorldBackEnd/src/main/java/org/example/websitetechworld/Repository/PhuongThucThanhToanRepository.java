package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.PhuongThucThanhToan;
import org.example.websitetechworld.Enum.HoaDon.TenHinhThuc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhuongThucThanhToanRepository extends JpaRepository<PhuongThucThanhToan,Integer> {
    PhuongThucThanhToan findOneByTenPhuongThuc(TenHinhThuc tenHinhThuc);

    List<PhuongThucThanhToan> findByLoaiHinhThuc(String loaiHinhThuc);
}
