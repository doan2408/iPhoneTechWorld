package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.HeDieuHanh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeDieuHanhRepository extends JpaRepository<HeDieuHanh, Integer> {
}
