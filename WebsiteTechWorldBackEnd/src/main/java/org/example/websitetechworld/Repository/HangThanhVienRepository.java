package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.HangThanhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HangThanhVienRepository extends JpaRepository<HangThanhVien, Integer> {
}
