package org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.NhanVien.NhanVienChucVu;
import org.example.websitetechworld.Enum.NhanVien.NhanVienTrangThai;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminStaffRequest {
    private Integer id;

    private String maNhanVien;

    private String tenNhanVien;

    private String taiKhoan;

    private String email;

    private String sdt;

    private String diaChi;

    private NhanVienTrangThai trangThai;

    private NhanVienChucVu chucVu;

    private Boolean gioiTinh;

    private LocalDate namSinh;
}
