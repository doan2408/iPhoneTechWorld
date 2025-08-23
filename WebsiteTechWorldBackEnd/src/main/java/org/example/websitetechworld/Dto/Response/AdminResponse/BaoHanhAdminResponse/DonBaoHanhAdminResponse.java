package org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DonBaoHanhAdminResponse {
    private Integer idLsbh;
    private String soImei;
    private String tenSanPham;
    private String mau;
    private String rom;
    private String loaiBaoHanh;
    private String tenKhachHang;
    private String ngayTiepNhan;
    private String trangThai;
}
