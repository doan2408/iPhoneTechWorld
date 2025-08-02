package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HinhAnhAdminRequest {
    private Integer id;

    private Integer idSanPhamChiTiet;

//    @NotBlank(message = "URL hình ảnh không được để trống")
    @Size(max = 255, message = "URL hình ảnh không được vượt quá 255 ký tự")
    private String url;

//    @NotBlank(message = "imagePublicId không được để trống")
//    @Size(max = 255, message = "imagePublicId không được vượt quá 255 ký tự")
    private String imagePublicId;

}
