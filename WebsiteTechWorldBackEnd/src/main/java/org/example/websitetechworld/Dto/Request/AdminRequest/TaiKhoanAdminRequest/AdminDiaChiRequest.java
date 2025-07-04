package org.example.websitetechworld.Dto.Request.AdminRequest.TaiKhoanAdminRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "Tên người nhận không được để trống.")
    private String tenNguoiNhan;

    @NotBlank(message = "Số điện thoại người nhận không được để trống.")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại không hợp lệ. Phải bắt đầu bằng 0 và đủ 10 chữ số.")
    private String sdtNguoiNhan;

    @NotBlank(message = "Số nhà không được để trống.")
    private String soNha;

    @NotBlank(message = "Tên đường không được để trống.")
    private String tenDuong;

    @NotBlank(message = "Xã/Phường không được để trống.")
    private String xaPhuong;

    @NotBlank(message = "Quận/Huyện không được để trống.")
    private String quanHuyen;

    @NotBlank(message = "Tỉnh/Thành phố không được để trống.")
    private String tinhThanhPho;

    private Boolean diaChiChinh;

    private Integer idKhachHang;

}

