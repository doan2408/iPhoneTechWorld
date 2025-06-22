package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PinAdminRequest {
    private Integer id;

    @NotBlank(message = "Phiên bản không được để trống")
    @Size(max = 50, message = "Phiên bản không được vượt quá 50 ký tự")
    private String phienBan;

    @Size(max = 50, message = "Công suất sạc không được vượt quá 50 ký tự")
    private String congSuatSac;

    @Size(max = 100, message = "Thời gian sử dụng không được vượt quá 100 ký tự")
    private String thoiGianSuDung;

    @Size(max = 50, message = "Số lần sạc tối đa không được vượt quá 50 ký tự")
    private String soLanSacToiDa;
}
