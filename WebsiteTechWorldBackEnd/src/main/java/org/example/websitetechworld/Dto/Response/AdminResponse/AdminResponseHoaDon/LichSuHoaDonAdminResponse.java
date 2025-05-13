package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import org.example.websitetechworld.Entity.HoaDon;
import org.example.websitetechworld.Entity.NhanVien;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

public class LichSuHoaDonAdminResponse {
    private Integer idLichSuHoaDon;

    private Integer idNhanVien;
    private String maNhanVien;
    private String tenNhanVien;

    private Integer idHoaDon;
    private String maHoaDon;

    private String hanhDong;

    private LocalDate thoiGianThayDoi;

    private String moTa;
}
