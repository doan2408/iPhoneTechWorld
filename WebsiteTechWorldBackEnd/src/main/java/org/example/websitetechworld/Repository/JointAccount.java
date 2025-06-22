package org.example.websitetechworld.Repository;

import org.example.websitetechworld.Enum.NhanVien.NhanVienTrangThai;

public interface JointAccount {
     Integer getId();
     String tai_khoan();
     String mat_khau();
     String getRole();
     String getFullName();
     String getEmail();
     String getSdt();
     String getTrangThai();
}
