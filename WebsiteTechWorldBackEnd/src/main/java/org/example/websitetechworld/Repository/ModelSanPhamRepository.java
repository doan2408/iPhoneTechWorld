package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.ModelSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelSanPhamRepository extends JpaRepository<ModelSanPham, Integer> {
    @Query(value = """
    SELECT
        msp.id_model_san_pham AS idModelSanPham,
        msp.ma_model_san_pham AS maModelSanPham,
        msp.ten_model AS tenModel,
        msp.id_loai AS idLoai,
        msp.id_ram AS idRam,
        msp.id_xuat_xu AS idXuatXu,
        msp.nam_ra_mat AS namRaMat,
        msp.trang_thai AS trangThai
    FROM model_san_pham msp
        JOIN ram AS r ON r.id_ram = msp.id_ram
        JOIN xuat_xu xx ON xx.id_xuat_xu = msp.id_xuat_xu
        JOIN loai l ON l.id_loai = msp.id_loai
""", nativeQuery = true)
    Page<Object[]> getAllPage(Pageable pageable);
}
