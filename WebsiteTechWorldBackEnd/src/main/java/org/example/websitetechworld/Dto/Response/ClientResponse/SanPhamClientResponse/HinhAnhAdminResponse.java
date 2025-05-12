package org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse;

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
