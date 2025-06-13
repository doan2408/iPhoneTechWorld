package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.NhanVien;
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

    // Tìm kiếm theo tên, email, số điện thoại, chức vụ
    @Query("SELECT nv FROM NhanVien nv WHERE " +
            "LOWER(nv.tenNhanVien) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(nv.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(nv.sdt) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(nv.chucVu) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(nv.taiKhoan) LIKE LOWER(CONCAT('%', :keyword, '%'))" )
    Page<NhanVien> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}
