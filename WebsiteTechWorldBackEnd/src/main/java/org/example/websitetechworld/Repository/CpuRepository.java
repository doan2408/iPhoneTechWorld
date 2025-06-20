package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.Cpu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CpuRepository extends JpaRepository<Cpu, Integer> {

        @Query("SELECT cpu FROM Cpu cpu WHERE " +
                "LOWER(cpu.hangSanXuat) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(cpu.chipXuLy) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(cpu.soNhan) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(cpu.xungNhip) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(cpu.congNgheSanXuat) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(cpu.boNhoDem) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                "LOWER(cpu.tieuThuDienNang) LIKE LOWER(CONCAT('%', :keyword, '%'))")
        Page<Cpu> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

        @Query("SELECT COUNT(cpu) FROM Cpu cpu WHERE " +
                "COALESCE(cpu.chipXuLy, '') = COALESCE(:chipXuLy, '')")
        Integer countCheckTrung(
                @Param("chipXuLy") String chipXuLy
        );

    }
