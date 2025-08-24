package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RomQuickAdminRequest {

    private Integer id;

    @NotBlank(message = "Dung lượng không được để trống")
    @Size(max = 50, message = "Dung lượng không được vượt quá 50 ký tự")
    private String dungLuong;

}
