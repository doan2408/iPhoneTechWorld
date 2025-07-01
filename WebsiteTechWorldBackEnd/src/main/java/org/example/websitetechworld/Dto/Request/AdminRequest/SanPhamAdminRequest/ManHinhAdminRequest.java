package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManHinhAdminRequest {
    private Integer id;

    @NotBlank(message = "Tên màn hình không được để trống")
    private String tenManHinh;

    @NotBlank(message = "Kích thước không được để trống")
    private String kichThuoc;

    @NotBlank(message = "Loại màn hình không được để trống")
    private String loaiManHinh;

    @NotBlank(message = "Độ phân giải không được để trống")
    private String doPhanGiai;

    @NotBlank(message = "Tần số quét không được để trống")
    private String tanSoQuet;

    @NotBlank(message = "Độ sáng không được để trống")
    private String doSang;

    @NotBlank(message = "Chất liệu kính không được để trống")
    private String chatLieuKinh;
}
