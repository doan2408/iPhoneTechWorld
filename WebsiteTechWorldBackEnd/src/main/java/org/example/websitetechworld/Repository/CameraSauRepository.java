package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse.CameraSauResponse;
import org.example.websitetechworld.Entity.CameraSau;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CameraSauRepository extends JpaRepository<CameraSau, Integer> {

    @Query("SELECT c FROM CameraSau c WHERE " +
            "LOWER(c.loaiCamera) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.doPhanGiai) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.khauDo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.loaiZoom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.cheDoChup) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<CameraSau> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(c) FROM CameraSau c WHERE " +
            "c.loaiCamera = :loaiCamera AND " +
            "c.doPhanGiai = :doPhanGiai AND " +
            "c.khauDo = :khauDo AND " +
            "c.loaiZoom = :loaiZoom AND " +
            "c.cheDoChup = :cheDoChup")
    Integer countCheckTrung(
            @Param("loaiCamera") String loaiCamera,
            @Param("doPhanGiai") String doPhanGiai,
            @Param("khauDo") String khauDo,
            @Param("loaiZoom") String loaiZoom,
            @Param("cheDoChup") String cheDoChup
    );
    @Query(value = """
        SELECT cs.do_phan_giai AS doPhanGiai,
             cs.loai_zoom AS loaiZoom,
             cs.loai_camera AS loaiCamera,
             cs.khau_do AS khauDo,
             cs.che_do_chup AS cheDoChup
            FROM model_san_pham mdsp
            INNER JOIN san_pham sp
                ON mdsp.id_model_san_pham = sp.id_model_san_pham
            INNER JOIN model_camera_sau mdcs
                ON mdsp.id_model_san_pham = mdcs.id_model_san_pham
            INNER JOIN camera_sau cs
                ON cs.id_camera_sau = mdcs.id_camera_sau
        WHERE sp.id_san_pham = :idSanPham
    """,nativeQuery = true)
    List<CameraSauResponse> findCameraSauByIdSanPham(@Param("idSanPham") Integer idSanPham);

    Optional<CameraSau> findByLoaiCameraAndDoPhanGiaiAndKhauDo (String loaiCamera, String doPhanGiai, String khauDo);

    Boolean existsByLoaiCameraAndDoPhanGiaiAndKhauDo (String loaiCamera, String doPhanGiai, String khauDo);

    Boolean existsByLoaiCameraAndDoPhanGiaiAndKhauDoAndIdNot (String loaiCamera, String doPhanGiai, String khauDo, Integer integer);
}
