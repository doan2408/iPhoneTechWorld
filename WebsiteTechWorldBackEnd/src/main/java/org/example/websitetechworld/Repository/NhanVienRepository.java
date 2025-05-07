package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.NhanVien;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    NhanVien findByTaiKhoan(String taiKhoan);
}
