package org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Enum.KhachHang.TrangThaiKhachHang;


import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminClientRequest {
    private Integer id;

    private String maKhachHang;

    @NotBlank(message = "Tên khách hàng không được để trống")
    @Size(max = 100, message = "Tên khách hàng tối đa 100 ký tự")
    private String tenKhachHang;

    @Pattern(regexp = "^$|0\\d{9}", message = "Số điện thoại không hợp lệ")
    private String sdt;

    @Size(min = 5, max = 50, message = "Tài khoản phải từ 5 đến 50 ký tự")
    private String taiKhoan;

    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String matKhau;

    @Pattern(
            regexp = "^$|^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Email không đúng định dạng"
    )
    private String email;


    @Past(message = "Ngày sinh không được sau hiện tại")
    private LocalDate ngaySinh;

    private Boolean gioiTinh; // True: Nam, False: Nữ

    private String anh;

    @DecimalMin(value = "0.0", inclusive = true, message = "Tổng điểm không được âm")
    private BigDecimal tongDiem;

    @DecimalMin(value = "0.0", inclusive = true, message = "Số điểm hiện tại không được âm")
    private BigDecimal soDiemHienTai;

    private HangKhachHang hangKhachHang;

    @NotNull(message = "Trạng thái khách hàng không được để trống")
    private TrangThaiKhachHang trangThai;
}
