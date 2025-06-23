package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.Imei;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public interface ImeiReposiory extends JpaRepository<Imei, Integer> {
    @Query(value = "SELECT TOP (:limit) * FROM imei WHERE id_san_pham_chi_tiet = :idSpct AND trang_thai_imei = :trangThai", nativeQuery = true)
    List<Imei> findTopByIdSanPhamChiTietAndTrangThaiImei(@Param("idSpct") Integer idSpct, @Param("trangThai") String trangThai, @Param("limit") int limit);

    Imei findBySoImei(String soImei);
    List<Imei> findAllBySoImeiIn(List<String> soImeis);
    List<Imei> findBySoImeiIn(List<String> soImeis);

    @Query("SELECT imei FROM Imei imei WHERE " +
            "LOWER(imei.soImei) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(imei.soImei2) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(imei.trangThaiImei) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Imei> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(i) FROM Imei i WHERE COALESCE(i.soImei, '') = COALESCE(:soImei, '')")
    Integer countSoImei(@Param("soImei") String soImei);

    @Query("SELECT COUNT(i) FROM Imei i WHERE COALESCE(i.soImei2, '') = COALESCE(:soImei2, '')")
    Integer countSoImei2(@Param("soImei2") String soImei2);

    @Transactional
    @Modifying
    @Query("DELETE FROM Imei imei WHERE imei.idSanPhamChiTiet = :sanPhamChiTiet")
    void deleteByIdSanPhamChiTiet(SanPhamChiTiet sanPhamChiTiet);


    boolean existsBySoImei(String soImei);

    Page<Imei> findByIdSanPhamChiTiet_IdAndTrangThaiImei(Integer idSanPhamChiTiet, TrangThaiImei trangThaiImei, Pageable pageable);

}
