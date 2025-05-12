package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.Loai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoaiRepository extends JpaRepository<Loai, Integer> {
}
