package org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThongTinNguoiNhanAdminRequest {
    private String tenNguoiNhan;
    private String diaChi;
    private String sdt;
}
