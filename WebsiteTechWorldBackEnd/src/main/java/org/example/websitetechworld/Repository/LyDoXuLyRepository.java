package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.LyDoXuLy;
import org.example.websitetechworld.Enum.CaseReason.CaseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LyDoXuLyRepository extends JpaRepository<LyDoXuLy,Integer> {
    LyDoXuLy findByLoaiVuViec(CaseType caseType);
}
