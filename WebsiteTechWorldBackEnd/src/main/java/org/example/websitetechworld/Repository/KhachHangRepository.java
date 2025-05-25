package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang,Integer> {
    Optional<KhachHang> findByTaiKhoan(String taiKhoan);
    Optional<KhachHang> findByEmail(String email);

    // KhachHangRepository
    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE KhachHang k SET k.matKhau = :password WHERE k.email = :email")
    int updatePasswordByEmail(@Param("email") String email, @Param("password") String hashedPassword);

}
