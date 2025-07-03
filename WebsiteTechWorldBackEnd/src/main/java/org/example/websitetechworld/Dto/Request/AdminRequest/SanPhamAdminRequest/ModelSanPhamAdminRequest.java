package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPhamModel;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelSanPhamAdminRequest {

    private Integer idModelSanPham;

    private String modelSanPham;

    private String tenModel;

    private LocalDate namRaMat;

    @Enumerated(EnumType.STRING)
    private TrangThaiSanPhamModel trangThaiSanPhamModel;

    private Integer idRam;

    private Integer idManHinh;

    private Integer idHeDieuHanh;

    private Integer idPin;

    private Integer idCpu;

    private Integer idCameraTruoc;

    private Integer idCameraSau;

    private Integer idXuatXu;

    private Integer idLoai;
}
