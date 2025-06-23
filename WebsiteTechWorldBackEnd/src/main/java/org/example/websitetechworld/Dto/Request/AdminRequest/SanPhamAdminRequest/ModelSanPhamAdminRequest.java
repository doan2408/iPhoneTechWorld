package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelSanPhamAdminRequest {

    private Integer id_model_san_pham;

    private String ma_model_san_pham;

    private String ten_model;

    private Date nam_ra_mat;

    private String mo_ta;

    private String trang_thai;

    private Integer idSanPham;

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
