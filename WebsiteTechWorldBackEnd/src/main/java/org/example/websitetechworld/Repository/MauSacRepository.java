package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MauSacRepository extends JpaRepository<MauSac, Integer> {
    boolean existsByTenMau(String tenMau);
    boolean existsByTenMauAndIdNot(String tenMau, Integer id);
    Page<MauSac> findByTenMauContainingIgnoreCase(String tenMau, Pageable pageable);
}