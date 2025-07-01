package org.example.websitetechworld.Dto.Request.AdminRequest.SanPhamAdminRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.websitetechworld.Enum.Imei.TrangThaiImei;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImeiAdminRequest {
    private Integer id;

    @NotBlank(message = "Số IMEI không được để trống")
    @Size(max = 50, message = "Số IMEI không được vượt quá 50 ký tự")
    private String soImei;


//    @NotNull(message = "Trạng thái IMEI không được để trống")
    private TrangThaiImei trangThaiImei;


//    @NotNull(message = "ID sản phẩm chi tiết không được để trống")
    private Integer idSanPhamChiTiet;

}
