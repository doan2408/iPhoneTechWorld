package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XuatXuQuickCreateAdminRequest {

    private Integer id;

    @NotBlank(message = "Mã xuất xứ không được để trống")
    @Size(max = 50, message = "Mã xuất xứ không được vượt quá 50 ký tự")
    private String maXuatXu;
}
