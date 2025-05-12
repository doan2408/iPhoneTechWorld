package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.Rom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RomRepository extends JpaRepository<Rom, Integer> {
}
