package org.example.websitetechworld.Dto.Request.AdminRequest.PhanHoiDanhGiaAdminRequest;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhanHoiDanhGiaAdminRequest {
    @Size(max = 500, message = "Nội dung phản hồi không được vượt quá 500 ký tự")
    private String noiDungPhanHoi;
}
