package org.example.websitetechworld.Dto.Request.AdminRequest.HoaDonAdminRequest;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {
    @NotBlank(message = "Tên người nhận không được để trống")
    @Size(min = 2, max = 50, message = "Tên người nhận phải từ 2 đến 50 ký tự")
    private String tenNguoiNhan;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^0\\d{9}$", message = "Số điện thoại không hợp lệ. Phải bắt đầu bằng 0 và có 10 chữ số")
    private String sdtNguoiNhan;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String emailNguoiNhan;

    @NotBlank(message = "Địa chỉ giao hàng không được để trống")
    @Size(min = 5, max = 255, message = "Địa chỉ giao hàng phải từ 5 đến 100 ký tự")
    private String diaChiGiaoHang;
    private String shippingMethod;

    @DecimalMin(value = "0.0", inclusive = true, message = "Phí ship không được âm")
    private BigDecimal phiShip;
    private Boolean isShipping;
    private String maVanDon;
    private BigDecimal thanhTien;
    private LocalDate ngayTaoDonHang;
}
