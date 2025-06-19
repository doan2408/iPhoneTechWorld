package org.example.websitetechworld.Dto.Request.AdminRequest.PhieuGiamGiaAdminRequest;

import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.PhieuGiamGia.TrangThaiPGG;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    @DecimalMax(value = "99999999.99", message = "Giá trị khuyến mãi tối đa không được vượt quá 99999999.99")
    private BigDecimal giaTriKhuyenMaiToiDa;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @FutureOrPresent(message = "Ngày bắt đầu phải là hôm nay hoặc trong tương lai")
    private LocalDate ngayBatDau;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @Future(message = "Ngày kết thúc phải trong tương lai")
    private LocalDate ngayKetThuc;

    @Size(max = 100, message = "Điều kiện áp dụng không được vượt quá 100 ký tự")
    private String dieuKienApDung;

    @Size(max = 50, message = "Hạng tối thiểu không được vượt quá 50 ký tự")
    private String hangToiThieu;

    @NotNull(message = "Số lượng không được để trống")
    @PositiveOrZero(message = "Số lượng phải lớn hơn hoặc bằng 0")
    private Integer soLuong;

    @PositiveOrZero(message = "Số điểm cần để đổi phải lớn hơn hoặc bằng 0")
    @DecimalMax(value = "99999999.99", message = "Số điểm cần để đổi không được vượt quá 99999999.99")
    private BigDecimal soDiemCanDeDoi;

    @NotNull(message = "Trạng thái isGlobal không được để trống")
    private Boolean isGlobal;

    private List<Integer> khachHangIds;

    @NotNull(message = "Trạng thái không được để trống")
    private TrangThaiPGG trangThai;
}