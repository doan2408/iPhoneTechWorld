package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CameraSauAdminRequest {
    private Integer id;

    @NotBlank(message = "Loại camera không được để trống")
    private String loaiCamera;

    @NotBlank(message = "Độ phân giải không được để trống")
    private String doPhanGiai;

    @NotBlank(message = "Khẩu độ không được để trống")
    private String khauDo;

    @NotBlank(message = "Loại zoom không được để trống")
    private String loaiZoom;

    @NotBlank(message = "Chế độ chụp không được để trống")
    private String cheDoChup;
}
