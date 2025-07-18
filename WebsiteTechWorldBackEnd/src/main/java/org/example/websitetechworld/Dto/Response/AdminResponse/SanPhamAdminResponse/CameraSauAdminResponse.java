package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Entity.ModelCameraSau;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CameraSauAdminResponse {

    private Integer id;

    private String loaiCamera;

    private String doPhanGiai;

    private String khauDo;

    private String loaiZoom;

    private String cheDoChup;

    private Boolean isChinh;
}
