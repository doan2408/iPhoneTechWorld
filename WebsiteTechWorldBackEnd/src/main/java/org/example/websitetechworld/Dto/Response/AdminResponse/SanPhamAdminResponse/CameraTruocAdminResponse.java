package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CameraTruocAdminResponse {
    private Integer id;

    private String loaiCamera;

    private String doPhanGiai;

    private String khauDo;

    private String loaiZoom;

    private String cheDoChup;

}
