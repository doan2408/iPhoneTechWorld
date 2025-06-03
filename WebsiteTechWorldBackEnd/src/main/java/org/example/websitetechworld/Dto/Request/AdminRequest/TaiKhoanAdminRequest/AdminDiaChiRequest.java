package org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AdminDiaChiRequest {
    private Integer id;

    private String tenNguoiNhan;

    private String sdtNguoiNhan;

    private String soNha;

    private String tenDuong;

    private String xaPhuong;

    private String quanHuyen;

    private String tinhThanhPho;

    private Boolean diaChiChinh;

}

