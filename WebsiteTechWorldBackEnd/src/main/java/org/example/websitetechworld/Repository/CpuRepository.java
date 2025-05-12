package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.Cpu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CpuRepository extends JpaRepository<Cpu, Integer> {
}
