package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RamAdminRequest {
    private Integer id;

    @NotBlank(message = "Dung lượng không được để trống")
    @Size(max = 50, message = "Dung lượng không được vượt quá 50 ký tự")
    private String dungLuong;

    @Size(max = 50, message = "Loại không được vượt quá 50 ký tự")
    private String loai;

    @Size(max = 100, message = "Tốc độ đọc/ghi không được vượt quá 100 ký tự")
    private String tocDoDocGhi;

    @Size(max = 100, message = "Nhà sản xuất không được vượt quá 100 ký tự")
    private String nhaSanXuat;

    @PastOrPresent(message = "Năm ra mắt phải là quá khứ hoặc hiện tại")
    private LocalDate namRaMat;
}
