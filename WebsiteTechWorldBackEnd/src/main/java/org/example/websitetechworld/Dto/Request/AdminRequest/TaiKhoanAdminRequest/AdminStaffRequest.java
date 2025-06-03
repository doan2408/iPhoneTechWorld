package org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.NhanVien.NhanVienChucVu;
import org.example.websitetechworld.Enum.NhanVien.NhanVienTrangThai;

import java.time.LocalDate;
import java.time.Period;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminStaffRequest {
    private Integer id;

    private String maNhanVien;

    @NotBlank(message = "Tên nhân viên không được để trống")
    private String tenNhanVien;

    @NotBlank(message = "Tài khoản không được để trống")
    private String taiKhoan;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 20, message = "Mật khẩu phải từ 6 đến 20 ký tự")
    private String matKhau;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "\\d{10}", message = "Số điện thoại phải đúng 10 chữ số")
    private String sdt;

    @NotBlank(message = "Địa chỉ không được để trống")
    private String diaChi;

    @NotNull(message = "Trạng thái không được để trống")
    private NhanVienTrangThai trangThai;

    @NotNull(message = "Chức vụ không được để trống")
    private NhanVienChucVu chucVu;

    @NotNull(message = "Giới tính không được để trống")
    private Boolean gioiTinh;

    @NotNull(message = "Năm sinh không được để trống")
    @Past(message = "Năm sinh phải trước ngày hiện tại")
    private LocalDate namSinh;

//    @AssertTrue(message = "Tuổi khách hàng phải từ 18 đến 65")
//    public boolean isTuoiHopLe() {
//        if (namSinh == null) return false;
//        int age = Period.between(namSinh, LocalDate.now()).getYears();
//        return age >= 18 && age <= 65;
//    }
}
