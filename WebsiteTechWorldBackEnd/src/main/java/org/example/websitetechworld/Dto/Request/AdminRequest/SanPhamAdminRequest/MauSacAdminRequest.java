package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MauSacAdminRequest {
    private Integer id;

    @NotBlank(message = "Tên màu không được để trống")
    @Size(max = 50, message = "Tên màu không được vượt quá 50 ký tự")
    private String tenMau;

    @NotBlank(message = "Mã màu không được để trống")
    @Size(max = 7, message = "Tên màu không được vượt quá 7 ký tự")
    private String maMau;
}
