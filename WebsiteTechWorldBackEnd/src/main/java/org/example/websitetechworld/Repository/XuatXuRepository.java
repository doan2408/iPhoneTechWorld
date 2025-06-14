package org.example.websitetechworld.Repository;

import jakarta.validation.constraints.Size;
import org.example.websitetechworld.Entity.XuatXu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XuatXuRepository extends JpaRepository<XuatXu, Integer> {
    boolean existsByMaXuatXu(String maXuatXu);
    boolean existsByMaXuatXuAndIdNot(String maXuatXu, Integer id);
    Page<XuatXu> findByMaXuatXuContainingIgnoreCaseOrTenQuocGiaContainingIgnoreCase(String maXuatXu, String tenQuocGia, Pageable pageable);
}