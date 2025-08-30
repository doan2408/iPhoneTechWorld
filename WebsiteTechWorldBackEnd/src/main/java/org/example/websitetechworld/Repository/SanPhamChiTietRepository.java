package org.example.websitetechworld.Repository;

import jakarta.validation.constraints.Size;
import org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse.SanPhamChiTietResponse;
import org.example.websitetechworld.Entity.KhuyenMai;
import org.example.websitetechworld.Entity.SanPham;
import org.example.websitetechworld.Entity.SanPhamChiTiet;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamChiTietRepository extends JpaRepository<SanPhamChiTiet, Integer> {
    @Query("SELECT s FROM SanPhamChiTiet s " +
            "LEFT JOIN FETCH s.idSanPham " +
            "LEFT JOIN FETCH s.idMau " +
//            "LEFT JOIN FETCH s.idRam " +
            "LEFT JOIN FETCH s.idRom " +
//            "LEFT JOIN FETCH s.idManHinh " +
//            "LEFT JOIN FETCH s.idHeDieuHanh " +
//            "LEFT JOIN FETCH s.idPin " +
//            "LEFT JOIN FETCH s.idCpu " +
//            "LEFT JOIN FETCH s.idCameraTruoc " +
//            "LEFT JOIN FETCH s.idCameraSau " +
//            "LEFT JOIN FETCH s.idXuatXu " +
//            "LEFT JOIN FETCH s.idLoai " +
            "WHERE s.id = :id")
    Optional<SanPhamChiTiet> findFullById(@Param("id") Integer id);

    @Query("""
    SELECT spct FROM SanPhamChiTiet spct
        WHERE (:tenSanPham IS NULL OR :tenSanPham = '' OR spct.idSanPham.tenSanPham LIKE %:tenSanPham%)
        AND spct.idSanPham.trangThaiSanPham = :trangThaiSanPham
        AND (:loaiSanPham IS NULL OR spct.idSanPham.idModelSanPham.idLoai.id = :loaiSanPham)
        AND (:giaTu IS NULL OR spct.giaBan >= :giaTu)
        AND (:giaDen IS NULL OR spct.giaBan <= :giaDen)
        AND (:soLuongTu IS NULL OR spct.soLuong >= :soLuongTu)
        AND (:soLuongDen IS NULL OR spct.soLuong <= :soLuongDen)
        AND (:maSpct IS NULL OR :maSpct = '' OR spct.maSanPhamChiTiet LIKE %:maSpct%)
        AND (:dungLuong IS NULL OR spct.idRom.id =  :dungLuong)
        AND (:mau IS NULL OR spct.idMau.id = :mau)
""")
    Page<SanPhamChiTiet> findByTenSanPhamAndTrangThai(
            @Param("tenSanPham") String tenSanPham,
            @Param("trangThaiSanPham") TrangThaiSanPham trangThaiSanPham,
            @Param("loaiSanPham") Integer loaiSanPham,
            @Param("giaTu") BigDecimal giaTu,
            @Param("giaDen") BigDecimal giaDen,
            @Param("soLuongTu") Integer soLuongTu,
            @Param("soLuongDen") Integer soLuongDen,
            @Param("maSpct") String maSpct,
            @Param("dungLuong") Integer dungLuong,
            @Param("mau") Integer mau,
            Pageable pageable
    );


    Page<SanPhamChiTiet> findByIdSanPham_MaSanPhamContainingAndIdSanPham_TrangThaiSanPham(String maSanPham,TrangThaiSanPham trangThaiSanPham, Pageable pageable);

    Page<SanPhamChiTiet> findByIdSanPham_TrangThaiSanPham(TrangThaiSanPham trangThaiSanPham, Pageable pageable);


    @Modifying
    @Transactional
    @Query("UPDATE SanPhamChiTiet spct SET spct.soLuong = spct.soLuong - :soLuongGiam WHERE spct.id = :idSanPhamChiTiet AND spct.soLuong >= :soLuongGiam")
    int giamSoLuongTon(@Param("idSanPhamChiTiet") Integer idSanPhamChiTiet, @Param("soLuongGiam") int soLuongGiam);

    @Modifying
    @Transactional
    @Query("UPDATE SanPhamChiTiet spct SET spct.soLuong = spct.soLuong + :soLuongTang WHERE spct.id = :idSanPhamChiTiet")
    int tangSoLuongTon(@Param("idSanPhamChiTiet") Integer idSanPhamChiTiet, @Param("soLuongTang") int soLuongTang);


    @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
    FROM san_pham_chi_tiet c
    JOIN san_pham sp ON c.id_san_pham = sp.id_san_pham
    JOIN model_san_pham m ON sp.id_model_san_pham = m.id_model_san_pham
    WHERE 
      c.id_san_pham = :idSp
      AND m.id_model_san_pham = :idLoai
      AND c.id_mau = :idMau
      AND c.id_rom = :idRom
""", nativeQuery = true)
    Integer existsVariantInLoai(
            @Param("idSp") Integer idSp,
            @Param("idMau") Integer idMau,
            @Param("idRom") Integer idRom,
            @Param("idLoai") Integer idLoai
    );


    @Query(value = """
    SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
    FROM san_pham_chi_tiet ct
    JOIN san_pham sp ON ct.id_san_pham = sp.id_san_pham
    JOIN model_san_pham m ON sp.id_model_san_pham = m.id_model_san_pham
    WHERE ct.id_mau = :idMau
      AND ct.id_rom = :idRom
      AND m.id_loai = :idLoai
      AND ct.id_san_pham_chi_tiet <> :excludeId
""", nativeQuery = true)
    Integer existsVariantInLoaiExceptId(@Param("idMau") Integer idMau, @Param("idRom") Integer idRom,
                                        @Param("idLoai") Integer idLoai, @Param("excludeId") Integer excludeId);



    List<SanPhamChiTiet> findByIdSanPhamAndIdMau_IdAndIdRom_Id(SanPham sanPham, Integer idMau, Integer idRom);

    // Method để tìm chi tiết sản phẩm theo sanPham, màu sắc và ROM
    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE spct.idSanPham.id = :sanPhamId " +
            "AND (:mauId IS NULL AND spct.idMau IS NULL OR spct.idMau.id = :mauId) " +
            "AND (:romId IS NULL AND spct.idRom IS NULL OR spct.idRom.id = :romId)")
    List<SanPhamChiTiet> findBySanPhamAndMauAndRom(
            @Param("sanPhamId") Integer sanPhamId,
            @Param("mauId") Integer mauId,
            @Param("romId") Integer romId);

    @Query("SELECT spct FROM SanPhamChiTiet spct WHERE spct.idSanPham.id IN :sanPhamIds")
    List<SanPhamChiTiet> findBySanPhamIds(@Param("sanPhamIds") List<Integer> sanPhamIds);

    List<SanPhamChiTiet> findAllByIdSanPham(SanPham idSanPham);

    Integer id(Integer id);

    @Query("""
        SELECT spct.id, spct.soLuong FROM SanPhamChiTiet spct
            WHERE spct.id = :idSanPhamChiTietId
                AND spct.idSanPham.trangThaiSanPham = 'ACTIVE'
    """)
    List<Object[]> checkProductStatus(@Param("idSanPhamChiTietId") Integer idSanPhamChiTietId);
}
