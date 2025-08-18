package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.LoaiBaoHanh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoaiBaoHanhRepository extends JpaRepository<LoaiBaoHanh,Integer> {

    @Query("SELECT lbh FROM LoaiBaoHanh lbh WHERE :search IS NULL OR lbh.tenLoaiBaoHanh LIKE %:search% ")
    Page<LoaiBaoHanh> findAll(@Param("search") String search, Pageable pageable);
}
