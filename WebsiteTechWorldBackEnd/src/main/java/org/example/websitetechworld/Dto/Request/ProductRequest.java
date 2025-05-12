package org.example.websitetechworld.Dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Entity.SanPham;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private Integer id;

    private String maSanPham;

    private String tenSanPham;

    private String thuongHieu;

    private Integer soLuongTonKho;

    private String tenNhaCungCap;

    private String url;

}
