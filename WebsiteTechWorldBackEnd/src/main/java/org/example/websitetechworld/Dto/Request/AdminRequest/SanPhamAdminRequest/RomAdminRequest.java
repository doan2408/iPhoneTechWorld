package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class RomAdminRequest {
    private Integer id;

    private String dungLuong;

    private String loai;

    private String tocDoDocGhi;

    private String nhaSanXuat;

    private LocalDate namRaMat;
}
