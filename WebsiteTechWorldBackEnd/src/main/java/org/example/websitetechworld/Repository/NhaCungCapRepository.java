package org.example.websitetechworld.Repository;

import jakarta.validation.constraints.Size;
import org.example.websitetechworld.Entity.NhaCungCap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NhaCungCapRepository extends JpaRepository<NhaCungCap, Integer> {
    boolean existsByTenNhaCungCap(String tenNhaCungCap);
    boolean existsByTenNhaCungCapAndIdNot(String tenNhaCungCap, Integer id);
    Page<NhaCungCap> findByTenNhaCungCapContainingIgnoreCaseOrEmailContainingIgnoreCase(@Size(max = 50) String tenNhaCungCap, @Size(max = 50) String email, Pageable pageable);

    boolean existsByEmailAndIdNot(String email, Integer id);
    boolean existsBySdtAndIdNot(String sdt, Integer id);

    boolean existsByEmail(String email);
    boolean existsBySdt(String sdt);

}