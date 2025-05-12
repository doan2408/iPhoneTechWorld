package org.example.websitetechworld.Dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class HinhAnhAdminResponse {

    private Integer id;

    private Integer idSanPhamChiTiet;

    private String url;

    private String imagePublicId;
}
