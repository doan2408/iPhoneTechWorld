package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CameraTruocAdminRequest {

    private Integer id;

    @NotBlank(message = "Loại camera không được để trống")
    @Size(max = 100, message = "Loại camera không được vượt quá 100 ký tự")
    private String loaiCamera;

    @NotBlank(message = "Độ phân giải không được để trống")
    @Size(max = 50, message = "Độ phân giải không được vượt quá 50 ký tự")
    private String doPhanGiai;

    @NotBlank(message = "Khẩu độ không được để trống")
    @Size(max = 20, message = "Khẩu độ không được vượt quá 20 ký tự")
    private String khauDo;

    @NotBlank(message = "Loại zoom không được để trống")
    @Size(max = 50, message = "Loại zoom không được vượt quá 50 ký tự")
    private String loaiZoom;

    @NotBlank(message = "Chế độ chụp không được để trống")
    @Size(max = 100, message = "Chế độ chụp không được vượt quá 100 ký tự")
    private String cheDoChup;

}
