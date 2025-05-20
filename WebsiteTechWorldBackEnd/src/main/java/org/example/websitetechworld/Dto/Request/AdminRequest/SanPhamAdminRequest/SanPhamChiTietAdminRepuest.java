package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamChiTietAdminRepuest {

    private Integer id;

    private String maSanPhamChiTiet;

    private Integer idSanPham;

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
}
