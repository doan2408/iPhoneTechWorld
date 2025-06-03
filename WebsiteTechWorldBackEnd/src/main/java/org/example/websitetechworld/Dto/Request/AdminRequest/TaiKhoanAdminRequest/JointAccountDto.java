package org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Enum.KhachHang.TrangThaiKhachHang;
import org.example.websitetechworld.Enum.NhanVien.NhanVienChucVu;
import org.example.websitetechworld.Enum.NhanVien.NhanVienTrangThai;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class JointAccountDto {
    private Integer id; // id của bản ghi (để kiểm tra khi update)
    private String ten;
    private String taiKhoan; // tài khoản cần kiểm tra trùng
    private String password;
    private String email;    // email cần kiểm tra trùng
    private String sdt;      // số điện thoại cần kiểm tra trùng
    private NhanVienChucVu role;
    private String diaChi;
    private NhanVienTrangThai trangThaiNhanVien;
    private boolean gioiTinh;
    private String anh;
    private BigDecimal tongDiem;
    private BigDecimal soDiemHienTai;
    private HangKhachHang hangKhachHang;
    private TrangThaiKhachHang trangThaiKhachHang;
    private LocalDate ngaySinh;

}
