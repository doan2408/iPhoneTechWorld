package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.Ram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RamRepository extends JpaRepository<Ram, Integer> {
}
