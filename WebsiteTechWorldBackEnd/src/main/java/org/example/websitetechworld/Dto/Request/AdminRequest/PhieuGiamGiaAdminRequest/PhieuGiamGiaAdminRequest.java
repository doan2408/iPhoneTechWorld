package org.example.websitetechworld.Dto.Request.AdminRequest.PhieuGiamGiaAdminRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.*;
import org.example.websitetechworld.Enum.KhachHang.HangKhachHang;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPhatHanh;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhieuGiamGiaAdminRequest {

    private Integer id;

    @Size(max = 10, message = "Mã giảm giá không được vượt quá 10 ký tự")
    private String maGiamGia;

    @NotBlank(message = "Tên khuyến mãi không được để trống")
    @Size(max = 50, message = "Tên khuyến mãi không được vượt quá 50 ký tự")
    private String tenKhuyenMai;

    @NotBlank(message = "Loại khuyến mãi không được để trống")
    private String loaiKhuyenMai;

    @NotNull(message = "Giá trị khuyến mãi không được để trống")
    @Positive(message = "Giá trị khuyến mãi phải lớn hơn 0")
    @DecimalMax(value = "99999999.99", message = "Giá trị khuyến mãi không được vượt quá 99999999.99")
    private BigDecimal giaTriKhuyenMai;

    @NotNull(message = "Giá trị đơn hàng tối thiểu không được để trống")
    @PositiveOrZero(message = "Giá trị đơn hàng tối thiểu phải lớn hơn hoặc bằng 0")
    @DecimalMax(value = "99999999.99", message = "Giá trị đơn hàng tối thiểu không được vượt quá 99999999.99")
    private BigDecimal giaTriDonHangToiThieu;

    @PositiveOrZero(message = "Giá trị khuyến mãi tối đa phải lớn hơn hoặc bằng 0")
    @DecimalMin(value = "10000.00", message = "Giá trị khuyến mãi tối đa không được ít hơn 10000.00")
    @DecimalMax(value = "99999999.99", message = "Giá trị khuyến mãi tối đa không được vượt quá 99999999.99")
    private BigDecimal giaTriKhuyenMaiToiDa;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate ngayBatDau;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate ngayKetThuc;

    @Size(max = 100, message = "Điều kiện áp dụng không được vượt quá 100 ký tự")
    private String dieuKienApDung;

    private HangKhachHang hangToiThieu;

    @NotNull(message = "Số lượng không được để trống")
    @PositiveOrZero(message = "Số lượng phải lớn hơn hoặc bằng 0")
    private Integer soLuong;

    @PositiveOrZero(message = "Số điểm cần để đổi phải lớn hơn hoặc bằng 0")
    @DecimalMax(value = "99999999.99", message = "Số điểm cần để đổi không được vượt quá 99999999.99")
    private BigDecimal soDiemCanDeDoi;

    @NotNull(message = "Trạng thái không được để trống")
    private TrangThaiPhatHanh trangThaiPhatHanh;
}