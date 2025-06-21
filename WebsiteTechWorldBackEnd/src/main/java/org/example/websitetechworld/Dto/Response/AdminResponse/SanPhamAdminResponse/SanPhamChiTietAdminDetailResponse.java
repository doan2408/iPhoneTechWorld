package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamChiTietAdminDetailResponse {
    private Integer id;
    private String maSanPhamChiTiet;
    private Integer idMau;
    private Integer idRam;
    private Integer idRom;
    private Integer idManHinh;
    private Integer idHeDieuHanh;
    private Integer idPin;
    private Integer idCpu;
    private Integer idCameraTruoc;
    private Integer idCameraSau;
    private Integer idXuatXu;
    private Integer idLoai;
    private Integer soLuong;
    private BigDecimal giaBan;
    private String imeisInput;
    private Set<ImeiAdminResponse> imeis;
    private Set<HinhAnhAdminResponse> hinhAnhs;
}
