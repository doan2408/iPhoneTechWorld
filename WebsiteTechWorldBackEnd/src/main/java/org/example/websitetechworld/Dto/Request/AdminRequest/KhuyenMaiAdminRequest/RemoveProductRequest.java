package org.example.websitetechworld.Dto.Request.AdminRequest.KhuyenMaiAdminRequest;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RemoveProductRequest {

    private Integer sanPhamChiTietId;
    private Integer khuyenMaiId;
}
