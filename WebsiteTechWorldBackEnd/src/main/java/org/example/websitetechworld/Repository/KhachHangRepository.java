package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang,Integer> {
    Optional<KhachHang> findByTaiKhoan(String taiKhoan);
}
