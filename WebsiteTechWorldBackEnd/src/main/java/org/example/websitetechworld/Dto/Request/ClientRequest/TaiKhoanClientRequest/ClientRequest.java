package org.example.websitetechworld.Dto.Request.ClientRequest.TaiKhoanClientRequest;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.CheckValidation.CreateGroups;
import org.example.websitetechworld.CheckValidation.UpdateGroups;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Enum.KhachHang.TrangThaiKhachHang;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientRequest {
    private Integer id;

    private String maKhachHang;

    @NotBlank(message = "Họ tên không được để trống")
    @Size(max = 100, message = "Tên tối đa 100 ký tự")
    private String tenKhachHang;

    @Pattern(regexp = "0\\d{9}", message = "Số điện thoại không hợp lệ")
    private String sdt;

    @NotBlank(message = "Tài khoản không được để trống")
    @Size(min = 5, max = 50, message = "Tài khoản phải từ 5 đến 50 ký tự")
    private String taiKhoan;

    @NotBlank(message = "Mật khẩu không được để trống", groups = CreateGroups.class)
    @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String matKhau;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Email không đúng định dạng"
    )
    private String email;


    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    private LocalDate ngaySinh;

    private Boolean gioiTinh; // True: Nam, False: Nữ

    private String anh; // Nếu có thể để trống

    @DecimalMin(value = "0.0", inclusive = true, message = "Tổng điểm không được âm")
    private BigDecimal tongDiem;

    @DecimalMin(value = "0.0", inclusive = true, message = "Số điểm hiện tại không được âm")
    private BigDecimal soDiemHienTai;

    private HangKhachHang hangKhachHang;

    private TrangThaiKhachHang trangThai;
}
