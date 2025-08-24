package org.example.websitetechworld.Dto.Response.AdminResponse.AdminResponseHoaDon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImeiDaBangAdminResponse {
    private Integer id;
    private String soImei;
    private String trangThaiImei;
    private Integer idHoaDonChiTiet;
    private String tenModel;
    private String maXuatXu;

    public ImeiDaBangAdminResponse(Integer id, String soImei, TrangThaiImei trangThaiImei, String tenModel, String maXuatXu) {
        this.id = id;
        this.soImei = soImei;
        this.trangThaiImei = trangThaiImei.name();
        this.tenModel = tenModel;
        this.maXuatXu = maXuatXu;
    }

    public ImeiDaBangAdminResponse(Integer id, String soImei, String trangThaiImei, Integer idHoaDonChiTiet) {
        this.id = id;
        this.soImei = soImei;
        this.trangThaiImei = trangThaiImei;
        this.idHoaDonChiTiet = idHoaDonChiTiet;
    }
}
