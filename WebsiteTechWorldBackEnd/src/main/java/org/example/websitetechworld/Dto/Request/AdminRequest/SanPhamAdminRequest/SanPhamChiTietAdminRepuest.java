package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;


import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Entity.HinhAnh;
import org.example.websitetechworld.Entity.Imei;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamChiTietAdminRepuest {
    private Integer id;

    private String maSanPhamChiTiet;

    private Integer idSanPham;

    @NotNull(message = "Màu sắc không được để trống")
    private Integer idMau;

    @NotNull(message = "ROM không được để trống")
    private Integer idRom;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer soLuong;

    @NotNull(message = "Vui lòng nhập giá bán sản phẩm")
    @DecimalMin(value = "1000.0", inclusive = true, message = "Giá bán phải lớn hơn 1.000 VNĐ")
    @Digits(integer = 10, fraction = 2, message = "Giá bán tối đa 10 chữ số trước dấu phẩy và không quá 2 số sau dấu phẩy (VD: 9999999999.99)")
    private BigDecimal giaBan;

    @NotNull(message = "Danh sách hình ảnh không được để trống")
    @Size(min = 1, message = "Phải có ít nhất 1 hình ảnh")
    @Valid
    private Set<HinhAnhAdminRequest> hinhAnhs;

    @NotNull(message = "Danh sách IMEI không được để trống")
    @Size(min = 1, message = "Phải có ít nhất 1 IMEI")
    @Valid
    private Set<ImeiAdminRequest> imeis;

}
