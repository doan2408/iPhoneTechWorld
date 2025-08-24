package org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Entity.BaoHanh;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaoHanhAdminResponse {
    private Integer idBaoHanh;

    private Integer idKhachHang;
    private String maKhachHang;
    private String sdtKhachHang;
    private String tenKhachHang;

    private Integer idImeiDaBan;
    private String soImeiDaBan;

    private Integer idLoaiBaoHanh;
    private String tenLoaiBaoHanh;
    private Integer thoiGianThang;

    private Date ngayBatDau;

    private Date ngayKetThuc;

    private String trangThaiBaoHanh;


    public static BaoHanhAdminResponse convertDto(BaoHanh baoHanh) {
        BaoHanhAdminResponse baoHanhAdminResponse = new BaoHanhAdminResponse();
        baoHanhAdminResponse.setIdBaoHanh(baoHanh.getId());
        if (baoHanh.getIdKhachHang() != null) {
            baoHanhAdminResponse.setIdKhachHang(baoHanh.getIdKhachHang().getId());
            baoHanhAdminResponse.setMaKhachHang(baoHanh.getIdKhachHang().getMaKhachHang());
            baoHanhAdminResponse.setTenKhachHang(baoHanh.getIdKhachHang().getTenKhachHang());
            baoHanhAdminResponse.setSdtKhachHang(baoHanh.getIdKhachHang().getSdt());
        }

        if (baoHanh.getIdLoaiBaoHanh() != null) {
            baoHanhAdminResponse.setIdLoaiBaoHanh(baoHanh.getIdLoaiBaoHanh().getId());
            baoHanhAdminResponse.setTenLoaiBaoHanh(baoHanh.getIdLoaiBaoHanh().getTenLoaiBaoHanh());
            baoHanhAdminResponse.setThoiGianThang(baoHanh.getIdLoaiBaoHanh().getThoiGianThang());
        }

        if(baoHanh.getIdImeiDaBan() != null) {
            baoHanhAdminResponse.setIdImeiDaBan(baoHanh.getIdImeiDaBan().getId());
            baoHanhAdminResponse.setSoImeiDaBan(baoHanh.getIdImeiDaBan().getSoImei());
        }
        baoHanhAdminResponse.setNgayBatDau(baoHanh.getNgayBatDau());
        baoHanhAdminResponse.setNgayKetThuc(baoHanh.getNgayKetThuc());
        baoHanhAdminResponse.setTrangThaiBaoHanh(baoHanh.getTrangThaiBaoHanh() != null ? baoHanh.getTrangThaiBaoHanh().name() : null);

        return baoHanhAdminResponse;
    }
}
