package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.CameraTruoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CameraTruocRepository extends JpaRepository<CameraTruoc, Integer> {
}
