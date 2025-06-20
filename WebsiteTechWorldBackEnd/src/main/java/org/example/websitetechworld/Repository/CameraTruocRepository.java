package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.CameraTruoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CameraTruocRepository extends JpaRepository<CameraTruoc, Integer> {

    @Query("SELECT c FROM CameraTruoc c WHERE " +
            "LOWER(c.loaiCamera) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.doPhanGiai) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.khauDo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.loaiZoom) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.cheDoChup) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<CameraTruoc> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(c) FROM CameraTruoc c WHERE " +
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


}
