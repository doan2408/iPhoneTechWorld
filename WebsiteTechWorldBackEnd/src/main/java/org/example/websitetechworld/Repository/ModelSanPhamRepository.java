package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.ModelSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelSanPhamRepository extends JpaRepository<ModelSanPham, Integer> {
}
