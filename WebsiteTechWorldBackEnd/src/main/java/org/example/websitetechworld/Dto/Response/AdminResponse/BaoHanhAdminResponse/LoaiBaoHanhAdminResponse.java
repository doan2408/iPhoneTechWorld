package org.example.websitetechworld.Dto.Response.AdminResponse.BaoHanhAdminResponse;

import lombok.*;
import org.example.websitetechworld.Entity.LoaiBaoHanh;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoaiBaoHanhAdminResponse {

    private Integer id;
    private String tenLoaiBaoHanh;
    private Integer idModelSanPham;
    private String tenModelSanPham;
    private String maXuatXu;
    private Integer thoiGianThang;
    private String moTa;
    private Boolean trangThai;
    private Boolean daCo;

    public static LoaiBaoHanhAdminResponse convertDto(LoaiBaoHanh loaiBaoHanh) {
        LoaiBaoHanhAdminResponse response = new LoaiBaoHanhAdminResponse();
        response.setId(loaiBaoHanh.getId());
        response.setIdModelSanPham(loaiBaoHanh.getIdModelSanPham().getIdModelSanPham());
        response.setTenModelSanPham(loaiBaoHanh.getIdModelSanPham().getTenModel());

        if (loaiBaoHanh.getIdModelSanPham().getIdXuatXu() != null) {
            response.setMaXuatXu(loaiBaoHanh.getIdModelSanPham().getIdXuatXu().getMaXuatXu());
        } else {
            response.setMaXuatXu("Chưa có xuất xứ");
        }

        response.setTenLoaiBaoHanh(loaiBaoHanh.getTenLoaiBaoHanh());
        response.setThoiGianThang(loaiBaoHanh.getThoiGianThang());
        response.setMoTa(loaiBaoHanh.getMoTa());
        response.setTrangThai(loaiBaoHanh.getTrangThai());
        return response;
    }

    public static LoaiBaoHanhAdminResponse convertDto(LoaiBaoHanh loaiBaoHanh, Boolean daCo) {
        LoaiBaoHanhAdminResponse response = new LoaiBaoHanhAdminResponse();
        response.setId(loaiBaoHanh.getId());
        response.setIdModelSanPham(loaiBaoHanh.getIdModelSanPham().getIdModelSanPham());
        response.setTenModelSanPham(loaiBaoHanh.getIdModelSanPham().getTenModel());

        if (loaiBaoHanh.getIdModelSanPham().getIdXuatXu() != null) {
            response.setMaXuatXu(loaiBaoHanh.getIdModelSanPham().getIdXuatXu().getMaXuatXu());
        } else {
            response.setMaXuatXu("Chưa có xuất xứ");
        }

        response.setTenLoaiBaoHanh(loaiBaoHanh.getTenLoaiBaoHanh());
        response.setThoiGianThang(loaiBaoHanh.getThoiGianThang());
        response.setMoTa(loaiBaoHanh.getMoTa());
        response.setTrangThai(loaiBaoHanh.getTrangThai());
        response.setDaCo(daCo);
        return response;
    }
}
