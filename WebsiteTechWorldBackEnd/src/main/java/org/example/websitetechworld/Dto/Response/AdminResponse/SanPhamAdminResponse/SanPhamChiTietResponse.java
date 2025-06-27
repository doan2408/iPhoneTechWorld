package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SanPhamChiTietResponse {

    private Integer id;
    private String maSanPhamChiTiet;
    private Integer soLuongSPCT;
    private BigDecimal giaBan;

    private String tenMau;
    private String maMau;

    private String dungLuongRom;
    private String loaiRom;
    private String tocDoDocGhiRom;
    private String nhaSanXuatRom;
    private LocalDate namRaMatRom;

    private Set<ImeiAdminResponse> imeis;

    private Set<HinhAnhAdminResponse> hinhAnhs;

}
