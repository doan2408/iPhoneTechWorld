package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.ManHinh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ManHinhRepository extends JpaRepository<ManHinh, Integer> {
    @Query("SELECT mh FROM ManHinh mh WHERE " +
            "LOWER(mh.tenManHinh) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<ManHinh> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
