package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KhachHangRepository extends JpaRepository<KhachHang,Integer> {
    KhachHang findByTaiKhoan(String taiKhoan);
}
