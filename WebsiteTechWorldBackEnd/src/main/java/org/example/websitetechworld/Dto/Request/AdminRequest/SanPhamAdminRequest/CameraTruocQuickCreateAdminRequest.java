package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CameraTruocQuickCreateAdminRequest {
    private Integer id;

    private String loaiCamera;

    private String doPhanGiai;

    private String khauDo;
}
