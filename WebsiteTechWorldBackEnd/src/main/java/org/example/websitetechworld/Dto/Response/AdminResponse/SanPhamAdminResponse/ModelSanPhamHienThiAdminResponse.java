package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.SanPham.TrangThaiSanPhamModel;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelSanPhamHienThiAdminResponse {

    private Integer idModelSanPham;

    private String maModelSanPham;

    private String tenModel;

    private Integer idLoai;

    private Integer idRam;

    private Integer idXuatXu;

    private LocalDate namRaMat;

    @Enumerated
    private TrangThaiSanPhamModel trangThaiSanPhamModel;

}
