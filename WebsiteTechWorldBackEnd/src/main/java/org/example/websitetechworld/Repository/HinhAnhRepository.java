package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.HinhAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface HinhAnhRepository extends JpaRepository<HinhAnh, Integer> {
    List<HinhAnh> findByIdSanPhamChiTiet_Id(Integer idSanPhamChiTiet);

    @Transactional
    @Modifying
    @Query("DELETE FROM HinhAnh ha WHERE ha.idSanPhamChiTiet.id = :idSanPhamChiTiet")
    void deleteByIdSanPhamChiTiet(Integer idSanPhamChiTiet);


}
