package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.HinhAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HinhAnhRepository extends JpaRepository<HinhAnh, Integer> {
    List<HinhAnh> findByIdSanPhamChiTiet_Id(Integer idSanPhamChiTiet);
}
