package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HinhAnhAdminRequest {
    private Integer id;

    private Integer idSanPhamChiTiet;

    private String url;

    private String imagePublicId;

}
