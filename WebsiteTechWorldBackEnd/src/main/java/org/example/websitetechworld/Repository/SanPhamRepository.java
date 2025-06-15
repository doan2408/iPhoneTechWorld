package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamHienThiAdminResponse;
import org.example.websitetechworld.Entity.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    @Query("""
            select new org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamHienThiAdminResponse(
                sp.id,
                sp.maSanPham,
                sp.tenSanPham,
                loai.tenLoai,
                spct.soLuong,
                sp.trangThaiSanPham,
                spct.giaBan,
                hinhAnh.url
            ) from SanPhamChiTiet spct
            join spct.idLoai loai
            join spct.idSanPham sp
            join spct.hinhAnhs hinhAnh
            group by 
                sp.id,
                sp.maSanPham,
                sp.tenSanPham,
                loai.tenLoai,
                spct.soLuong,
                sp.trangThaiSanPham,
                spct.giaBan,
                hinhAnh.url
            """)
    Page<SanPhamHienThiAdminResponse> getAllHienThi(Pageable pageable);
}
