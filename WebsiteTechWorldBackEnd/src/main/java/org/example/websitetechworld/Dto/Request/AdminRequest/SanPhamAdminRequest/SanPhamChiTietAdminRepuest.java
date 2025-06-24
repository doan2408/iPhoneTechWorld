package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;


import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "RAM không được để trống")
    private Integer idRam;

    @NotNull(message = "ROM không được để trống")
    private Integer idRom;

    @NotNull(message = "Màn hình không được để trống")
    private Integer idManHinh;

    @NotNull(message = "Hệ điều hành không được để trống")
    private Integer idHeDieuHanh;

    @NotNull(message = "Pin không được để trống")
    private Integer idPin;

    @NotNull(message = "CPU không được để trống")
    private Integer idCpu;

    @NotNull(message = "Camera trước không được để trống")
    private Integer idCameraTruoc;

    @NotNull(message = "Camera sau không được để trống")
    private Integer idCameraSau;

    @NotNull(message = "Xuất xứ không được để trống")
    private Integer idXuatXu;

    @NotNull(message = "Loại sản phẩm không được để trống")
    private Integer idLoai;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn 0")
    private Integer soLuong;

    @NotNull(message = "Giá bán không được để trống")
    @DecimalMin(value = "1000.0", inclusive = false, message = "Giá bán phải lớn hơn 1.000 VNĐ")
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
