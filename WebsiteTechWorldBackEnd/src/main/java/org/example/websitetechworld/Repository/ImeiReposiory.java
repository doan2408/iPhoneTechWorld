package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.Imei;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImeiReposiory extends JpaRepository<Imei, Integer> {
}
