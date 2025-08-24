package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManHinhQuickCreateAdminRequest {

    private Integer id;

    @NotBlank(message = "Kích thước không được để trống")
    @Size(max = 10, message = "Kích thước tối đa 10 ký tự")
    private String kichThuoc;

    @NotBlank(message = "Tên màn hình không được để trống")
    @Size(max = 50, message = "Tên màn hình tối đa 50 ký tự")
    private String tenManHinh;

    @NotBlank(message = "Loại màn hình không được để trống")
    @Size(max = 10, message = "Loại màn hình tối đa 10 ký tự")
    private String loaiManHinh;
}
