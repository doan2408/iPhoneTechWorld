package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelSanPhamAdminRequest {

    private Integer idModelSanPham;

    private String tenModel;

    private LocalDate namRaMat;

    private String moTa;

    private String trangThai;

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
