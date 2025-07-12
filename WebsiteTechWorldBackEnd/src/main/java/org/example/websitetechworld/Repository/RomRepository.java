package org.example.websitetechworld.Repository;

import jakarta.validation.constraints.Size;
import org.example.websitetechworld.Entity.Rom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RomRepository extends JpaRepository<Rom, Integer> {
    boolean existsByDungLuong(String dungLuong);
    boolean existsByDungLuongAndIdNot(String dungLuong, Integer id);
    Page<Rom> findByDungLuongContainingIgnoreCaseOrNhaSanXuatContainingIgnoreCase(@Size(max = 50) String dungLuong, @Size(max = 50) String nhaSanXuat, Pageable pageable);

    @Query("""
    select distinct r.id, r.dungLuong from SanPhamChiTiet spct
    join spct.idRom r
    where spct.idSanPham.id = ?1
""")
    List<Object[]> getRomByIdSanPham(Integer id);
}