package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Entity.NhanVien;
import org.example.websitetechworld.Enum.KhachHang.TrangThaiKhachHang;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang,Integer> {
    Optional<KhachHang> findByTaiKhoan(String taiKhoan);
    Optional<KhachHang> findByEmail(String email);

    // KhachHangRepository
    boolean existsByEmail(String email);
    boolean existsBySdt(String sdt);
    boolean existsByTaiKhoan(String taiKhoan);

    boolean existsByTaiKhoanAndIdNot(String taiKhoan, Integer id);
    boolean existsByEmailAndIdNot(String email, Integer id);
    boolean existsBySdtAndIdNot(String sdt, Integer id);

    @Modifying
    @Query("UPDATE KhachHang k SET k.matKhau = :password WHERE k.email = :email")
    int updatePasswordByEmail(@Param("email") String email, @Param("password") String hashedPassword);

    // Tìm kiếm theo tên, email, số điện thoại
//    @Query("SELECT nv FROM KhachHang nv WHERE " +
//            "LOWER(nv.tenKhachHang) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(nv.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
//            "LOWER(nv.sdt) LIKE LOWER(CONCAT('%', :keyword, '%'))")
//    Page<KhachHang> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
                SELECT kh FROM KhachHang kh
                WHERE (:gioiTinh IS NULL OR kh.gioiTinh = :gioiTinh)
                AND (:trangThai IS NULL OR kh.trangThai = :trangThai)
                AND
                (
                :keyword IS NULL OR
                    LOWER(kh.tenKhachHang) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(kh.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(kh.sdt) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
            """)
    Page<KhachHang> findByFilters(
            @Param("keyword") String keyword,
            @Param("gioiTinh") Boolean gioiTinh,
            @Param("trangThai") TrangThaiKhachHang trangThai,
            Pageable pageable
    );


    @Query("SELECT k FROM KhachHang k WHERE NOT EXISTS (SELECT h FROM HoaDon h WHERE h.idKhachHang = k)")
    List<KhachHang> findNewCustomers();

    @Query("SELECT k FROM KhachHang k WHERE EXISTS (SELECT h.id FROM HoaDon h WHERE h.idKhachHang = k)")
    List<KhachHang> findOldCustomers();

    @Query("SELECT k FROM KhachHang k WHERE LOWER(k.tenKhachHang) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<KhachHang> findByTenKhachHangContainingIgnoreCase(@RequestParam("search") String search, Pageable pageable);
}
