package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.Imei;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImeiReposiory extends JpaRepository<Imei, Integer> {
    @Query(value = "SELECT TOP (:limit) * FROM imei WHERE id_san_pham_chi_tiet = :idSpct AND trang_thai_imei = :trangThai", nativeQuery = true)
    List<Imei> findTopByIdSanPhamChiTietAndTrangThaiImei(@Param("idSpct") Integer idSpct, @Param("trangThai") String trangThai, @Param("limit") int limit);

    Imei findBySoImei(String soImei);
    List<Imei> findAllBySoImeiIn(List<String> soImeis);

}
