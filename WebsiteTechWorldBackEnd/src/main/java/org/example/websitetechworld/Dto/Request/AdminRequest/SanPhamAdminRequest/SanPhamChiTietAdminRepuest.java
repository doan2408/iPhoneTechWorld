package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;


import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Entity.HinhAnh;
import org.example.websitetechworld.Entity.Imei;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamChiTietAdminRepuest {
    private Integer id;

    private String maSanPhamChiTiet;

    private Integer idSanPham;

    @NotNull(message = "{chitiet.mau.required}")
    private Integer idMau;

    @NotNull(message = "{chitiet.rom.required}")
    private Integer idRom;

    @NotNull(message = "{chitiet.soLuong.required}")
    @Min(value = 1, message = "{chitiet.soLuong.min}")
    private Integer soLuong;

    @NotNull(message = "{chitiet.giaBan.required}")
    @DecimalMin(value = "1000.0", inclusive = true, message = "{chitiet.giaBan.min}")
    @Digits(integer = 10, fraction = 2, message = "{chitiet.giaBan.format}")
    private BigDecimal giaBan;

    @NotNull(message = "{chitiet.hinhAnh.required}")
    @Size(min = 1, message = "{chitiet.hinhAnh.min}")
    @Valid
    private Set<HinhAnhAdminRequest> hinhAnhs;

    @NotNull(message = "{chitiet.imei.required}")
    @Size(min = 1, message = "{chitiet.imei.min}")
    @Valid
    private Set<ImeiAdminRequest> imeis;

}
