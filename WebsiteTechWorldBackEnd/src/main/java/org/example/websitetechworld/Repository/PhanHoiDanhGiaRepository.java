package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.PhanHoiDanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhanHoiDanhGiaRepository extends JpaRepository<PhanHoiDanhGia, Integer> {

}
