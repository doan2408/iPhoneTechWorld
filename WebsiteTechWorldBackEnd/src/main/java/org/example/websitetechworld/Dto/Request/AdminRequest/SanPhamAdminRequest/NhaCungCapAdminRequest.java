package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NhaCungCapAdminRequest {
    private Integer id;

    @NotBlank(message = "Tên nhà cung cấp không được để trống")
    @Size(max = 100, message = "Tên nhà cung cấp không được vượt quá 100 ký tự")
    private String tenNhaCungCap;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 200, message = "Địa chỉ không được vượt quá 200 ký tự")
    private String diaChi;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Size(max = 10, message = "Số điện thoại không được vượt quá 10 ký tự")
    private String sdt;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email không được vượt quá 100 ký tự")
    private String email;
}
