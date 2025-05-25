package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.PhuongThucThanhToan;
import org.example.websitetechworld.Enum.HoaDon.LoaiHinhThuc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhuongThucThanhToanRepository extends JpaRepository<PhuongThucThanhToan,Integer> {
    PhuongThucThanhToan findByLoaiHinhThuc(LoaiHinhThuc laoiHinhThuc);
}
