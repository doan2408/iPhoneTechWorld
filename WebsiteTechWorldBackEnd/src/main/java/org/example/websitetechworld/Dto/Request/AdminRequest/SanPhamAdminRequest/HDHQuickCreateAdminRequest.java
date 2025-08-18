package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HDHQuickCreateAdminRequest {
    private Integer id;

    @NotBlank(message = "Phiên bản không được để trống")
    @Size(max = 50, message = "Phiên bản không được vượt quá 50 ký tự")
    private String phienBan;

}
