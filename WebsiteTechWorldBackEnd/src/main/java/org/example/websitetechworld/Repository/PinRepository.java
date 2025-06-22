package org.example.websitetechworld.Repository;

import jakarta.validation.constraints.Size;
import org.example.websitetechworld.Entity.Pin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinRepository extends JpaRepository<Pin, Integer> {
    boolean existsByPhienBan(String phienBan);
    boolean existsByPhienBanAndIdNot(String phienBan, Integer id);
    Page<Pin> findByPhienBanContainingIgnoreCaseOrCongSuatSacContainingIgnoreCase(@Size(max = 50) String phienBan, @Size(max = 50) String congSuatSac, Pageable pageable);
}