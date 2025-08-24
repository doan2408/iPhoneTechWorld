package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PinQuicKCreateAdminRequest {
    private Integer id;

    @NotBlank(message = "Không được để trống phiên bản pin")
    @Size(max = 30, message = "Phiên bản pin không được quá 30 ký tự")
    private String phienBan;
}
