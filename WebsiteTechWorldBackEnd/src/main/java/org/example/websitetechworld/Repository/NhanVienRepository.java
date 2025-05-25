package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.NhanVien;
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

    @Modifying
    @Query("UPDATE NhanVien n SET n.matKhau = :password WHERE n.email = :email")
    int updatePasswordByEmail(@Param("email") String email, @Param("password") String hashedPassword);

}
