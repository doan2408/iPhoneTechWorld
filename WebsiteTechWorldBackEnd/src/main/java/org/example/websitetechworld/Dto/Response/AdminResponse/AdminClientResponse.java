package org.example.websitetechworld.Dto.Response.AdminResponse;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Entity.DiaChi;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Enum.KhachHang.TrangThaiKhachHang;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminClientResponse {
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

    private String hangKhachHang;

    private String trangThai;

    private AdminDiaChiResponse diaChiChinh;

}
