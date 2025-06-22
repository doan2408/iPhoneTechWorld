package org.example.websitetechworld.Repository;

import jakarta.validation.constraints.Size;
import org.example.websitetechworld.Entity.Rom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RomRepository extends JpaRepository<Rom, Integer> {
    boolean existsByDungLuong(String dungLuong);
    boolean existsByDungLuongAndIdNot(String dungLuong, Integer id);
    Page<Rom> findByDungLuongContainingIgnoreCaseOrNhaSanXuatContainingIgnoreCase(@Size(max = 50) String dungLuong, @Size(max = 50) String nhaSanXuat, Pageable pageable);
}