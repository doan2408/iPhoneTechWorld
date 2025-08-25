package org.example.websitetechworld.Dto.Request.AdminRequest.BaoHanhAdminRequest;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class YeuCauBaoHanhAdminRequest {

    @NotNull(message = "ID bảo hành không được để trống")
    @Min(value = 1, message = "ID bảo hành phải lớn hơn 0")
    private Integer idBaoHanh;

    @NotBlank(message = "Lý do bảo hành không được để trống")
    @Size(min = 10, max = 255, message = "Lý do bảo hành phải từ 10 đến 255 ký tự")
    private String lyDoBaoHanh;
}
