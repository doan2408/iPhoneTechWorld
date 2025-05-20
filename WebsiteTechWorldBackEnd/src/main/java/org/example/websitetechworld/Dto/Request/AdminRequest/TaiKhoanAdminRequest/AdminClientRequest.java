package org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminDiaChiResponse;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Enum.KhachHang.TrangThaiKhachHang;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminClientRequest {
    private Integer id;

    private String maKhachHang;

    private String tenKhachHang;

    private String sdt;

    private String taiKhoan;

    private String matKhau;

    private String email;

    private LocalDate ngaySinh;

    private Boolean gioiTinh;

    private String anh;

    private BigDecimal tongDiem;

    private BigDecimal soDiemHienTai;

    private HangKhachHang hangKhachHang;

    private TrangThaiKhachHang trangThai;
}
