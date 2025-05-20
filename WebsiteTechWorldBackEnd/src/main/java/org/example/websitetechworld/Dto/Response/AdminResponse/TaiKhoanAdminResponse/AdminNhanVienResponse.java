package org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.NhanVien.NhanVienChucVu;
import org.example.websitetechworld.Enum.NhanVien.NhanVienTrangThai;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminNhanVienResponse {
    private Integer id;

    private String maNhanVien;

    private String tenNhanVien;

    private String taiKhoan;

    private String matKhau;

    private String email;

    private String sdt;

    private String diaChi;

    private NhanVienTrangThai trangThai;

    private NhanVienChucVu chucVu;

    private Boolean gioiTinh;

    private LocalDate namSinh;
}
