package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse.LoaiClientResponse;
import org.example.websitetechworld.Entity.Loai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoaiRepository extends JpaRepository<Loai, Integer> {
    boolean existsByTenLoai(String tenLoai);
    boolean existsByTenLoaiAndIdNot(String tenLoai, Integer id);
    Page<Loai> findByTenLoaiContainingIgnoreCase(String tenLoai, Pageable pageable);

    @Query("""
    select new org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse.LoaiClientResponse (
    l.id,
    l.tenLoai
    )
    from Loai l
    """)
    List<LoaiClientResponse> getLoaiClientResponse();
}