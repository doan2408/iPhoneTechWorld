package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang, Integer> {
    GioHang findByIdKhachHangId(Integer customerId);
}
