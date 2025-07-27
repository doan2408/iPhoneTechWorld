package org.example.websitetechworld.Dto.Response.ClientResponse.SanPhamClientResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThongSoCompareResponse {
    private String cpu;
    private String ram;
    private String rom;

    private String tenManHinh;
    private String kichThuoc;
    private String loaiManHinh;
    private String doPhanGiai;
    private String tanSoQuet;
    private String doSang;
    private String chatLieuKinh;

    private String cameraSau;
    private String cameraTruoc;

    private String phienBanPin;
    private String congXuatSac;
    private String thoiGianSuDung;
    private String soLanSacToiDa;

    private String heDieuHanh;
    private String xuatXu;
    private String tenLoai;

    // cpu
    private String soNhan;
    private String xungNhip;
    private String boNhoDem;
    private String tieuThuDienNang;
    private Date namRaMat;
}
