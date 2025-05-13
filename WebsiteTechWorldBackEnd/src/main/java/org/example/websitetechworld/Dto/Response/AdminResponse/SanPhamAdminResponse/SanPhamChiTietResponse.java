package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SanPhamChiTietResponse {

    private Integer id;
    private String maSanPhamChiTiet;
    private String tenSanPham;
    private String tenMau;
    private String dungLuongRam;
    private String dungLuongRom;
    private String kichThuocManHinh;
    private String heDieuHanh;
    private String loaiSanPham;
    private String pin;
    private String cpu;
    private String cameraTruoc;
    private String cameraSau;
    private String xuatXu;

    private Integer soLuong;
    private BigDecimal giaBan;

    private List<String> hinhAnhUrls;
    private List<ImeiAdminResponse> imeiList;
    private List<BaoHanhAdminResponse> baoHanhs;

}
