package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.CaseReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseReasonRepository extends JpaRepository<CaseReason,Integer> {
}
