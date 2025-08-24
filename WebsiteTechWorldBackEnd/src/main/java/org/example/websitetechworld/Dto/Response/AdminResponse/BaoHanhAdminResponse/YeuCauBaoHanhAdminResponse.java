package org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class YeuCauBaoHanhAdminResponse {
    private Integer idImei;
    private String soImei;
    private String tenSanPham;
    private String mau;
    private String dungLuong;
    private LocalDateTime ngayMuaHang;
    private List<BaoHanhOfProductResponse> lstBaoHanh;
}
