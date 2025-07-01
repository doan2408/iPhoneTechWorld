package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CpuAdminRequest {
    private Integer id;

    @NotBlank(message = "Hãng sản xuất không được để trống")
    @Size(max = 50, message = "Hãng sản xuất không được vượt quá 50 ký tự")
    private String hangSanXuat;

    @NotBlank(message = "Chip xử lý không được để trống")
    @Size(max = 50, message = "Chip xử lý không được vượt quá 50 ký tự")
    private String chipXuLy;

    @NotBlank(message = "Số nhân không được để trống")
    @Size(max = 50, message = "Số nhân không được vượt quá 50 ký tự")
    private String soNhan;

    @NotBlank(message = "Xung nhịp không được để trống")
    @Size(max = 50, message = "Xung nhịp không được vượt quá 50 ký tự")
    private String xungNhip;

    @NotBlank(message = "Công nghệ sản xuất không được để trống")
    @Size(max = 50, message = "Công nghệ sản xuất không được vượt quá 50 ký tự")
    private String congNgheSanXuat;

    @NotBlank(message = "Bộ nhớ đệm không được để trống")
    @Size(max = 50, message = "Bộ nhớ đệm không được vượt quá 50 ký tự")
    private String boNhoDem;

    @NotBlank(message = "Tiêu thụ điện năng không được để trống")
    @Size(max = 50, message = "Tiêu thụ điện năng không được vượt quá 50 ký tự")
    private String tieuThuDienNang;

    @NotNull(message = "Năm ra mắt không được để trống")
    @PastOrPresent(message = "Năm ra mắt phải là trong quá khứ hoặc hiện tại")
    private LocalDate namRaMat;
}
