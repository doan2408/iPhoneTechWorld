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
        return response;
    }
}
