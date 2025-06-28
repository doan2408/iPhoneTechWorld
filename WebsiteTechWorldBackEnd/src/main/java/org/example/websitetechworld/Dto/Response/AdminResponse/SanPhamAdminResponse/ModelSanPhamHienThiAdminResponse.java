package org.example.websitetechworld.Dto.Response.AdminResponse.SanPhamAdminResponse;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Entity.Loai;
import org.example.websitetechworld.Entity.Ram;
import org.example.websitetechworld.Entity.XuatXu;

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

    private String trangThai;



}
