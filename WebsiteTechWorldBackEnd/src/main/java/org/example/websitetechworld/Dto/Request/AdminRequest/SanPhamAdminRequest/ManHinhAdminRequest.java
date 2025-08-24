package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManHinhAdminRequest {
    private Integer id;

    @NotBlank(message = "Tên màn hình không được để trống")
    @Size(max = 100, message = "Tên màn hình không được vượt quá 100 ký tự")
    private String tenManHinh;

    @NotBlank(message = "Kích thước không được để trống")
    @Size(max = 50, message = "Kích thước không được vượt quá 50 ký tự")
    private String kichThuoc;

    @NotBlank(message = "Loại màn hình không được để trống")
    @Size(max = 50, message = "Loại màn hình không được vượt quá 50 ký tự")
    private String loaiManHinh;

    @NotBlank(message = "Độ phân giải không được để trống")
    @Size(max = 50, message = "Độ phân giải không được vượt quá 50 ký tự")
    private String doPhanGiai;

    @NotBlank(message = "Tần số quét không được để trống")
    @Size(max = 50, message = "Tần số quét không được vượt quá 50 ký tự")
    private String tanSoQuet;

    @NotBlank(message = "Độ sáng không được để trống")
    @Size(max = 50, message = "Độ sáng không được vượt quá 50 ký tự")
    private String doSang;

    @NotBlank(message = "Chất liệu kính không được để trống")
    @Size(max = 100, message = "Chất liệu kính không được vượt quá 100 ký tự")
    private String chatLieuKinh;

}
