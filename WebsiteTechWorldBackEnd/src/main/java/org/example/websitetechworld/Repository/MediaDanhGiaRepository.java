package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.MediaDanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaDanhGiaRepository extends JpaRepository<MediaDanhGia, Integer> {
    List<MediaDanhGia> findByDanhGiaSanPham_IdDanhGia(Integer id);

    @Modifying
    @Query("DELETE FROM MediaDanhGia m WHERE m.danhGiaSanPham.idDanhGia = :idDanhGia")
    void deleteByDanhGiaSanPham_IdDanhGia(@Param("idDanhGia") Integer idDanhGia);

    @Modifying
    @Query("DELETE FROM MediaDanhGia m WHERE m.idMedia = :idMedia")
    void deleteByIdMedia(@Param("idMedia") Integer idMedia);

}
