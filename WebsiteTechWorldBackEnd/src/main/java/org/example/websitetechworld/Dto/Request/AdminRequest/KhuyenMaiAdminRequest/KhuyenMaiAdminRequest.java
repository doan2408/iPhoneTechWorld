package org.example.websitetechworld.Dto.Request.AdminRequest.KhuyenMaiAdminRequest;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.KhuyenMai.DoiTuongApDung;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class KhuyenMaiAdminRequest {

    private Integer id;

    @Size(max = 50, message = "Mã khuyến mại không được vượt quá 50 ký tự")
    private String maKhuyenMai;

    @NotBlank(message = "Tên khuyến mại không được để trống")
    @Size(max = 255, message = "Tên khuyến mại không được vượt quá 255 ký tự")
    private String tenKhuyenMai;

    private String moTa;

    @NotNull(message = "Phần trăm giảm giá không được để trống")
    @Min(value = 1, message = "Phần trăm giảm phải lớn hơn 0%")
    @Max(value = 60, message = "Phần trăm giảm không được vượt quá 60%")
    private Integer phanTramGiam;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDateTime ngayBatDau;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDateTime ngayKetThuc;

    @NotNull(message = "Đối tượng áp dụng không được để trống")
    private DoiTuongApDung doiTuongApDung;

    @NotNull(message = "Mức độ ưu tiên không được để trống")
    @Min(value = 1, message = "Mức độ ưu tiên phải lớn hơn 0")
    @Max(value = 100, message = "Mức độ ưu tiên không được vượt quá 100")
    private Integer mucDoUuTien;

    private String trangThai;

    @NotEmpty(message = "Phải chọn ít nhất một sản phẩm áp dụng khuyến mại")
    private List<Integer> idSanPhamChiTietList;
}
