package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManHinhQuickCreateAdminRequest {

    private Integer id;

    @NotBlank(message = "Kích thước không được để trống")
    private String kichThuoc;

    @NotBlank(message = "Tên màn hình không được để trống")
    private String tenManHinh;

    @NotBlank(message = "Loại màn hình không được để trống")
    private String loaiManHinh;
}
