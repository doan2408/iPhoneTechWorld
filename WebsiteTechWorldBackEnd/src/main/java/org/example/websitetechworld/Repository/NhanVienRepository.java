package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.NhanVien;
import org.example.websitetechworld.Enum.NhanVien.NhanVienChucVu;
import org.example.websitetechworld.Enum.NhanVien.NhanVienTrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {
    Optional<NhanVien> findByTaiKhoan(String taiKhoan);

    Optional<NhanVien> findByEmail(String email);

    // NhanVienRepository
    boolean existsByEmail(String email);

    boolean existsBySdt(String sdt);

    boolean existsByTaiKhoan(String taiKhoan);

    boolean existsByTaiKhoanAndIdNot(String taiKhoan, Integer id);

    boolean existsByEmailAndIdNot(String email, Integer id);

    boolean existsBySdtAndIdNot(String sdt, Integer id);

    @Modifying
    @Query("UPDATE NhanVien n SET n.matKhau = :password WHERE n.email = :email")
    int updatePasswordByEmail(@Param("email") String email, @Param("password") String hashedPassword);

    //get nhan vien not include admin role
    @Query("select nv from NhanVien nv where nv.chucVu != :chucVu")
    Page<NhanVien> findByNotChucVu(@Param("chucVu") NhanVienChucVu chucVu, Pageable pageable);

    //lọc nâng cao
    @Query("""
                SELECT nv FROM NhanVien nv
                WHERE nv.chucVu != :loaiTruChucVu
                AND (:gioiTinh IS NULL OR nv.gioiTinh = :gioiTinh)
                AND (:trangThai IS NULL OR nv.trangThai = :trangThai)
                AND (
                    :keyword IS NULL OR
                    LOWER(nv.tenNhanVien) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(nv.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(nv.sdt) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                    LOWER(nv.taiKhoan) LIKE LOWER(CONCAT('%', :keyword, '%'))
                )
            """)
    Page<NhanVien> searchAdvanced(
            @Param("loaiTruChucVu") NhanVienChucVu loaiTruChucVu,
            @Param("gioiTinh") Boolean gioiTinh,
            @Param("trangThai") NhanVienTrangThai trangThai,
            @Param("keyword") String keyword,
            Pageable pageable
    );


}
