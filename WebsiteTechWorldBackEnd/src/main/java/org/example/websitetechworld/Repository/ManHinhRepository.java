package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.ManHinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManHinhRepository extends JpaRepository<ManHinh, Integer> {
}
