package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Entity.KhachHang;
import org.example.websitetechworld.Entity.NhanVien;
import org.example.websitetechworld.Entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Integer> {

    // Tìm token theo chuỗi token và loại (access/refresh)
    Optional<UserToken> findByTokenAndTokenType(String token, String tokenType);

    // Tìm token refresh của 1 nhân viên
    List<UserToken> findByIdNhanVienAndTokenType(NhanVien idNhanVien, String tokenType);

    // Tìm token refresh của 1 khách hàng
    List<UserToken> findByIdKhachHangAndTokenType(KhachHang idKhachHang, String tokenType);

    // xoá token cũ nếu cần:
    void deleteByToken(String token);
}
