package org.example.websitetechworld.Dto.Response.ClientResponse.TaiKhoanResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Dto.Response.AdminResponse.TaiKhoanAdminResponse.AdminDiaChiResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoanClientReponse {
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

}
